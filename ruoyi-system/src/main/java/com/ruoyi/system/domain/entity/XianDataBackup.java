package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zzw
 * @description: 数据库操作记录
 * @date 2025/11/8 下午5:08
 */
@Data
@TableName("xian_data_backup")
public class XianDataBackup {
    @TableId
    private Integer id;
    @TableField("data")
    private String data;
    @TableField("type")
    private String type;
    @TableField("time")
    private LocalDateTime time;
    @TableField("ip")
    private String ip;
}
