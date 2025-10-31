package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterSafetyAccidentServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 安全生产类事故灾难信息表(xian_disaster_safety_accident)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_safety_accident")
public class XianDisasterSafetyAccidentController {
/**
* 服务对象
*/
@Resource
private XianDisasterSafetyAccidentServiceImpl xianDisasterSafetyAccidentServiceImpl;



}
