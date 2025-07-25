package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.EarthquakeRainDTO;
import com.ruoyi.system.service.EarthquakeRainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/earthquake-rain")
public class EarthquakeRainController {

    @Resource
    private EarthquakeRainService earthquakeRainService;

    @GetMapping("/list")
    public AjaxResult list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        List<EarthquakeRainDTO> list = earthquakeRainService.getPagedList(pageNum, pageSize);
        int total = earthquakeRainService.getTotalCount();

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("records", list);

        return AjaxResult.success(result);
    }
}
