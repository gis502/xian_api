package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("analysis_hospital")
public class Hospital {

    @TableId
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("address")
    private String address;
    @TableField("type_code")
    private String typeCode;
    @TableField("type")
    private String type;
    @TableField("level")
    private String level;
    @TableField("institution_nature")
    private String institutionNature;
    @TableField("devices")
    private Integer hospitalDevices;
    @TableField("workers")
    private Integer hospitalWorkers;
    @TableField("sum_people")
    private Integer sumPeople;
    @TableField("beds")
    private Integer beds;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("country")
    private String country;
    @TableField("unit_head")
    private String unitHead;
    @TableField("telephone")
    private String telephone;
    @TableField("longitude")
    private Double longitude;
    @TableField("latitude")
    private Double latitude;

}
