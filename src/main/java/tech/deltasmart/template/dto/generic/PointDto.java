package com.bookretail.dto.generic;

public class PointDto extends org.springframework.data.geo.Point {

    public PointDto(double x, double y) {
        super(x, y);
    }

    public PointDto(org.springframework.data.geo.Point point) {
        super(point);
    }

    public PointDto() {
        super(0, 0);
    }
}
