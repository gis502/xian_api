package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_reservoir_list")
public class AnalysisReservoir {

    @TableId
    private Integer id;

    @TableField("gid")
    private Integer gid;

    @TableField("adcd")
    private String adcd;

    @TableField("name")
    private String name;

    @TableField("location")
    private String location;

    @TableField("safety")
    private Integer safety;

    @TableField("safetytime")
    private String safetyTime;

    @TableField("safetyr")
    private String safetyR;

    @TableField("reinforce")
    private String reinforce;

    @TableField("rscd")
    private String rscd;

    @TableField("jhfr")
    private Integer jhfr;

    @TableField("gcgm")
    private Integer gcgm;

    @TableField("is_dangerous")
    private String isDangerous;

    @TableField("note1")
    private Integer note1;

    @TableField("lon")
    private Double longitude;

    @TableField("lat")
    private Double latitude;

    @TableField("point")
    private String point;

}