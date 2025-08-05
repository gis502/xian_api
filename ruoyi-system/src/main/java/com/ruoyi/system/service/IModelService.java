package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.ModelGetDataDTO;
import com.ruoyi.system.domain.dto.ModelGetDataFactorListEntityIdDTO;
import com.ruoyi.system.domain.entity.FactorAnalysis;
import com.ruoyi.system.domain.entity.FactorValue;
import com.ruoyi.system.domain.vo.FactorVO;

import java.util.List;

public interface IModelService extends IService<FactorAnalysis> {

    public List<ModelGetDataDTO> rainSlideTrigger(List<List<FactorVO>> factorList);


    public ModelGetDataDTO rainSlideFactorUpdata(List<FactorVO> factorList);


    public List<ModelGetDataDTO> eqSlideTrigger(List<ModelGetDataFactorListEntityIdDTO> request);


    public ModelGetDataDTO eqSlideFactorUpdata(List<FactorVO> factorList);
}
