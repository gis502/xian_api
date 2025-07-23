package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XianEarthquakeListMapper extends BaseMapper<XianEarthquakeList> {
    List<XianEarthquakeList> selectList();
}
