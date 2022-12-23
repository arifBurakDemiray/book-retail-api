package com.bookretail.export;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ExcelExporter {
    @Getter
    private final XSSFWorkbook workbook;
    @Getter
    private Sheet sheet;
    private List<IExportable> data;

    private ExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    public static ExcelExporter builder() {
        return new ExcelExporter();
    }

    public ExcelExporter sheet() {
        sheet = workbook.createSheet();

        return this;
    }

    public ExcelExporter sheet(String name) {
        sheet = workbook.createSheet(name);

        return this;
    }

    public ExcelExporter data(List<IExportable> data) {
        checkIfSheetPresent();
        this.data = data;

        var row = sheet.createRow(0);
        var columns = data.get(0);

        var headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);

        for (int i = 0; i < columns.getLength(); i++) {
            var cell = row.createCell(i);
            cell.setCellValue(columns.getName(i));
            cell.setCellStyle(headerStyle);
        }

        var pt = new PropertyTemplate();
        pt.drawBorders(
                new CellRangeAddress(0, 1, 0, columns.getLength() - 1),
                BorderStyle.THIN,
                BorderExtent.ALL
        );
        pt.applyBorders(sheet);


        int iRow = 1;
        for (var currentData : data) {
            row = sheet.createRow(iRow++);
            int iCol = 0;

            for (int i = 0; i < currentData.getLength(); i++) {
                var cell = row.createCell(iCol++);
                var field = currentData.getValue(i);

                ExportHelper.setCellValue(cell, field);
            }
        }

        return this;
    }

    public ExcelExporter resize() {
        checkIfSheetPresent();
        checkIfDataPresent();

        for (int i = 0; i < data.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        return this;
    }

    public XSSFWorkbook build() {
        return workbook;
    }

    private void checkIfSheetPresent() {
        if (sheet == null) {
            throw new IllegalStateException("");
        }
    }

    private void checkIfDataPresent() {
        if (data == null) {
            throw new IllegalStateException("");
        }
    }
}
