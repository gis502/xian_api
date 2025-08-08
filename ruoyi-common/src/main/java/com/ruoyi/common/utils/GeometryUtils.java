package com.ruoyi.common.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * @author: xiaodemos
 * @date: 2025-08-08 0:12
 * @description: 经纬度转换类
 */


public class GeometryUtils {


    private static GeometryFactory geometryFactory = new GeometryFactory();

    // 转换经纬度点
    public static Point convertPoint(double longitude, double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }

    // TODO 转换经纬度面

    // TODO 转换经纬度线

}
