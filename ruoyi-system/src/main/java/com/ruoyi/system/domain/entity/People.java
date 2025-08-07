package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 人口信息实体类
 */
@Data
@TableName("xian_people")
public class People {

    /** 主键ID */
    @TableId("id")
    private Integer id;

    /** 人口数量 */
    @TableField("people_num")
    private Integer peopleNum;

    /** 省份 */
    @TableField("province")
    private String province;

    /** 城市 */
    @TableField("city")
    private String city;

    /** 县区 */
    @TableField("county")
    private String county;

    /** 国家 */
    @TableField("country")
    private String country;

    /** 物理键 */
    @TableField("physical_key")
    private String physicalKey;

    /** 更新时间 */
    @TableField("updatetime")
    private String updatetime;

    /** 写入时间 */
    @TableField("writetime")
    private String writetime;

    /** 几何信息 (WKT格式) */
    @TableField("point")
    private String point;
}