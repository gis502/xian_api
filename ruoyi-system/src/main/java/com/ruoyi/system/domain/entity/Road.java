package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_road")
public class Road {

    @TableId
    private Integer id;

    @TableField("road_name")
    private String roadName;

    @TableField("qdmc")
    private String qdmc;

    @TableField("zdmc")
    private String zdmc;

    @TableField("qctz")
    private String qctz;

    @TableField("jzsfld")
    private String jzsfld;

    @TableField("mqsfld")
    private String mqsfld;

    @TableField("dlzc")
    private String dlzc;

    @TableField("sfkzdb")
    private String sfkzdb;

    @TableField("fxpc_sjzt")
    private String fxpcSjzt;

    @TableField("fxpc_shlcz")
    private String fxpcShlcz;

    @TableField("fxpc_shzt")
    private String fxpcShzt;

    @TableField("fxpc_sshy_")
    private String fxpcSshy;

    @TableField("fxpc_datai")
    private String fxpcDatai;

    @TableField("geom")
    private String geom;

    private String pointGeom;

}
