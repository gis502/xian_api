package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 17:01
 * @description: 山洪危险区
 */

@Data
@TableName("mountain_torrent_disaster")
public class MountainTorrentDisaster {

    @TableId
    private String id;
    @TableField("county")
    private String county;
    @TableField("disaster_name")
    private String disasterName;
    @TableField("street_name")
    private String streetName;
    @TableField("village_name")
    private String villageName;
    @TableField("risk_level")
    private String riskLevel;
    @TableField("risk_people")
    private String riskPeople;
    @TableField("household")
    private String household;
    @TableField("house")
    private String house;
    @TableField("admin_name")
    private String adminName;
    @TableField("admin_position")
    private String adminPosition;
    @TableField("admin_tele")
    private String adminTele;
    @TableField("monitor_name")
    private String monitorName;
    @TableField("monitor_position")
    private String monitorPosition;
    @TableField("monitor_tele")
    private String monitorTele;
    @TableField("transfer_name")
    private String transferName;
    @TableField("transfer_position")
    private String transferPosition;
    @TableField("transfer_tele")
    private String transferTele;
    @TableField("reset_name")
    private String resetName;
    @TableField("reset_position")
    private String resetPosition;
    @TableField("reset_tele")
    private String resetTele;


}
