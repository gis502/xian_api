package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XianDisasterRainMapper extends BaseMapper<XianDisasterRain> {
    List<XianDisasterRain> selectList();
}
