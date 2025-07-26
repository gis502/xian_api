package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.mapper.XianNewsMapper;
import com.ruoyi.system.domain.XianNews;
import com.ruoyi.system.service.XianNewsService;
@Service
public class XianNewsServiceImpl extends ServiceImpl<XianNewsMapper, XianNews> implements XianNewsService{

    @Override
    public IPage<XianNews> getFilteredNews(int pageNum, int pageSize, String disasterType, Integer earthquakeDisasterId, Integer rainDisasterId) {
        Page<XianNews> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<XianNews> queryWrapper = new LambdaQueryWrapper<>();

        if ("rain".equals(disasterType) && rainDisasterId != null) {
            queryWrapper.eq(XianNews::getRainId, rainDisasterId);
        } else if ("earthquake".equals(disasterType) && earthquakeDisasterId != null) {
            queryWrapper.eq(XianNews::getEarthquakeId, earthquakeDisasterId);
        }

        return this.page(page, queryWrapper);
    }

}
