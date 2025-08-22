package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



@Validated
@RestController
@RequestMapping("/XianEarthquakeList")

public class XianEarthquakeListController {

    @Resource
    private IXianEarthquakeListService eqListService;

    @GetMapping("/getAllEarthquakeList")
    public AjaxResult selectAllEq() {

        System.out.println(eqListService.selectAllEq()+"eqListService.selectAllEq()");
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


}
