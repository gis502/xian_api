package com.ruoyi.system.domain.dto;

import lombok.Data;

/**
 * @description: 通用灾害点 DTO（支持隐患点和风险区域）
 */
@Data
public class DisasterPointDTO {

    // 基础 ID（隐患点用 id，风险区域也用 id）
    private Integer id;

    // 野外编号/单位编码（隐患点用 fieldCode，风险区域用 unitCode）
    private String code;

    // 县
    private String county;

    // 县编号
    private Integer countyId;

    // 乡镇
    private String village;

    // 灾害点名称
    private String disasterName;

    // 纬度
    private String latitude;

    // 经度
    private String longitude;

    // 纬度（双精度）
    private Double lon;

    // 经度（双精度）
    private Double lat;

    // 位置
    private String position;

    // 灾害类型（滑坡、泥石流、山洪、内涝、风险区域）
    private String disasterType;

    // 规模等级
    private String scaleGrade;

    // 风险等级
    private String riskGrade;

    /**
     * 从 GeologicalDisasterHideDTO 转换
     */
    public static DisasterPointDTO fromHideDTO(GeologicalDisasterHideDTO hideDTO) {
        if (hideDTO == null) {
            return null;
        }

        DisasterPointDTO dto = new DisasterPointDTO();
        dto.setId(hideDTO.getId());
        dto.setCode(hideDTO.getFieldCode());
        dto.setCounty(hideDTO.getCounty());
        dto.setCountyId(hideDTO.getCountyId());
        dto.setVillage(hideDTO.getVillage());
        dto.setDisasterName(hideDTO.getDisasterName());
        dto.setLatitude(hideDTO.getLatitude());
        dto.setLongitude(hideDTO.getLongitude());
        dto.setLon(hideDTO.getLon());
        dto.setLat(hideDTO.getLat());
        dto.setPosition(hideDTO.getPosition());
        dto.setDisasterType(hideDTO.getDisasterType());
        dto.setScaleGrade(hideDTO.getScaleGrade());
        dto.setRiskGrade(hideDTO.getRiskGrade());

        return dto;
    }

    /**
     * 从风险区域实体转换（需要导入 GeologicalDisasterRisk 类）
     */
    public static DisasterPointDTO fromRiskEntity(Object riskEntity) {
        if (riskEntity == null) {
            return null;
        }

        // 这里需要根据实际的 GeologicalDisasterRisk 实体类来转换
        // 由于可能存在循环依赖，我们使用反射或者在调用方直接构造
        DisasterPointDTO dto = new DisasterPointDTO();
        // 具体属性由调用方设置

        return dto;
    }
}
