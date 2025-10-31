package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterSandstormServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 沙尘暴灾害信息表(xian_disaster_sandstorm)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_sandstorm")
public class XianDisasterSandstormController {
/**
* 服务对象
*/
@Resource
private XianDisasterSandstormServiceImpl xianDisasterSandstormServiceImpl;



}
