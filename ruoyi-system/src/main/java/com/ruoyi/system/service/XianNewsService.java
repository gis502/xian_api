package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.XianNews;
public interface XianNewsService extends IService<XianNews> {

    IPage<XianNews> getPageList(int pageNum, int pageSize);
}
