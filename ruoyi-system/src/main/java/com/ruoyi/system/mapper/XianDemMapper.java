package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDem;
import org.apache.ibatis.annotations.Param;

public interface XianDemMapper extends BaseMapper<XianDem>{
    /**
     * 查找包含指定经纬度点的网格
     */
    XianDem findGridContainingPoint(
            @Param("lon") Double lon,
            @Param("lat") Double lat);

    /**
     * 查找3x3范围内高程最低的网格
     */
    XianDem findMinElevationGridIn3x3(
            @Param("centerLon") Double centerLon,
            @Param("centerLat") Double centerLat,
            @Param("distance") Double distance);

    /**
     * 获取网格中心点的经度
     */
    default Double getCenterLon(XianDem grid) {
        // 实际项目中可通过PostGIS函数计算，或在查询时直接返回
        // 这里简化处理，实际应从geom字段解析
        return null;
    }

    /**
     * 获取网格中心点的纬度
     */
    default Double getCenterLat(XianDem grid) {
        // 实际项目中可通过PostGIS函数计算，或在查询时直接返回
        return null;
    }
}
