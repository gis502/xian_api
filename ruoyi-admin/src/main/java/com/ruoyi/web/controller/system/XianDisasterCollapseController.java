package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterCollapseServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 崩塌灾害信息表(xian_disaster_collapse)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_collapse")
public class XianDisasterCollapseController {
/**
* 服务对象
*/
@Resource
private XianDisasterCollapseServiceImpl xianDisasterCollapseServiceImpl;



}
