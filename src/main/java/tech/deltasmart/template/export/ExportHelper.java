package com.bookretail.export;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ExportHelper {

    protected static void setCellValue(Cell cell, Object data) {
        if (data instanceof String) {
            cell.setCellValue((String) data);
        } else if (data instanceof Double) {
            cell.setCellValue((Double) data);
        } else if (data instanceof Integer) {
            cell.setCellValue((Integer) data);
        } else if (data instanceof Long) {
            cell.setCellValue((Long) data);
        } else if (data instanceof Date) {
            cell.setCellValue((Date) data);
        } else if (data instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) data);
        } else if (data instanceof Calendar) {
            cell.setCellValue((Calendar) data);
        }
    }
}
