package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.dto.DisasterInfoDTO;
import com.ruoyi.system.domain.vo.DisasterFileVO;
import com.ruoyi.system.mapper.DisasterFileMapper;
import com.ruoyi.system.service.IDisasterFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 灾害文件管理 Service 业务层处理
 *
 * @author ruoyi
 * @date 2026-03-29
 */
@Service
public class DisasterFileServiceImpl implements IDisasterFileService {

    @Autowired
    private DisasterFileMapper disasterFileMapper;

    /**
     * 查询灾害列表（暴雨和地震）
     *
     * @param disasterType 灾害类型（rain:暴雨，earthquake:地震，null 或空：全部）
     * @return 灾害信息列表
     */
    @Override
    public List<DisasterInfoDTO> selectDisasterList(String disasterType) {
        return disasterFileMapper.selectDisasterList(disasterType);
    }

    /**
     * 根据灾害 ID 和类型查询文件列表
     *
     * @param disasterId 灾害 ID
     * @param disasterType 灾害类型
     * @param occurrenceTime 灾害发生时间（用于生成模糊匹配的 ID）
     * @return 文件列表
     */
    @Override
    public List<DisasterFileVO> selectFilesByDisasterId(String disasterId, String disasterType, String occurrenceTime) {
        // 在 Java 层处理时间字符串，计算前后 3 秒的时间范围
        String[] timeRange = calculateTimeRange(occurrenceTime);
        return disasterFileMapper.selectFilesByDisasterId(disasterId, disasterType, timeRange[0], timeRange[1]);
    }

    /**
     * 计算前后 3 秒的时间范围
     * @param occurrenceTime 原始时间字符串（格式：yyyy-MM-dd HH:mm:ss）
     * @return 时间范围数组 [startTime, endTime]
     */
    private String[] calculateTimeRange(String occurrenceTime) {
        if (occurrenceTime == null || occurrenceTime.isEmpty()) {
            return new String[]{"", ""};
        }
        try {
            // 解析时间字符串
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(
                occurrenceTime.replace(" ", "T"), 
                java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            
            // 计算前后 3 秒
            java.time.LocalDateTime startTime = dateTime.minusSeconds(3);
            java.time.LocalDateTime endTime = dateTime.plusSeconds(3);
            
            // 格式化为不带特殊字符的格式
            java.time.format.DateTimeFormatter formatter = 
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            
            return new String[]{
                startTime.format(formatter),
                endTime.format(formatter)
            };
        } catch (Exception e) {
            // 如果解析失败，返回空范围
            return new String[]{"", ""};
        }
    }

    /**
     * 删除灾害记录（逻辑删除）
     *
     * @param disasterId 灾害 ID
     * @param disasterType 灾害类型
     * @return 结果
     */
    @Override
    public int deleteDisasterById(String disasterId, String disasterType) {
        return disasterFileMapper.deleteDisasterById(disasterId, disasterType);
    }
}
