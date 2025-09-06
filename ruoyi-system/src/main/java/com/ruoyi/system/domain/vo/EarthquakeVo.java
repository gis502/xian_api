package com.ruoyi.system.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private List<CircleParam> circleParam;
    private Double rotation;
    private Double semiMajorAxis;
    private Double semiMinorAxis;
    private String source;
    private String countyCode;
    private String townshipCode;
    private String district;
    private String province;
    private String city;

    public EarthquakeVo() {
        this.circleParam = new ArrayList<>();
    }

    // 改为静态内部类
    @Data
    public static class CircleParam {  // 关键修改：添加static，且权限改为public（便于外部访问）
        private Double circleArea;
        private Integer intensity;
        private Double rotation;
        private Double semiMajorAxis;
        private Double semiMinorAxis;
    }
}