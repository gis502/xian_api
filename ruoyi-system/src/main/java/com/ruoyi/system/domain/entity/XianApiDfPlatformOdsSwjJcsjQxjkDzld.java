package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import lombok.Data;

/**
 * 气象监控电子雷达实体类
 * 
 * @author ckw
 * @date 2025.8.25
 */
@Data
@TableName("xian_api_df_platform_ods_swj_jcsj_qxjk_dzld")
public class XianApiDfPlatformOdsSwjJcsjQxjkDzld {
    
    /** X轴最大坐标 */
    @TableField("x_max")
    private String xMax;
    
    /** X轴最小坐标 */
    @TableField("x_min")
    private String xMin;
    
    /** Y轴最大坐标 */
    @TableField("y_max")
    private String yMax;
    
    /** Y轴最小坐标 */
    @TableField("y_min")
    private String yMin;
    
    /** 雷达回波 */
    @TableField("brhref")
    private String brhref;
    
    /** 时间 */
    @TableField("obsdate")
    private Date obsdate;
    
    /** 瞬时降雨 */
    @TableField("r0href")
    private String r0href;
    
    /** 一小时预报 */
    @TableField("r1href")
    private String r1href;
    
    /** radarnum */
    @TableField("radarnum")
    private String radarnum;
}