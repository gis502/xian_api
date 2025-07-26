package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.vo.FactorVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class ModelGetDataFactorListEntityIdDTO implements Serializable {
    private List<FactorVO> factorVoList;

    private String entityId;

}
