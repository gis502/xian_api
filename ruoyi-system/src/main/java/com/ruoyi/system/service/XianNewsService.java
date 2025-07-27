package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.XianNews;
public interface XianNewsService extends IService<XianNews> {


    IPage<XianNews> getFilteredNews(int pageNum, int pageSize, String disasterType, Integer earthquakeDisasterId, Integer rainDisasterId);
}
