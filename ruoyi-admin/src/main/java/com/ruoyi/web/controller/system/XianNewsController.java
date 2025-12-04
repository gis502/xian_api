package com.ruoyi.web.controller.system;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.XianNews;
import com.ruoyi.system.service.XianNewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多种灾害发生后相关新闻(xian_news)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/admins/xian_news")
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
            @RequestParam(required = false) String lastItem) {

        String disasterType = null;
        Integer disasterId = null;

        if (lastItem != null && !lastItem.isEmpty()) {
            try {
                JSONObject lastItemObj = JSONObject.parseObject(lastItem);
                disasterType = lastItemObj.getString("disasterType");

                // 直接从 JSON 取对应 ID
                if (disasterType != null) {
                    disasterId = lastItemObj.getInteger(getDisasterFieldName(disasterType));
                }
            } catch (Exception e) {
                return R.fail("参数解析错误: " + e.getMessage());
            }
        }

        IPage<XianNews> page = xianNewsService.getFilteredNews(pageNum, pageSize, disasterType, disasterId);
        return R.ok(page);
    }

    /**
     * 根据 disasterType 获取对应字段名
     */
    private String getDisasterFieldName(String disasterType) {
        Map<String, String> disasterIdMap = new HashMap<>();
        disasterIdMap.put("earthquake", "earthquakeDisasterId");
        disasterIdMap.put("rain", "rainDisasterId");
        disasterIdMap.put("snow", "snowDisasterId");
        disasterIdMap.put("coldDamage", "coldDamageDisasterId");
        disasterIdMap.put("collapse", "collapseDisasterId");
        disasterIdMap.put("landslide", "landslideDisasterId");
        disasterIdMap.put("debrisFlow", "debrisFlowDisasterId");
        disasterIdMap.put("galeHail", "galeHailDisasterId");
        disasterIdMap.put("sandstorm", "sandstormDisasterId");
        disasterIdMap.put("drought", "droughtDisasterId");
        disasterIdMap.put("heatwave", "heatwaveDisasterId");
        disasterIdMap.put("wildfire", "wildfireDisasterId");
        disasterIdMap.put("bioDisaster", "bioDisasterId");
        disasterIdMap.put("safetyAccident", "safetyAccidentDisasterId");
        return disasterIdMap.getOrDefault(disasterType, "");
    }

}

