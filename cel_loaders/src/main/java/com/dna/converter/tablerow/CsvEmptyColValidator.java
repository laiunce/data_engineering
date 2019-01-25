package com.dna.converter.tablerow;

/**
 * @author Cristian Laiun

 */

public class CsvEmptyColValidator implements java.io.Serializable {

    public boolean valEmptyCols;
    public int[] colPosToVal;

    public CsvEmptyColValidator(boolean valEmptyCols, int[] colPosToVal) {
        this.valEmptyCols = valEmptyCols;
        this.colPosToVal = colPosToVal;
    }

    public boolean csvHasEmptyCol(String[] values) {
        for (int i = 0; i < colPosToVal.length; i++) {
            if (values[colPosToVal[i]].isEmpty()) return true;
        }
        return false;
    }
}
