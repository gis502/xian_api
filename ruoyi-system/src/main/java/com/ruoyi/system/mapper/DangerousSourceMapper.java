package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.DangerousSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface DangerousSourceMapper extends BaseMapper<DangerousSource> {

    /**
     * 查询椭圆范围内风险源
     * @param longitude 中心点经度
     * @param latitude 中心点纬度
     * @param semiMajorAxis 椭圆长轴
     * @param semiMinorAxis 椭圆短轴
     * @param rotation 椭圆旋转角度
     */
    List<DangerousSource> selectDangerAffectPoints (@Param("longitude") Double longitude,
                                                    @Param("latitude") Double latitude,
                                                    @Param("semiMajorAxis") Double semiMajorAxis,
                                                    @Param("semiMinorAxis") Double semiMinorAxis,
                                                    @Param("rotation") Double rotation);
}
