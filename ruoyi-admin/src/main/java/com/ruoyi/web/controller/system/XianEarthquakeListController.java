package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



@Validated
@RestController
@RequestMapping("/XianEarthquakeList")

public class XianEarthquakeListController {

    private static final Logger log = LoggerFactory.getLogger(XianEarthquakeListController.class);
    @Resource
    private IXianEarthquakeListService eqListService;

    @GetMapping("/getAllEarthquakeList")
    public AjaxResult selectAllEq() {
        return AjaxResult.success(eqListService.selectAllEq()) ;
    }
    @PostMapping("/getEarthquakeEventById")
    public AjaxResult getEarthquakeEventById(@RequestParam(value = "id") Long Id) {
        return AjaxResult.success(eqListService.getEarthquakeEventById(Id));
    }

    @PostMapping("/disaster/add")
    public AjaxResult disasterAdd(@RequestBody EarthquakeVo earthquake) {
        return AjaxResult.success(eqListService.insertDisaster(earthquake));
    }

    @PostMapping("/earthquake/add")
    public AjaxResult earthquakeAdd(@RequestBody EarthquakeVo earthquake) {
        System.out.println(earthquake);
        log.info("87474155654656546464665 {}", earthquake);
        return AjaxResult.success(eqListService.insertEarthquake(earthquake));
    }

    @PostMapping("/allAffectPoints/get")
    public AjaxResult getAllAffectPoints(@RequestBody EarthquakeVo earthquake) {
        return AjaxResult.success(eqListService.selectAffectPoints(earthquake));
    }
}
