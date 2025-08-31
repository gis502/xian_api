package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author wzy
 * @description: TODO(前端地震实体)
 * @date 2025/7/28 下午12:37
 */
@Data
public class EarthquakeVo implements Serializable {
    // 主数据字段
    private Integer disasterId;
    private String name;
    private String fullName;
    private String position;
    private Double magnitude;
    private Double depth;
    private Double longitude;
    private Double latitude;
    private String dateTime;
    private String type;

    // 非必须数据字段
    private String source;
    private String countyCode;
    private String townshipCode;
    private String district;
    private String province;
    private String city;
    private Double circleArea;
    private Double rotation;
    private Double semiMajorAxis;
    private Double semiMinorAxis;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EarthquakeVo that = (EarthquakeVo) o;
        return Objects.equals(name, that.name) && Objects.equals(fullName, that.fullName) && Objects.equals(position, that.position) && Objects.equals(magnitude, that.magnitude) && Objects.equals(depth, that.depth) && Objects.equals(longitude, that.longitude) && Objects.equals(latitude, that.latitude)&& Objects.equals(semiMajorAxis, that.semiMajorAxis) && Objects.equals(semiMinorAxis, that.semiMinorAxis)&& Objects.equals(rotation, that.rotation)&& Objects.equals(circleArea, that.circleArea)&& Objects.equals(dateTime, that.dateTime) && Objects.equals(type, that.type) && Objects.equals(source, that.source) && Objects.equals(countyCode, that.countyCode) && Objects.equals(townshipCode, that.townshipCode) && Objects.equals(district, that.district) && Objects.equals(province, that.province) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fullName, position, magnitude, depth, longitude, latitude,semiMajorAxis,semiMinorAxis,rotation,circleArea, dateTime, type, source, countyCode, townshipCode, district, province, city);
    }

    @Override
    public String toString() {
        return "EarthquakeVo{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", position='" + position + '\'' +
                ", magnitude=" + magnitude +
                ", depth=" + depth +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", semiMajorAxis=" + semiMajorAxis +
                ", semiMinorAxis=" + semiMinorAxis +
                ", rotation=" + rotation +
                ", circleArea=" + circleArea +
                ", dateTime='" + dateTime + '\'' +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", countyCode='" + countyCode + '\'' +
                ", townshipCode='" + townshipCode + '\'' +
                ", district='" + district + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}