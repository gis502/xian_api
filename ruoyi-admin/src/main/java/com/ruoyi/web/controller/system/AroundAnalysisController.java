package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admins/around")
public class AroundAnalysisController {

    @Resource
    private IDangerousSourceService dangerousSourceService;
    @Resource
    private IFireFighterService fireFighterService;
    @Resource
    private IHospitalService hospitalService;
    @Resource
    private IEmergencyShelterService emergencyShelterService;
    @Resource
    private IStorePointsService storePointsService;
    @Resource
    private ISchoolService schoolService;
    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;
    @Resource
    private IAroundAnalysisService aroundAnalysisService;

    @GetMapping("/getDangerousSource")

    public AjaxResult getDangerousSourceList(){
        return AjaxResult.success(dangerousSourceService.getDangerousSourceList());
    }

    @GetMapping("/getFireFighter")

    public AjaxResult getFireFighterList(){
        return AjaxResult.success(fireFighterService.getFireFighterList());
    }

    @GetMapping("/getHospital")

    public AjaxResult getHospitalList(){
        return AjaxResult.success(hospitalService.getHospitalList());
    }

    @GetMapping("/getEmergencyShelter")

    public AjaxResult getEmergencyShelterList(){
        return AjaxResult.success(emergencyShelterService.getEmergencyShelterList());
    }

    @GetMapping("/getStorePoints")

    public AjaxResult getStorePointsList(){
        return AjaxResult.success(storePointsService.getAllStorePointsList());
    }

    @GetMapping("/getSchool")

    public AjaxResult getSchoolList(){
        return AjaxResult.success(schoolService.getSchoolList());
    }

    @GetMapping("/getWater")

    public AjaxResult getWaterList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByWaterList());
    }

    @GetMapping("/getFlood")

    public AjaxResult getFloodList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByFloodList());
    }

    @GetMapping("/getReservoir")

    public AjaxResult getReservoirList(){
        return AjaxResult.success(aroundAnalysisService.getAllReservoir());
    }

    @GetMapping("/getBridge")

    public AjaxResult getBridgeList(){
        return AjaxResult.success(aroundAnalysisService.getAllBridge());
    }

    @GetMapping("/getSubway")

    public AjaxResult getSubwayList(){
        return AjaxResult.success(aroundAnalysisService.getAllSubwayStation());
    }
}
