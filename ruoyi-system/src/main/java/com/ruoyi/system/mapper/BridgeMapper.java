package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.Bridge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BridgeMapper extends BaseMapper<Bridge> {
    
    /**
     * 查询与指定面相交或在面内的桥梁数据
     * @param wktPolygon WKT格式的面数据
     * @return 桥梁列表
     */
    List<Bridge> findIntersectingBridges(@Param("wktPolygon") String wktPolygon);
}