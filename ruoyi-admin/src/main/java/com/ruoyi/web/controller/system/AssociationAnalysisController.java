package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IHistoryDisasterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/association")
public class AssociationAnalysisController {

    @Resource
    private IHistoryDisasterService historyDisasterService;

    @GetMapping("/getHistoryDisaster")
    @ApiOperation("获取历史灾害数据")
    public AjaxResult getHistoryDisasterList(){
        return AjaxResult.success(historyDisasterService.getHistoryDisasterList());
    }
}
