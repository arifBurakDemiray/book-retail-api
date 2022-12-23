package com.bookretail.export;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class CellBuilder {
    private CellStyle style;
    private CellStyle customStyle = null;
    private Object data = null;
    private int index = 0;
    private Row row;

    private CellBuilder() {

    }

    public static CellBuilder builder() {
        return new CellBuilder();
    }

    public CellBuilder resetIndex() {
        index = 0;
        return this;
    }

    public CellBuilder withRow(Row row) {
        this.row = row;
        return this;
    }

    public CellBuilder withStyle(CellStyle style) {
        this.style = style;
        return this;
    }

    public CellBuilder withCustomStyle(CellStyle customStyle) {
        this.customStyle = customStyle;
        return this;
    }

    public CellBuilder withIndex(int index) {
        this.index = index;
        return this;
    }

    public CellBuilder withData(Object data) {
        this.data = data;
        return this;
    }

    public CellBuilder withoutIndexIncrement() {
        index--;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public void build() {
        if (row == null) {
            return;
        }
        var tempCell = row.createCell(index);
        if (data != null) {
            ExportHelper.setCellValue(tempCell, data);
        }
        if (customStyle != null) {
            tempCell.setCellStyle(customStyle);
        } else if (style != null) {
            tempCell.setCellStyle(style);
        }
        customStyle = null;
        data = null;
        index++;
    }
}
