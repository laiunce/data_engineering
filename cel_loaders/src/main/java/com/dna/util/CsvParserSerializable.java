package com.dna.util;

import com.opencsv.CSVParser;

import java.io.Serializable;

/**
 * @author Cristian Laiun

 * Just a serializable version of CSVParser
 */
public class CsvParserSerializable extends CSVParser implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final CsvParserSerializable CSV_PARSER = new CsvParserSerializable();
    public static final CsvParserSerializable CSV_PARSER_TAB = new CsvParserSerializable('\t');
    public static final CsvParserSerializable CSV_PARSER_TAB_IGNORE_QUOTES = new CsvParserSerializable('\t', true);

    public CsvParserSerializable() {
        super();
    }

    public CsvParserSerializable(char separator) {
        super(separator);
    }
    public CsvParserSerializable(boolean ignoreQuotes) {
        this(DEFAULT_SEPARATOR, DEFAULT_IGNORE_QUOTATIONS);
    }

    public CsvParserSerializable(char separator, boolean ignoreQuotes) {
        this(DEFAULT_SEPARATOR,
                DEFAULT_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER,
                DEFAULT_STRICT_QUOTES,
                DEFAULT_IGNORE_LEADING_WHITESPACE, ignoreQuotes);
    }


    public CsvParserSerializable(char separator, char quotechar) {
        super(separator, quotechar);
    }

    public CsvParserSerializable(char separator, char quotechar, char escape) {
        super(separator, quotechar, escape);
    }

    public CsvParserSerializable(char separator, char quotechar, char escape, boolean strictQuotes) {
        super(separator, quotechar, escape, strictQuotes);
    }

    public CsvParserSerializable(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        super(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace);
    }

    public CsvParserSerializable(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace, boolean ignoreQuotations) {
        super(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace, ignoreQuotations);
    }

}
