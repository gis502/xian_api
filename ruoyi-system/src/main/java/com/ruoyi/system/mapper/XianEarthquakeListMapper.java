package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianEarthquakeListMapper extends BaseMapper<XianEarthquakeList> {

    /**插入地震基本信息*/
    int insertDisaster(@Param("earthquake") EarthquakeVo earthquake);

    /**插入地震影响街道信息*/
    boolean insertAffect(@Param("eq_id") int eq_id,@Param("village") String village);

    /**插入地震造成损失信息*/
    boolean insertDamage(@Param("eq_id") int eq_id,@Param("pop_injury") int pop_injury,@Param("pop_die") int pop_die);

    /**插入地震造成损失范围信息*/
    boolean insertDamageRange(@Param("eq_id") int eq_id,@Param("affectPopRange") String affectPopRange,@Param("diePopRange") String diePopRange);
}
