package com.bookretail.factory;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class SpatialFactory {

    private final static GeometryFactory factory = new GeometryFactory();

    public static Point createDatabasePoint(@NotNull org.springframework.data.geo.Point point) {
        return factory.createPoint(createCoordinate(point.getX(), point.getY()));

    }

    /**
     * This function creates a polygon like a rectangle, the creation of coordinates
     * row must be kept like
     * LeftTop -> RightTop -> RightBottom -> LeftBottom -> LeftTop
     * To finish shape, we must end it with starting coordinate
     *
     * @param leftLongitude  is the x left coordinate of the screen
     * @param topLatitude    is the y top coordinate of the screen
     * @param rightLongitude is the x right coordinate of the screen
     * @param bottomLatitude is the y bottom coordinate of the screen
     * @return creates polygon with the fiven sequence
     */
    public static Polygon createPolygon(Double leftLongitude, Double topLatitude,
                                        Double rightLongitude, Double bottomLatitude) {

        Coordinate[] points = {
                createCoordinate(leftLongitude, topLatitude),     //LeftTop
                createCoordinate(rightLongitude, topLatitude),    //RightTop
                createCoordinate(rightLongitude, bottomLatitude), //RightBottom
                createCoordinate(leftLongitude, bottomLatitude),  //LeftBottom
                createCoordinate(leftLongitude, topLatitude)};    //LeftTop

        return factory.createPolygon(points);
    }

    private static Coordinate createCoordinate(Double x, Double y) {
        return new Coordinate(x, y, 0);
    }

    public static org.springframework.data.geo.Point createSpringPoint(Point point) {
        return new org.springframework.data.geo.Point(point.getX(), point.getY());
    }
}
