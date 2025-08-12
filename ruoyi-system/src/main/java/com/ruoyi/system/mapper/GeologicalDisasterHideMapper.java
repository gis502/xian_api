package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 17:59
 * @description:
 */

@Mapper
public interface GeologicalDisasterHideMapper extends BaseMapper<GeologicalDisasterHide> {
    
    /**
     * 查询圆形范围内的危险源数据
     * @param centerLon 中心点经度
     * @param centerLat 中心点纬度
     * @param radiusMeters 半径（米）
     * @return 危险源列表
     */
    List<GeologicalDisasterHide> findWithinCircle(@Param("centerLon") Double centerLon, 
                                                   @Param("centerLat") Double centerLat, 
                                                   @Param("radiusMeters") Double radiusMeters);
}
