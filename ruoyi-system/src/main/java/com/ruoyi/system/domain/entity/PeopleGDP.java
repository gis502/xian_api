package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_GDP_people")
public class PeopleGDP {

    /**主键ID*/
    @TableId("id")
    private Integer id;

    /**人均GDP*/
    @TableField("gdp")
    private Float gdp;

    /**人口数量*/
    @TableField("people_num")
    private Integer peopleNum;

    /**省份*/
    @TableField("province")
    private String province;

    /**城市*/
    @TableField("city")
    private String city;

    /**区县*/
    @TableField("county")
    private String county;

    /**村庄（街道）*/
    @TableField("villages")
    private String villages;

    /**区县编码*/
    @TableField("county_code")
    private Integer countyCode;

    /**经度*/
    @TableField("lon")
    private Float lon;

    /**纬度*/
    @TableField("lat")
    private Float lat;
}
