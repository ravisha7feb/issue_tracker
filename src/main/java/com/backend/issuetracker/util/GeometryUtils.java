package com.backend.issuetracker.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public final class GeometryUtils {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private GeometryUtils() {}

    // Note: x = longitude, y = latitude
    public static Point createPoint(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return GEOMETRY_FACTORY.createPoint(coordinate);
    }
}
