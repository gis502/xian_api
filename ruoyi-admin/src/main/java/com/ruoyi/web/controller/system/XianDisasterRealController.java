package com.ruoyi.web.controller.system;


import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianDisasterReal;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianDisasterRealMapper;
import com.ruoyi.system.service.IXianDisasterRealService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/XianDisasterReal")

public class XianDisasterRealController {

    @Resource
    private XianDisasterRealMapper xianDisasterRealMapper;
    @Resource
    private IXianDisasterRealService XianDisasterRealService;


    @PostMapping("/selectDisasterRealByDisasterId")
    public List<XianDisasterReal> selectDisasterRealByDisasterId(@RequestParam(value = "disasterId") String disasterId ,@RequestParam(value = "disasterTrigger") String disasterTrigger) {
        return xianDisasterRealMapper.selectDisasterRealByDisasterId(disasterId,disasterTrigger);
    }
    /**
     * @description: "根据plotIds和plotTypes获取指定地震所有标绘点信息"
     * @author: NTY
     * @time: 2024/10/21
     **/
    @PostMapping("/getExcelPlotInfo")
    public Object getExcelPlotInfo(@RequestBody Map<String, List<String>> params) {
        List<String> plotIds = params.get("plotIds");
        List<String> plotTypes = params.get("plotTypes");

        try {
            if (plotIds.size() != plotTypes.size()) {
                return "plotIds and plotTypes must have the same length.";
            }

            // 查询多条标绘数据
            List<Object> ExcelPlotInfoList = XianDisasterRealService.getExcelPlotInfo(plotTypes, plotIds);
            System.out.println("数据: " + ExcelPlotInfoList);
            return !ExcelPlotInfoList.isEmpty() ? ExcelPlotInfoList : Collections.emptyList();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while querying details: " + e.getMessage();
        }
    }

    /**
     * @description: "根据plotId和plotType获取标绘点信息"
     * @author: SWB
     * @time: 2024/9/30 23:16
     **/
    @GetMapping("/getplotinfo")
    public Object getPlotDetails(@RequestParam String plotId, @RequestParam String plotType) {
        try {
            Object details = XianDisasterRealService.getPlotInfos(plotType, plotId);
            if (details != null) {
                return details;
            } else {
                return "No details found for the given plotId and plotType";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while querying details: " + e.getMessage();
        }
    }

}
