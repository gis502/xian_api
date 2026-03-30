package com.ruoyi.system.mapper;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.RainAssessmentOutput;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 暴雨评估产出物 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-03-29
 */
@Mapper
@DataSource(DataSourceType.SLAVE)
public interface RainAssessmentOutputMybatisMapper {

    /**
     * 插入暴雨评估产出物记录
     *
     * @param output 暴雨评估产出物实体
     * @return 影响行数
     */
    int insertRainAssessmentOutput(@Param("output") RainAssessmentOutput output);
}
