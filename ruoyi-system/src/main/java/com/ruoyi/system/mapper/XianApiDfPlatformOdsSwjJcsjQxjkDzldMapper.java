package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianApiDfPlatformOdsSwjJcsjQxjkDzld;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 气象监控电子雷达Mapper接口
 * 
 * @author ckw
 * @date 2025-01-25
 */
@Mapper
public interface XianApiDfPlatformOdsSwjJcsjQxjkDzldMapper extends BaseMapper<XianApiDfPlatformOdsSwjJcsjQxjkDzld> {
    
    /**
     * 根据时间范围查询雷达数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    
    /**
     * 根据雷达编号查询数据
     * 
     * @param radarnum 雷达编号
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectByRadarnum(@Param("radarnum") String radarnum);
    
    /**
     * 获取最新的雷达数据
     * 
     * @param limit 限制条数
     * @return 雷达数据列表
     */
    List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> selectLatestData(@Param("limit") Integer limit);
}