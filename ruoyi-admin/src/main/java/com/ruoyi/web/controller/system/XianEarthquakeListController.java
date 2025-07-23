package com.ruoyi.web.controller.system;

import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;



@Validated
@RestController
@RequestMapping("/XianEarthquakeList")

public class XianEarthquakeListController {

    @Resource
    private IXianEarthquakeListService xianEarthquakeListService;

    @GetMapping("/getAllEarthquakeList")
    public List<XianEarthquakeList> selectAllEq() {
        return xianEarthquakeListService.selectAllEq();
    }
}
