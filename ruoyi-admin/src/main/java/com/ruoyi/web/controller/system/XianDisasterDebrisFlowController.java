package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterDebrisFlowServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 泥石流灾害信息表(xian_disaster_debris_flow)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/xian_disaster_debris_flow")
public class XianDisasterDebrisFlowController {
/**
* 服务对象
*/
@Resource
private XianDisasterDebrisFlowServiceImpl xianDisasterDebrisFlowServiceImpl;



}
