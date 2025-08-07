package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_water_pipe")
public class WaterPipe {

    @TableId
    private Integer id;
    
    @TableField("feature_id")
    private Integer featureId;
    
    @TableField("geom")
    private String geom;
    
    @TableField("OBJECTID_1")
    private String objectId1;
    
    @TableField("OBJECTID")
    private String objectId;
    
    @TableField("NAME")
    private String name;
    
    @TableField("Shape_Leng")
    private String shapeLength;
    
    @TableField("Shape_Length")
    private String shapeLengthDetail;
    
    @TableField("FID")
    private String fid;
    
    @TableField("bh")
    private String bh;
    
    @TableField("kzsfld")
    private String kzsfld;
    
    @TableField("xkzsfld")
    private String xkzsfld;
    
    @TableField("sfkzdb")
    private String sfkzdb;
    
    @TableField("sfczbldz")
    private String sfczbldz;
    
    @TableField("fxpc_xzqh1")
    private String fxpcXzqh1;
    
    @TableField("fxpc_xzqh2")
    private String fxpcXzqh2;
    
    @TableField("fxpc_xzqh3")
    private String fxpcXzqh3;
    
    @TableField("fxpc_updat")
    private String fxpcUpdat;
    
    @TableField("fxpc_creat")
    private String fxpcCreat;
    
    @TableField("fxpc_sjzt_")
    private String fxpcSjzt;
    
    @TableField("fxpc_shlcz")
    private String fxpcShlcz;
    
    @TableField("fxpc_shzt_")
    private String fxpcShzt;
    
    @TableField("fxpc_sshy_")
    private String fxpcSshy;
    
    @TableField("fxpc_rqfp_")
    private String fxpcRqfp;
    
    @TableField("fxpc_pch_s")
    private String fxpcPchS;
    
    @TableField("fxpc_dcdxb")
    private String fxpcDcdxb;
    
    @TableField("fxpc_datai")
    private String fxpcDatai;
}