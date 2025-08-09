package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.BatchHideIdsDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.entity.FactorValue;
import com.ruoyi.system.domain.vo.FactorVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFactorValueService {


    // 根据 hideId 查询
    public List<FactorVO> getFactorValueByHideId(Integer hideId);

    // 批量查询因子值
    public List<FactorVO> getFactorValuesByHideIds(List<Integer> hideIds);

    // 获取所有的 hideId
    public Set<Integer> getAllHideId();

    // 获取因子可选列表值
    public Map<String, List<String>> getFactorValueList();

    public void saveFactorValue(FactorValueDTO factorValueDTO);

}
