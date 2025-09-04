package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.mapper.XianNewsMapper;
import com.ruoyi.system.domain.XianNews;
import com.ruoyi.system.service.XianNewsService;

import java.util.HashMap;
import java.util.Map;

@Service
public class XianNewsServiceImpl extends ServiceImpl<XianNewsMapper, XianNews> implements XianNewsService{

    @Override
    public IPage<XianNews> getFilteredNews(int pageNum, int pageSize, String disasterType, Integer disasterId) {
        Page<XianNews> page = new Page<>(pageNum, pageSize);
        QueryWrapper<XianNews> queryWrapper = new QueryWrapper<>();

        if (disasterId != null && disasterType != null) {
            String columnName = getColumnByType(disasterType);
            if (StringUtils.isNotBlank(columnName)) {
                queryWrapper.eq(columnName, disasterId);
            }
        }

        return this.page(page, queryWrapper);
    }

    private String getColumnByType(String disasterType) {
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("earthquake", "earthquake_id");
        columnMap.put("rain", "rain_id");
        columnMap.put("snow", "snow_id");
        columnMap.put("coldDamage", "cold_damage_id");
        columnMap.put("collapse", "collapse_id");
        columnMap.put("landslide", "landslide_id");
        columnMap.put("debrisFlow", "debris_flow_id");
        columnMap.put("galeHail", "gale_hail_id");
        columnMap.put("sandstorm", "sandstorm_id");
        columnMap.put("drought", "drought_id");
        columnMap.put("heatwave", "heatwave_id");
        columnMap.put("wildfire", "wildfire_id");
        columnMap.put("bioDisaster", "bio_disaster_id");
        columnMap.put("safetyAccident", "safety_accident_id");
        return columnMap.get(disasterType);
    }


}
