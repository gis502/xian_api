package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.People;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PeopleMapper extends BaseMapper<People> {
    
    /**
     * 查询与指定面相交或在面内的人口数据
     * @param wktPolygon WKT格式的面数据
     * @return 人口列表
     */
    List<People> findIntersectingPeople(@Param("wktPolygon") String wktPolygon);
}