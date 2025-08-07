package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_highway") // 请根据实际表名修改
public class Highway {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("feature_id")
    private Integer featureId;
    
    @TableField("geom")
    private String geom; // 几何信息
    
    @TableField("OBJECTID_1")
    private String objectId1;
    
    @TableField("OBJECTID")
    private String objectId;
    
    @TableField("NAME")
    private String name; // 名称
    
    @TableField("Shape_Leng")
    private String shapeLeng; // 形状长度
    
    @TableField("Shape_Length")
    private String shapeLength; // 形状长度

    private String geomGeom;

}