package com.ruoyi.web.controller.system;

import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



@Validated
@RestController
@RequestMapping("/XianEarthquakeList")

public class XianEarthquakeListController {

    @Resource
    private XianEarthquakeListMapper xianEarthquakeListMapper;

    @GetMapping("/getAllEarthquakeList")
    public List<XianEarthquakeList> selectAllEq() {
        return xianEarthquakeListMapper.selectAllEq();
    }
    @PostMapping("/getEarthquakeEventById")
    public XianEarthquakeList getEarthquakeEventById(@RequestParam(value = "id") String id) {
        System.out.println(id+"getEarthquakeEventById id");
        System.out.println(xianEarthquakeListMapper.getEarthquakeEventById(id)+"xianEarthquakeListMapper.getEarthquakeEventById(id)");
        return xianEarthquakeListMapper.getEarthquakeEventById(id);
    }

}
