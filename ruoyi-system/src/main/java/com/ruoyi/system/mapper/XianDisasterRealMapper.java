package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDisasterReal;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XianDisasterRealMapper extends BaseMapper<XianDisasterReal> {
    List<XianDisasterReal> selectDisasterRealByDisasterId(@Param("disasterId") String disasterId, @Param("disasterTrigger") String disasterTrigger);
    XianDisasterReal selectById(@Param("plot_id") String plotId);
}
