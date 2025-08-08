package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.Crops;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CropsMapper extends BaseMapper<Crops> {
    
    /**
     * 查询与指定面相交或在面内的农作物数据
     * @param wktPolygon WKT格式的面数据
     * @return 农作物列表
     */
    List<Crops> findIntersectingCrops(@Param("wktPolygon") String wktPolygon);
}