package com.bookretail.export;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelTest {

    private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";

    public static void main(String[] args) {

        var workbook = ExcelExporter.builder()
                .sheet("test sheet")
                .data(IntStream.range(0, 10000).mapToObj(i -> new ExampleExportable()).collect(Collectors.toList()))
                .resize()
                .build();

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}
