package com.dna.converter.tablerow;

import com.dna.converter.Converter;
import com.dna.exception.BqParseException;
import com.google.api.services.bigquery.model.TableRow;

/**
 * @author Cristian Laiun

 */
public interface TableRowConverter<I> extends Converter<I, TableRow> {

    @Override
    TableRow convert(I input) throws BqParseException;

}
