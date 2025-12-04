package com.ruoyi.common.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * @author: xiaodemos
 * @date: 2025-08-08 0:12
 * @description: 经纬度转换类
 */


public class GeometryUtils {

    // 使用SRID 4490（国家大地坐标系）创建GeometryFactory，匹配数据库表要求
    private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4490);

    // 转换经纬度点
    public static Point convertPoint(double longitude, double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        // 显式设置SRID以确保一致性
        point.setSRID(4490);
        return point;
    }

    // TODO 转换经纬度面

    // TODO 转换经纬度线

}
