package com.dna.converter.tablerow;

import com.dna.converter.StringConverter;
import com.dna.exception.BqParseException;
import com.google.api.services.bigquery.model.TableRow;

/**
 * @author Cristian Laiun

 */
public interface StringToTableRowConverter extends StringConverter<TableRow> {

    @Override
    TableRow convert(String input) throws BqParseException;

}
