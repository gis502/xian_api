package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_emergency_shelter")
public class EmergencyShelter {

    @TableId
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("year")
    private String year;
    @TableField("address")
    private String address;
    @TableField("type")
    private String type;
    @TableField("district")
    private String district;
    @TableField("construction_category")
    private String constructionCategory;
    @TableField("cover_area")
    private String coverArea;
    @TableField("effective_refuge_area")
    private String effectiveRefugeArea;
    @TableField("effective_number_of_refugees")
    private String effectiveNumberOfRefugees;
    @TableField("lon")
    private Double longitude;
    @TableField("lat")
    private Double latitude;
}