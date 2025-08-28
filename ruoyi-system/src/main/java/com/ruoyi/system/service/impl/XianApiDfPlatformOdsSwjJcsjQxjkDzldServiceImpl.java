package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.entity.XianApiDfPlatformOdsSwjJcsjQxjkDzld;
import com.ruoyi.system.mapper.XianApiDfPlatformOdsSwjJcsjQxjkDzldMapper;
import com.ruoyi.system.service.IXianApiDfPlatformOdsSwjJcsjQxjkDzldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 气象监控电子雷达Service业务层处理
 * 
 * @author ckw
 * @date 2025-01-25
 */
@Service
public class XianApiDfPlatformOdsSwjJcsjQxjkDzldServiceImpl extends ServiceImpl<XianApiDfPlatformOdsSwjJcsjQxjkDzldMapper, XianApiDfPlatformOdsSwjJcsjQxjkDzld> implements IXianApiDfPlatformOdsSwjJcsjQxjkDzldService {
    
    @Autowired
    private XianApiDfPlatformOdsSwjJcsjQxjkDzldMapper xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper;
    
    /**
     * 查询气象监控电子雷达列表
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 气象监控电子雷达
     */
    @Override
    public List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectXianApiDfPlatformOdsSwjJcsjQxjkDzldList(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
        QueryWrapper<XianApiDfPlatformOdsSwjJcsjQxjkDzld> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotEmpty(xianApiDfPlatformOdsSwjJcsjQxjkDzld.getRadarnum())) {
            queryWrapper.eq("radarnum", xianApiDfPlatformOdsSwjJcsjQxjkDzld.getRadarnum());
        }
        if (StringUtils.isNotEmpty(xianApiDfPlatformOdsSwjJcsjQxjkDzld.getXMax())) {
            queryWrapper.like("x_max", xianApiDfPlatformOdsSwjJcsjQxjkDzld.getXMax());
        }
        if (StringUtils.isNotEmpty(xianApiDfPlatformOdsSwjJcsjQxjkDzld.getXMin())) {
            queryWrapper.like("x_min", xianApiDfPlatformOdsSwjJcsjQxjkDzld.getXMin());
        }
        if (xianApiDfPlatformOdsSwjJcsjQxjkDzld.getObsdate() != null) {
            queryWrapper.eq("obsdate", xianApiDfPlatformOdsSwjJcsjQxjkDzld.getObsdate());
        }
        
        queryWrapper.orderByDesc("obsdate");
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByTimeRange(Date startTime, Date endTime) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.selectByTimeRange(startTime, endTime);
    }
    
    @Override
    public List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByRadarnum(String radarnum) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.selectByRadarnum(radarnum);
    }
    
    @Override
    public List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectLatestData(Integer limit) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.selectLatestData(limit);
    }
    
    /**
     * 新增气象监控电子雷达
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 结果
     */
    @Override
    public int insertXianApiDfPlatformOdsSwjJcsjQxjkDzld(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.insert(xianApiDfPlatformOdsSwjJcsjQxjkDzld);
    }
    
    /**
     * 修改气象监控电子雷达
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 结果
     */
    @Override
    public int updateXianApiDfPlatformOdsSwjJcsjQxjkDzld(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.updateById(xianApiDfPlatformOdsSwjJcsjQxjkDzld);
    }
    
    /**
     * 批量删除气象监控电子雷达
     * 
     * @param ids 需要删除的气象监控电子雷达主键
     * @return 结果
     */
    @Override
    public int deleteXianApiDfPlatformOdsSwjJcsjQxjkDzldByIds(String[] ids) {
        return xianApiDfPlatformOdsSwjJcsjQxjkDzldMapper.deleteBatchIds(Arrays.asList(ids));
    }
}