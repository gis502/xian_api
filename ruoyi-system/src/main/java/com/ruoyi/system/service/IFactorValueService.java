package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.vo.FactorVO;

import java.util.List;

public interface IFactorValueService {


    // 根据 hideId 查询
    public List<FactorVO> getFactorValueByHideId(Integer hideId);

    // 获取所有的 hideId
    public List<FactorValueDTO> getAllHideId();

}
