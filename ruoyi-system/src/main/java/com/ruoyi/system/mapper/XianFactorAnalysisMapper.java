package com.ruoyi.system.mapper;

import com.ruoyi.common.enums.DisasterType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 13129
 * @description: TODO()
 * @date 2025/8/8 下午3:25
 */
@Mapper
public interface XianFactorAnalysisMapper {
    List<Map<String, Object>> queryDisasterEstimation(
            @Param("disasterId")Long disasterId,
            @Param("disasterType")DisasterType disasterType);

    List<Map<String, Object>> queryDisasterEstimationGetAll(
            @Param("disasterId")Long disasterId,
            @Param("disasterType")DisasterType disasterType);
}
