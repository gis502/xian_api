package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 16:07
 * @description: 致灾因子分析表
 */


@Data
@TableName("xian_factor_analysis")
public class FactorAnalysis {

    @TableField("analysis_id")
    private Integer analysisId;
    @TableField("attribute_id")
    private Integer attributeId;
    @TableField("value_id")
    private Integer valueId;
    @TableField("factor_value")
    private String factorValue;
    @TableField("probability")
    private Double probability;
    @TableField("predicted_occur")
    private Integer predictedOccur;
    @TableField("level")
    private String level;
    @TableField("actual_occur")
    private Integer actualOccur;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    @TableField("is_deleted")
    private Integer isDeleted;

}
