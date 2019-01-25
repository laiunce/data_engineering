package com.dna.util;

import com.google.common.annotations.VisibleForTesting;
import static com.google.common.base.Preconditions.checkState;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.Coder.Context;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.extensions.gcp.options.GcsOptions;
import org.apache.beam.sdk.io.CompressedSource;
import org.apache.beam.sdk.io.FileBasedSource;
import org.apache.beam.sdk.io.Read;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.fs.MatchResult.Metadata;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;
import static org.apache.beam.sdk.repackaged.com.google.common.base.Preconditions.checkNotNull;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.display.DisplayData;
import org.apache.beam.sdk.util.gcsfs.GcsPath;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PInput;

public class TextSourceSkipLeading<T> extends FileBasedSource<T> {
    /**
     * The Coder to use to decode each line.
     */
    private final Coder<T> coder;
    private int skipLeadingRows = 0;

    @VisibleForTesting
    TextSourceSkipLeading(ValueProvider<String> fileSpec, Coder<T> coder, int skipLeadingRows) {
        super(fileSpec, 1L);
        this.coder = coder;
        this.skipLeadingRows = skipLeadingRows;
    }

    private TextSourceSkipLeading(Metadata fileName, long start, long end, Coder<T> coder, int skipLeadingRows) {
        super(fileName, 1L, start, end);
        this.coder = coder;
        this.skipLeadingRows = skipLeadingRows;
    }

    public static CsvBoundSkipLeading<String> from(String fileOrPatternSpec, int skipLeadingRows) {
        return new CsvBoundSkipLeading<>(StringUtf8Coder.of()).from(fileOrPatternSpec, skipLeadingRows);
    }

    @Override
    protected FileBasedSource<T> createForSubrangeOfFile(Metadata fileMetadata, long start, long end) {
        return new TextSourceSkipLeading<>(fileMetadata, start, end, coder, skipLeadingRows);
    }

    @Override
    protected FileBasedSource.FileBasedReader<T> createSingleFileReader(PipelineOptions options) {
        return new TextBasedReader<>(this, skipLeadingRows);
    }

    @Override
    public Coder<T> getDefaultOutputCoder() {
        return coder;
    }

    /**
     * A {@link org.apache.beam.sdk.io.FileBasedSource.FileBasedReader FileBasedReader}
     * which can decode records delimited by newline characters.
     * <p>
     * <p>See {@link TextSourceSkipLeading} for further details.
     */
    @VisibleForTesting
    static class TextBasedReader<T> extends FileBasedSource.FileBasedReader<T> {
        private static final int READ_BUFFER_SIZE = 8192;
        private final Coder<T> coder;
        private final ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        private ByteString buffer;
        private int startOfSeparatorInBuffer;
        private int endOfSeparatorInBuffer;
        private long startOfRecord;
        private volatile long startOfNextRecord;
        private volatile boolean eof;
        private volatile boolean elementIsPresent;
        private T currentValue;
        private ReadableByteChannel inChannel;
        Integer skipLeadingRows;

        private TextBasedReader(TextSourceSkipLeading<T> source, Integer skipLeadingRows) {
            super(source);
            coder = source.coder;
            buffer = ByteString.EMPTY;
            this.skipLeadingRows = skipLeadingRows;
        }

        @Override
        protected long getCurrentOffset() throws NoSuchElementException {
            if (!elementIsPresent) {
                throw new NoSuchElementException();
            }
            return startOfRecord;
        }

        @Override
        public long getSplitPointsRemaining() {
            if (isStarted() && startOfNextRecord >= getCurrentSource().getEndOffset()) {
                return isDone() ? 0 : 1;
            }
            return super.getSplitPointsRemaining();
        }

        @Override
        public T getCurrent() throws NoSuchElementException {
            if (!elementIsPresent) {
                throw new NoSuchElementException();
            }
            return currentValue;
        }

        @Override
        protected void startReading(ReadableByteChannel channel) throws IOException {
            this.inChannel = channel;
            // If the first offset is greater than zero, we need to skip bytes until we see our
            // first separator.
            if (getCurrentSource().getStartOffset() > 0) {
                checkState(channel instanceof SeekableByteChannel,
                        "%s only supports reading from a SeekableByteChannel when given a start offset"
                                + " greater than 0.", TextSourceSkipLeading.class.getSimpleName());
                long requiredPosition = getCurrentSource().getStartOffset() - 1;
                ((SeekableByteChannel) channel).position(requiredPosition);
                findSeparatorBounds();
                buffer = buffer.substring(endOfSeparatorInBuffer);
                startOfNextRecord = requiredPosition + endOfSeparatorInBuffer;
                endOfSeparatorInBuffer = 0;
                startOfSeparatorInBuffer = 0;
            }
            //for (int i = 0; i < skipLeadingRows && readNextRecord(); i++) ; // TODO implement this some other way
        }

        /**
         * Locates the start position and end position of the next delimiter. Will
         * consume the channel till either EOF or the delimiter bounds are found.
         * <p>
         * <p>This fills the buffer and updates the positions as follows:
         * <pre>{@code
         * ------------------------------------------------------
         * | element bytes | delimiter bytes | unconsumed bytes |
         * ------------------------------------------------------
         * 0            start of          end of              buffer
         *              separator         separator           size
         *              in buffer         in buffer
         * }</pre>
         */
        private void findSeparatorBounds() throws IOException {
            int bytePositionInBuffer = 0;
            while (true) {
                if (!tryToEnsureNumberOfBytesInBuffer(bytePositionInBuffer + 1)) {
                    startOfSeparatorInBuffer = endOfSeparatorInBuffer = bytePositionInBuffer;
                    break;
                }

                byte currentByte = buffer.byteAt(bytePositionInBuffer);

                if (currentByte == '\n') {
                    startOfSeparatorInBuffer = bytePositionInBuffer;
                    endOfSeparatorInBuffer = startOfSeparatorInBuffer + 1;
                    break;
                } else if (currentByte == '\r') {
                    startOfSeparatorInBuffer = bytePositionInBuffer;
                    endOfSeparatorInBuffer = startOfSeparatorInBuffer + 1;

                    if (tryToEnsureNumberOfBytesInBuffer(bytePositionInBuffer + 2)) {
                        currentByte = buffer.byteAt(bytePositionInBuffer + 1);
                        if (currentByte == '\n') {
                            endOfSeparatorInBuffer += 1;
                        }
                    }
                    break;
                }

                // Move to the next byte in buffer.
                bytePositionInBuffer += 1;
            }
        }

        @Override
        protected boolean readNextRecord() throws IOException {
            startOfRecord = startOfNextRecord;
            findSeparatorBounds();

            // If we have reached EOF file and consumed all of the buffer then we know
            // that there are no more records.
            if (eof && buffer.size() == 0) {
                elementIsPresent = false;
                return false;
            }

            decodeCurrentElement();
            startOfNextRecord = startOfRecord + endOfSeparatorInBuffer;
            return true;
        }

        /**
         * Decodes the current element updating the buffer to only contain the unconsumed bytes.
         * <p>
         * <p>This invalidates the currently stored {@code startOfSeparatorInBuffer} and
         * {@code endOfSeparatorInBuffer}.
         */
        private void decodeCurrentElement() throws IOException {
            ByteString dataToDecode = buffer.substring(0, startOfSeparatorInBuffer);
            currentValue = coder.decode(dataToDecode.newInput(), Context.OUTER);
            elementIsPresent = true;
            buffer = buffer.substring(endOfSeparatorInBuffer);
        }

        /**
         * Returns false if we were unable to ensure the minimum capacity by consuming the channel.
         */
        private boolean tryToEnsureNumberOfBytesInBuffer(int minCapacity) throws IOException {
            // While we aren't at EOF or haven't fulfilled the minimum buffer capacity,
            // attempt to read more bytes.
            while (buffer.size() <= minCapacity && !eof) {
                eof = inChannel.read(readBuffer) == -1;
                readBuffer.flip();
                buffer = buffer.concat(ByteString.copyFrom(readBuffer));
                readBuffer.clear();
            }
            // Return true if we were able to honor the minimum buffer capacity request
            return buffer.size() >= minCapacity;
        }
    }


    public static class CsvBoundSkipLeading<T> extends PTransform<PInput, PCollection<T>> {
        /**
         * The filepattern to read from.
         */
        @Nullable
        private final ValueProvider<String> filepattern;

        /**
         * The Coder to use to decode each line.
         */
        private final Coder<T> coder;

        /**
         * An option to indicate if input validation is desired. Default is true.
         */
        private final boolean validate;

        /**
         * Option to indicate the input source's compression type. Default is AUTO.
         */
        private final TextIO.CompressionType compressionType;

        private final int skipLeadingRows;

        public CsvBoundSkipLeading(Coder<T> coder) {
            this(null, null, coder, true, TextIO.CompressionType.AUTO, 0);
        }

        private CsvBoundSkipLeading(@Nullable String name, @Nullable ValueProvider<String> filepattern,
                                    Coder<T> coder, boolean validate, TextIO.CompressionType compressionType, int skipLeadingRows) {
            super(name);
            this.coder = coder;
            this.filepattern = filepattern;
            this.validate = validate;
            this.compressionType = compressionType;
            this.skipLeadingRows = skipLeadingRows;
        }

        /**
         * Returns a new transform for reading from text files that's like this one but
         * with the given step name.
         * <p>
         * <p>Does not modify this object.
         */
        public CsvBoundSkipLeading<T> named(String name) {
            return new CsvBoundSkipLeading<>(name, filepattern, coder, validate, compressionType, skipLeadingRows);
        }

        /**
         * Returns a new transform for reading from text files that's like this one but
         * that reads from the file(s) with the given name or pattern. See {@link TextIO.Read#from}
         * for a description of filepatterns.
         * <p>
         * <p>Does not modify this object.
         */
        public CsvBoundSkipLeading<T> from(String filepattern, int skipLeadingRows) {
            checkNotNull(filepattern, "Filepattern cannot be empty.");
            return new CsvBoundSkipLeading<>(name, ValueProvider.StaticValueProvider.of(filepattern), coder, validate,
                    compressionType, skipLeadingRows);
        }

        /**
         * Same as {@code from(filepattern)}, but accepting a {@link ValueProvider}.
         */
        public CsvBoundSkipLeading<T> from(ValueProvider<String> filepattern) {
            checkNotNull(filepattern, "Filepattern cannot be empty.");
            return new CsvBoundSkipLeading<>(name, filepattern, coder, validate, compressionType, skipLeadingRows);
        }

        /**
         * Returns a new transform for reading from text files that's like this one but
         * that uses the given {@link Coder} to decode each of the
         * lines of the file into a value of type {@code X}.
         * <p>
         * <p>Does not modify this object.
         *
         * @param <X> the type of the decoded elements, and the
         *            elements of the resulting PCollection
         */
        public <X> CsvBoundSkipLeading<X> withCoder(Coder<X> coder) {
            return new CsvBoundSkipLeading<>(name, filepattern, coder, validate, compressionType, skipLeadingRows);
        }

        /**
         * Returns a new transform for reading from text files that's like this one but
         * that has GCS path validation on csvtobigquery creation disabled.
         * <p>
         * <p>This can be useful in the case where the GCS input does not
         * exist at the csvtobigquery creation time, but is expected to be
         * available at execution time.
         * <p>
         * <p>Does not modify this object.
         */
        public CsvBoundSkipLeading<T> withoutValidation() {
            return new CsvBoundSkipLeading<>(name, filepattern, coder, false, compressionType, skipLeadingRows);
        }

        /**
         * Returns a new transform for reading from text files that's like this one but
         * reads from input sources using the specified compression type.
         * <p>
         * <p>If no compression type is specified, the default is {@link TextIO.CompressionType#AUTO}.
         * See {@link TextIO.Read#withCompressionType} for more details.
         * <p>
         * <p>Does not modify this object.
         */

        public CsvBoundSkipLeading<T> withCompressionType(TextIO.CompressionType compressionType) {
            return new CsvBoundSkipLeading<>(name, filepattern, coder, validate, compressionType, skipLeadingRows);
        }

        @Override
        public void validate(PipelineOptions options) {
            super.validate(options);
            if (validate) {
                checkState(filepattern.isAccessible(),
                        "Cannot validate with a filepattern provided at runtime.");
                try {
                    GcsOptions gcsOptions = options.as(GcsOptions.class);
                    List<GcsPath> paths = gcsOptions.getGcsUtil().expand(GcsPath.fromUri(filepattern.get()));
                    List<String>filesToProcess = paths.stream().map(item -> item.toString()).collect(Collectors.toList());
                    checkState(
                            !filesToProcess.isEmpty(),
                            "Unable to find any files matching %s",
                            filepattern);
                } catch (IOException e) {
                    throw new IllegalStateException(
                            String.format("Failed to validate %s", filepattern.get()), e);
                }
            }
        }

        @Override
        public PCollection<T> expand(PInput input) {
            if (filepattern == null) {
                throw new IllegalStateException("need to set the filepattern of a TextIO.Read transform");
            }

            final Read.Bounded<T> read = org.apache.beam.sdk.io.Read.from(getSource());
            PCollection<T> pcol = input.getPipeline().apply("Read", read);
            // Honor the default output coder that would have been used by this PTransform.
            pcol.setCoder(getDefaultOutputCoder());
            return pcol;
        }

        // Helper to create a source specific to the requested compression type.
        protected FileBasedSource<T> getSource() {
            switch (compressionType) {
                case UNCOMPRESSED:
                    return new TextSourceSkipLeading<T>(filepattern, coder, skipLeadingRows);
                case AUTO:
                    return CompressedSource.from(new TextSourceSkipLeading<T>(filepattern, coder, skipLeadingRows));
                case BZIP2:
                    return
                            CompressedSource.from(new TextSourceSkipLeading<T>(filepattern, coder, skipLeadingRows))
                                    .withDecompression(CompressedSource.CompressionMode.BZIP2);
                case GZIP:
                    return
                            CompressedSource.from(new TextSourceSkipLeading<T>(filepattern, coder, skipLeadingRows))
                                    .withDecompression(CompressedSource.CompressionMode.GZIP);
                default:
                    throw new IllegalArgumentException("Unknown compression type: " + compressionType);
            }
        }

        @Override
        public void populateDisplayData(DisplayData.Builder builder) {
            super.populateDisplayData(builder);

            builder
                    .add(DisplayData.item("compressionType", compressionType.toString())
                            .withLabel("Compression Type"))
                    .addIfNotDefault(DisplayData.item("validation", validate)
                            .withLabel("Validation Enabled"), true)
                    .addIfNotNull(DisplayData.item("filePattern", filepattern)
                            .withLabel("File Pattern"));
        }

        @Override
        protected Coder<T> getDefaultOutputCoder() {
            return coder;
        }

        public String getFilepattern() {
            return filepattern.get();
        }

        public boolean needsValidation() {
            return validate;
        }

        public TextIO.CompressionType getCompressionType() {
            return compressionType;
        }
    }

}
