package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.FactorVO;

import java.util.HashMap;
import java.util.List;

public interface IModelService {
    public List<HashMap<String, String>> rainSlideTrigger(List<List<FactorVO>> factorList);
}
