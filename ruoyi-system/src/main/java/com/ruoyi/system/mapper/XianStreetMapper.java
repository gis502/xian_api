package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianStreet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface XianStreetMapper extends BaseMapper<XianStreet> {


    @Select("SELECT st.street "+
            "FROM xian_street st "+
            "WHERE ST_Contains("+
                "st.geometry_wkt,"+
                "ST_SetSRID(ST_MakePoint(#{lon}, #{lat}), 4490)"+
            ")")
    List<String> inStreet(@Param("lat") float lat, @Param("lon") float lon);
}
