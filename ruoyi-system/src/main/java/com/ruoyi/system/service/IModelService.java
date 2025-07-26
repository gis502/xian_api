package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.ModelGetDataDTO;
import com.ruoyi.system.domain.dto.ModelGetDataFactorListEntityIdDTO;
import com.ruoyi.system.domain.vo.FactorVO;

import java.util.List;

public interface IModelService {

    public List<ModelGetDataDTO> rainSlideTrigger(List<List<FactorVO>> factorList);


    public ModelGetDataDTO rainSlideFactorUpdata(List<FactorVO> factorList);


    public List<ModelGetDataDTO> eqSlideTrigger(List<ModelGetDataFactorListEntityIdDTO> request);


    public ModelGetDataDTO eqSlideFactorUpdata(List<FactorVO> factorList);
}
