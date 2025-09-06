package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.swing.*;
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

    /**
     * 查询椭圆范围内风险源
     * @param longitude 中心点经度
     * @param latitude 中心点纬度
     * @param semiMajorAxis 椭圆长轴
     * @param semiMinorAxis 椭圆短轴
     * @param rotation 椭圆旋转角度
     */
    List<GeologicalDisasterHide> selectDisasterHideAffectPoints(@Param("longitude") Double longitude,
                                                                @Param("latitude") Double latitude,
                                                                @Param("semiMajorAxis") Double semiMajorAxis,
                                                                @Param("semiMinorAxis") Double semiMinorAxis,
                                                                @Param("rotation") Double rotation);

    @Select("SELECT count(*) FROM xian_disaster_risk where county = #{county}")
    Integer getRiskNumByCounty(String county);
}
