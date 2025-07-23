package com.ruoyi.web.controller.system;

import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 19:15
 * @description: 风险区控制类
 */


@RestController
public class XianEarthquakeListController {

    @Resource
    private IXianEarthquakeListService xianEarthquakeListService;

}
