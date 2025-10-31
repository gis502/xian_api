package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation("获取全部风险源")
    public AjaxResult getDangerousSourceList(){
        return AjaxResult.success(dangerousSourceService.getDangerousSourceList());
    }

    @GetMapping("/getFireFighter")
    @ApiOperation("获取所有消防队")
    public AjaxResult getFireFighterList(){
        return AjaxResult.success(fireFighterService.getFireFighterList());
    }

    @GetMapping("/getHospital")
    @ApiOperation("获取所有医院")
    public AjaxResult getHospitalList(){
        return AjaxResult.success(hospitalService.getHospitalList());
    }

    @GetMapping("/getEmergencyShelter")
    @ApiOperation("获取所有避难所")
    public AjaxResult getEmergencyShelterList(){
        return AjaxResult.success(emergencyShelterService.getEmergencyShelterList());
    }

    @GetMapping("/getStorePoints")
    @ApiOperation("获取所有物资储备点")
    public AjaxResult getStorePointsList(){
        return AjaxResult.success(storePointsService.getAllStorePointsList());
    }

    @GetMapping("/getSchool")
    @ApiOperation("获取学校")
    public AjaxResult getSchoolList(){
        return AjaxResult.success(schoolService.getSchoolList());
    }

    @GetMapping("/getWater")
    @ApiOperation("获取内涝")
    public AjaxResult getWaterList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByWaterList());
    }

    @GetMapping("/getFlood")
    @ApiOperation("获取山洪")
    public AjaxResult getFloodList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByFloodList());
    }

    @GetMapping("/getReservoir")
    @ApiOperation("获取水库")
    public AjaxResult getReservoirList(){
        return AjaxResult.success(aroundAnalysisService.getAllReservoir());
    }

    @GetMapping("/getBridge")
    @ApiOperation("获取桥梁")
    public AjaxResult getBridgeList(){
        return AjaxResult.success(aroundAnalysisService.getAllBridge());
    }

    @GetMapping("/getSubway")
    @ApiOperation("获取地铁站")
    public AjaxResult getSubwayList(){
        return AjaxResult.success(aroundAnalysisService.getAllSubwayStation());
    }
}
