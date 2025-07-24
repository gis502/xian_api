package com.ruoyi.system.service.impl;

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
    public IPage<XianNews> getPageList(int pageNum, int pageSize) {
        return this.page(new Page<>(pageNum, pageSize));
    }
}
