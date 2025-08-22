package com.ruoyi.system.domain.dto;

import lombok.Data;
import java.util.List;

/**
 * 灾害数据查询参数
 */
@Data
public class DisasterQueryDTO {
    // 页码（默认第1页）
    private Integer pageNum = 1;
    // 每页条数（默认10条）
    private Integer pageSize = 10;
    // 灾害类型数组（如 ["rain", "fire"]）
    private List<String> disasterTypes;
}
