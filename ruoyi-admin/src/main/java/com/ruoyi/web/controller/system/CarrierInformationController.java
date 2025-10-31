package com.ruoyi.web.controller.system;

import com.ruoyi.system.service.CarrierInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author wzy
 * @description: TODO()
 * @date 2025/9/6 下午4:52
 */
@RestController
@RequestMapping("/admins/carrier_information")
public class CarrierInformationController {

    private final CarrierInformationService carrierInformationService;

    public CarrierInformationController(CarrierInformationService carrierInformationService) {
        this.carrierInformationService = carrierInformationService;
    }

    @GetMapping("/disaster_names")
    public ResponseEntity<?> disasterNames(){
        return ResponseEntity.ok(carrierInformationService.queryDisasterNames());
    }

    @PostMapping("/people")
    public ResponseEntity<?> people(@RequestParam Long disasterId) {
        return ResponseEntity.ok(carrierInformationService.queryPeople(disasterId));
    }

    @PostMapping("/traffic")
    public ResponseEntity<?> traffic(@RequestParam Long disasterId) {
        return ResponseEntity.ok(carrierInformationService.queryTraffic(disasterId));
    }

    @PostMapping("/danger")
    public ResponseEntity<?> danger(@RequestParam Long disasterId) {
        return ResponseEntity.ok(carrierInformationService.queryDanger(disasterId));
    }

    @PostMapping("/station")
    public ResponseEntity<?> station(@RequestParam Long disasterId) {
        return ResponseEntity.ok(carrierInformationService.queryStation(disasterId));
    }

    @PostMapping("/table_info")
    public ResponseEntity<?> tableInfo(@RequestParam Long disasterId) {
        return ResponseEntity.ok(carrierInformationService.tableInfo(disasterId));
    }
}