package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 农作物信息实体类
 */
@Data
@TableName("xian_crops")
public class Crops {

    /** 主键ID */
    @TableId("id")
    private Long id;

    /** 水稻种植面积 */
    @TableField("rice_area")
    private Integer riceArea;

    /** 玉米种植面积 */
    @TableField("maiz_area")
    private Double maizArea;

    /** 小麦种植面积 */
    @TableField("wheat_area")
    private Double wheatArea;

    /** 省名称 */
    @TableField("province_name")
    private String provinceName;

    /** 市名称 */
    @TableField("city_name")
    private String cityName;

    /** 县名称 */
    @TableField("county_name")
    private String countyName;

    /** 乡镇名称 */
    @TableField("township_name")
    private String townshipName;

    /** 几何信息 (WKT格式) */
    @TableField("point")
    private String point;

    private String pointGeom;
}
