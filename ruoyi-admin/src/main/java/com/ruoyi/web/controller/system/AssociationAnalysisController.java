package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IAnalysisRainService;
import com.ruoyi.system.service.IHistoryDisasterService;

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
    public AjaxResult getHistoryDisasterList(@RequestParam String regionName){
        return AjaxResult.success(historyDisasterService.getHistoryDisasterList(regionName.isEmpty()?"长安区" : regionName));
    }

    @GetMapping("/getRainPH")
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
