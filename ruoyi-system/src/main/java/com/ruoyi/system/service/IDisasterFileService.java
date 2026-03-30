package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.DisasterInfoDTO;
import com.ruoyi.system.domain.vo.DisasterFileVO;

import java.util.List;

/**
 * 灾害文件管理 Service 接口
 *
 * @author ruoyi
 * @date 2026-03-29
 */
public interface IDisasterFileService {

    /**
     * 查询灾害列表（暴雨和地震）
     *
     * @param disasterType 灾害类型（rain:暴雨，earthquake:地震，null 或空：全部）
     * @return 灾害信息列表
     */
    List<DisasterInfoDTO> selectDisasterList(String disasterType);

    /**
     * 根据灾害 ID 和类型查询文件列表
     *
     * @param disasterId 灾害 ID
     * @param disasterType 灾害类型
     * @param occurrenceTime 灾害发生时间（用于生成模糊匹配的 ID）
     * @return 文件列表
     */
    List<DisasterFileVO> selectFilesByDisasterId(String disasterId, String disasterType, String occurrenceTime);

    /**
     * 删除灾害记录（逻辑删除）
     *
     * @param disasterId 灾害 ID
     * @param disasterType 灾害类型
     * @return 结果
     */
    int deleteDisasterById(String disasterId, String disasterType);
}
