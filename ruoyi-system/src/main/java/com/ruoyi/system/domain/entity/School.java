package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_school")
public class School {


    @TableField("id")
    private Integer id;
    @TableField("school_name")
    private String schoolName;
    @TableField("school_address")
    private String schoolAddress;
    @TableField("school_type")
    private String schoolType;
    @TableField("area")
    private String schoolArea;
    @TableField("is_important")
    private String isImportant;
    @TableField("students")
    private String students;
    @TableField("staff")
    private String staffs;
    @TableField("is_have_hospital")
    private String isHaveHospital;
    @TableField("is_disaster_type")
    private String isDisasterType;
    @TableField("have_emergency_plan_type")
    private String haveEmergencyPlanType;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("code")
    private String code;
    @TableField("telephone")
    private String telephone;
    @TableField("lon")
    private Double lon;
    @TableField("lat")
    private Double lat;

}
