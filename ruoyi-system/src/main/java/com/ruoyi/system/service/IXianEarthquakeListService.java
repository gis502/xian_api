package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.entity.XianEarthquakeList;

import java.util.List;

public interface IXianEarthquakeListService {
    List<XianEarthquakeList> selectAllEq();

}
