package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IAnalysisRainService;
import com.ruoyi.system.service.IHistoryDisasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admins/association")
public class AssociationAnalysisController {

    @Resource
    private IHistoryDisasterService historyDisasterService;

    @Resource
    private IAnalysisRainService analysisRainService;

    @GetMapping("/getHistoryDisaster")
    @ApiOperation("获取历史灾害数据")
    public AjaxResult getHistoryDisasterList(@RequestParam String regionName){
        return AjaxResult.success(historyDisasterService.getHistoryDisasterList(regionName.isEmpty()?"长安区" : regionName));
    }

    @GetMapping("/getRainPH")
    @ApiOperation("获取每个区降雨量")
    public AjaxResult getRainPHList(){
        return AjaxResult.success(analysisRainService.getRainPreHours());
    }

    @PostMapping("/getRainAdmin")
    public AjaxResult getRainAdminList(@RequestParam int adminCode){
        try{
            return AjaxResult.success(analysisRainService.getAdminCodeRain(adminCode));
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }
}
