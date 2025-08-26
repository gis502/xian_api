package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterColdDamageServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 低温冻害信息表(xian_disaster_cold_damage)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/xian_disaster_cold_damage")
public class XianDisasterColdDamageController {
/**
* 服务对象
*/
@Resource
private XianDisasterColdDamageServiceImpl xianDisasterColdDamageServiceImpl;



}
