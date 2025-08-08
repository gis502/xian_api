package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_dem")
public class XianDem {
    @TableId(type = IdType.AUTO)
    private Integer gid;

    private Double id;

    // 高程值存储在gridcode字段
    private Double gridcode;

    // 存储网格的几何范围
    @TableField(value = "geom")
    private String geom; // 存储WKT格式的几何信息，或使用空间类型

    // 非数据库字段，用于存储计算出的中心点坐标
    @TableField(exist = false)
    private Double centerLon;

    @TableField(exist = false)
    private Double centerLat;

    // 获取高程值（转为整数）
    public Integer getElevation() {
        return gridcode != null ? gridcode.intValue() : null;
    }
    
    // 添加构造函数
    public XianDem(Double centerLat, Double centerLon, Integer elevation) {
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.gridcode = elevation != null ? elevation.doubleValue() : null;
    }
}
