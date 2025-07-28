package com.ruoyi.system.domain.dto;


import com.ruoyi.system.domain.vo.FactorVO;
import lombok.Data;

import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-07-28 0:43
 * @description: 所有致灾因子值
 */

@Data
public class BatchHideIdsDTO {

    private Integer hideId;
    private List<FactorVO> factorList;

}
