package com.ruoyi.system.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author wzy
 * @description: TODO(前端地震实体)
 * @date 2025/7/28 下午12:37
 */
public class EarthquakeVo implements Serializable {
    // 主数据字段
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getTownshipCode() {
        return townshipCode;
    }

    public void setTownshipCode(String townshipCode) {
        this.townshipCode = townshipCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EarthquakeVo that = (EarthquakeVo) o;
        return Objects.equals(name, that.name) && Objects.equals(fullName, that.fullName) && Objects.equals(position, that.position) && Objects.equals(magnitude, that.magnitude) && Objects.equals(depth, that.depth) && Objects.equals(longitude, that.longitude) && Objects.equals(latitude, that.latitude) && Objects.equals(dateTime, that.dateTime) && Objects.equals(type, that.type) && Objects.equals(source, that.source) && Objects.equals(countyCode, that.countyCode) && Objects.equals(townshipCode, that.townshipCode) && Objects.equals(district, that.district) && Objects.equals(province, that.province) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fullName, position, magnitude, depth, longitude, latitude, dateTime, type, source, countyCode, townshipCode, district, province, city);
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