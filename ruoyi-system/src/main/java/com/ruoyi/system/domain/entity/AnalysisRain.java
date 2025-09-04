package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/*
* 20250902 0328
* 映射的名字一定要一致，不然老麻烦了 */

@Data
@TableName("xian_api_df_platform_ods_xasqxj_sksj_xssk")
public class AnalysisRain {

    @TableField("station_name")
    private String stationName;
    @TableField("vis")
    private String visible;
    @TableField("admin_code_chn")
    private String adminCodeChn;
    @TableField("win_s_max")
    private String winSpeed;
    @TableField("pre_1h")
    private String pre1h;
    @TableField("win_d_s_max")
    private String winSpeedDirection;
    @TableField("win_s_inst_max")
    private String winSpeedMax;
    @TableField("tem")
    private String temperature;
    @TableField("win_d_inst_max")
    private String winSpeedMaxDirection;
    @TableField("station_id_c")
    private String stationId;
    @TableField("rhu")
    private String relativeHumidity;
    @TableField("lon")
    private float lon;
    @TableField("lat")
    private float lat;
    // 添加datetime字段
    @TableField("datetime")
    private String datetime;
}
