package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("history_disaster")
public class HistoryDisaster {


    @TableId
    private Integer id;
    @TableField("regional_name")
    private String name;
    @TableField("disaster_events")
    private String disasterEvent;
    @TableField("types_disasters")
    private String disasterType;
    @TableField("disaster_population")
    private String disasterPopulation;
    @TableField("missing_persons")
    private String missingPersons;
    @TableField("direct_economic_losses")
    private String economicLosses;
    @TableField("number_of_households_with_collapsed_houses")
    private String collapsedHouses;


}
