package com.ruoyi.web.controller.system;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.XianNews;
import com.ruoyi.system.service.XianNewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 多种灾害发生后相关新闻(xian_news)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/xian_news")
public class XianNewsController {
/**
* 服务对象
*/
    @Resource
    private XianNewsService xianNewsService;

    @GetMapping("/list")
    public R getNewsPage(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String disasterType,
            @RequestParam(required = false) Integer earthquakeDisasterId,
            @RequestParam(required = false) Integer rainDisasterId) {
        System.out.println(pageSize+ " " + pageNum);
        System.out.println(disasterType + " " + earthquakeDisasterId + " " + rainDisasterId);

        // 根据灾害类型及对应ID查询新闻
        IPage<XianNews> page = xianNewsService.getFilteredNews(pageNum, pageSize, disasterType, earthquakeDisasterId, rainDisasterId);
        return R.ok(page);
    }




}
