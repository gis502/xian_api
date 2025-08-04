package com.ruoyi.web.controller.system;

import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.DownloadreportService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Validated
@RestController
@RequestMapping("/downloadreport")
public class Downloadreport {
    @Resource
    private DownloadreportService downloadreportService;
    @GetMapping("/downloadrainreport")
    public void downloadrainreport() {
        downloadreportService.downloadrainreport();
    }
}
