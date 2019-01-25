package com.dna.converter;

import com.dna.util.BqDataType;
import com.dna.util.StringUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun

 */
public class BqStringConverterHandler implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(BqStringConverterHandler.class);

    private static final long serialVersionUID = -3502661044149533170L;

    private static final DateTimeFormatter BQ_DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final StringConverter<Long> longConverter;
    private final StringConverter<Number> floatConverter;
    private final StringConverter<LocalDate> dateConverter;
    private final StringConverter<Boolean> booleanConverter;

    protected BqStringConverterHandler(Builder builder) {
        this.longConverter = builder.longConverter;
        this.floatConverter = builder.floatConverter;
        this.dateConverter = builder.dateConverter;
        this.booleanConverter = builder.booleanConverter;
    }

    public Object parseValue(String value, BqDataType type) throws Exception {

        if (StringUtils.isBlank(value) && type != null) {

            switch (type) {
                case STRING:
                {
                    LOG.warn(String.format("Got NULL value for %s. Will set to default \"\" based on type.", type.toString()));
                    value = "";
                    break;
                }
                case INTEGER:
                case FLOAT:
                {
                    LOG.warn(String.format("Got NULL value for %s. Will set to default \"0\" based on type.", type.toString()));
                    value = "0";
                    break;
                }
                // for BOOLEAN, DATE, TIMESTAMP we keep it "null"/""
                default: 
                {
                    LOG.warn(String.format("Got NULL value for %s. Will set to default \"\" which will likely cause an Exception.", type.toString()));
                    value = ""; // was already null/blank, this is just for clarity
                }
            }
        }

        value = sanitizeString(value);

        if (type != BqDataType.STRING) {
            value = StringUtil.trim(value);
            if (value.equals("")) {
                return null;
            }
        }

        if (type == BqDataType.INTEGER || type == BqDataType.FLOAT) {
            // if we are dealing with numbers, filter to our finite set of values
            value = value.replaceAll("[^0-9\\.]", "");
        }

        if (type == BqDataType.INTEGER) {
            return longConverter.convert(value);
        } else if (type == BqDataType.FLOAT) {
            return floatConverter.convert(value).toString();
        } else if (type == BqDataType.DATE) {
            return dateConverter.convert(value).format(BQ_DATE_FORMATTER);
        } else if (type == BqDataType.BOOLEAN) {
            return booleanConverter.convert(value);
        }

        return value;
    }

    public static class Builder {

        private StringConverter<Long> longConverter;
        private StringConverter<Number> floatConverter;
        private StringConverter<LocalDate> dateConverter;
        private StringConverter<Boolean> booleanConverter;

        public Builder setLongConverter(StringConverter<Long> longConverter) {
            this.longConverter = longConverter;
            return this;
        }

        public Builder setFloatConverter(StringConverter<Number> floatConverter) {
            this.floatConverter = floatConverter;
            return this;
        }

        public Builder setDateConverter(StringConverter<LocalDate> dateConverter) {
            this.dateConverter = dateConverter;
            return this;
        }

        public Builder setBooleanConverter(StringConverter<Boolean> booleanConverter) {
            this.booleanConverter = booleanConverter;
            return this;
        }

        public BqStringConverterHandler build() {
            if (longConverter == null) longConverter = createDefaultLongParser();
            if (floatConverter == null) floatConverter = createDefaultFloatParser();
            if (dateConverter == null) dateConverter = createDefaultDateParser();
            if (booleanConverter == null) booleanConverter = createDefaultBooleanParser();
            return new BqStringConverterHandler(this);
        }

        private StringConverter<Long> createDefaultLongParser() {
            return (value) -> {
                float f = Float.parseFloat(value);
                if (f % 1 != 0) throw new ParseException("Cannot parse a float as a long: " + value, 0);
                return (long) f;
            };
        }

        private StringConverter<Number> createDefaultFloatParser() {//5 decimal places
            return (value) -> new BigDecimal(value).setScale(5, RoundingMode.CEILING);

        }

        private StringConverter<Boolean> createDefaultBooleanParser() {
            return (value) -> (value != null) && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("y"));
        }

        private StringConverter<LocalDate> createDefaultDateParser() {
            return DateConverter.ISO_DATE_PARSER;
        }
    }

    public String sanitizeString(String s) {
        return s.replaceAll("[\\x00]", "");
    }
}