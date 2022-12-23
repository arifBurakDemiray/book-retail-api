package com.bookretail.export;

import java.util.Date;
import java.util.UUID;

public class ExampleExportable implements IExportable {
    private static final String[] names = new String[]{
            "String",
            "Integer",
            "Double",
            "Date",
            "Date formatted",
            "String",
    };
    private final Object[] values;

    public ExampleExportable() {
        values = new Object[]{
                UUID.randomUUID().toString(),
                5,
                Math.PI,
                new Date(),
                new Date().toString(),
                UUID.randomUUID().toString(),
        };
    }

    @Override
    public String getName(int i) {
        return names[i];
    }

    @Override
    public Object getValue(int i) {
        return values[i];
    }

    @Override
    public int getLength() {
        return names.length;
    }
}
