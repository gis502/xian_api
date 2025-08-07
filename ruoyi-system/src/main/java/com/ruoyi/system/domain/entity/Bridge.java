package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_bridge_points") 
public class Bridge {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("region")
    private String region; // 区域
    
    @TableField("bridge_name")
    private String bridgeName; // 名称
    
    @TableField("lon")
    private Double lon; // 经度
    
    @TableField("lat")
    private Double lat; // 纬度
    
    @TableField("location")
    private String location; // 位置
    
    @TableField("build_time")
    private String buildTime; // 建成时间
    
    @TableField("bridge_type")
    private String bridgeType; // 类型
    
    @TableField("maintain_type")
    private String maintainType; // 养护类型
    
    @TableField("tech_type")
    private String techType; // 技术类型
    
    @TableField("scale")
    private String scale; // 规模
    
    @TableField("area")
    private Double area; // 面积
    
    @TableField("master")
    private String master; // 所属单位
    
    @TableField("maint")
    private String maint; // 养护单位
    
    @TableField("note")
    private String note; // 备注
    
    @TableField("point")
    private String point; // 位置（几何类型）
}