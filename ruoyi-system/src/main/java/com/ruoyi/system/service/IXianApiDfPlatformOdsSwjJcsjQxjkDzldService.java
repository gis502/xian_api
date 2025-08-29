package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.entity.XianApiDfPlatformOdsSwjJcsjQxjkDzld;

import java.util.Date;
import java.util.List;

/**
 * 气象监控电子雷达Service接口
 * 
 * @author ckw
 * @date 2025-01-25
 */
public interface IXianApiDfPlatformOdsSwjJcsjQxjkDzldService extends IService<XianApiDfPlatformOdsSwjJcsjQxjkDzld> {
    
    /**
     * 查询气象监控电子雷达列表
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 气象监控电子雷达集合
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectXianApiDfPlatformOdsSwjJcsjQxjkDzldList(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld);
    
    /**
     * 根据时间范围查询雷达数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByTimeRange(Date startTime, Date endTime);
    
    /**
     * 根据雷达编号查询数据
     * 
     * @param radarnum 雷达编号
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByRadarnum(String radarnum);
    
    /**
     * 获取最新的雷达数据
     * 
     * @param limit 限制条数
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectLatestData(Integer limit);
    
    /**
     * 新增气象监控电子雷达
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 结果
     */
    int insertXianApiDfPlatformOdsSwjJcsjQxjkDzld(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld);
    
    /**
     * 修改气象监控电子雷达
     * 
     * @param xianApiDfPlatformOdsSwjJcsjQxjkDzld 气象监控电子雷达
     * @return 结果
     */
    int updateXianApiDfPlatformOdsSwjJcsjQxjkDzld(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld);
    
    /**
     * 批量删除气象监控电子雷达
     * 
     * @param ids 需要删除的气象监控电子雷达主键集合
     * @return 结果
     */
    int deleteXianApiDfPlatformOdsSwjJcsjQxjkDzldByIds(String[] ids);
}