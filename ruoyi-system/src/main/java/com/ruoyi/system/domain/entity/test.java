package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_test")
public class test {

    @TableId("id")
    private Integer id;

    @TableField("name")
    private String riskName;

    @TableField("lon")
    private Double lon;

    @TableField("lat")
    private Double lat;

}