package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.exception.base.TriggerException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.http.HttpRestClient;
import com.ruoyi.system.domain.dto.OutputDTO;
import com.ruoyi.system.domain.dto.TriggerDTO;
import com.ruoyi.system.domain.entity.AssessmentOutput;
import com.ruoyi.system.domain.params.ThematicQuery;
import com.ruoyi.system.domain.vo.GeneralVO;
import com.ruoyi.system.domain.vo.TokenVO;
import com.ruoyi.system.mapper.SlaveAssessmentOutputMapper;
import com.ruoyi.system.service.IFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-08-26 15:48
 * @description: 第三方接口实现
 */

@Slf4j
@Service
public class FeignServiceImpl implements IFeignService {


    @Resource
    private SlaveAssessmentOutputMapper slaveAssessmentOutputMapper;

    @Resource
    private HttpRestClient httpRestClient;

    // 地震触发
    @Override
    public ThematicQuery trigger(TriggerDTO triggerDTO) {
        // 抛异常
        if (triggerDTO == null) {
            throw new ParamsException(XianConstants.PARAMS_EMPTY);
        }
        try {
            // 获取授权
            ParameterizedTypeReference<TokenVO> tokenType = new ParameterizedTypeReference<TokenVO>() {
            };

            TokenVO tokenVO = httpRestClient.post(XianConstants.AUTH_URL, XianConstants.authBody, tokenType);

            if (!tokenVO.getCode().equals(HttpStatus.SUCCESS)) {
                throw new ParamsException(XianConstants.AUTH_ERROR);
            }

            // 设置请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("eqName", triggerDTO.getEqName());
            requestBody.put("eqAddr", triggerDTO.getEqAddr());
            requestBody.put("eqTime", triggerDTO.getEqTime());
            requestBody.put("longitude", triggerDTO.getLongitude());
            requestBody.put("latitude", triggerDTO.getLatitude());
            requestBody.put("eqDepth", triggerDTO.getEqDepth());
            requestBody.put("magnitude", triggerDTO.getMagnitude());
            requestBody.put("eqType", triggerDTO.getEqType());
            requestBody.put("faultZone", triggerDTO.getFaultZone());
            requestBody.put("circleArea", triggerDTO.getCircleArea());
            requestBody.put("affectPopMax", triggerDTO.getAffectPopMax());
            requestBody.put("affectPopMin", triggerDTO.getAffectPopMin());
            requestBody.put("diePopMax", triggerDTO.getDiePopMax());
            requestBody.put("diePopMin", triggerDTO.getDiePopMin());
            requestBody.put("intensity", triggerDTO.getIntensity());



            ParameterizedTypeReference<GeneralVO> triggerType = new ParameterizedTypeReference<GeneralVO>() {
            };
            // 触发地震接口
            GeneralVO generalVO = httpRestClient.post(XianConstants.TRIGGER_EARTHQUAKE_URL, tokenVO.getToken(), requestBody, triggerType);
            if (!generalVO.getCode().equals(HttpStatus.SUCCESS)) {
                throw new ParamsException(XianConstants.RESULT_EMPTY);
            }
            log.info("地震参数已导入模型，开始评估...");

            return generalVO.getData();

        } catch (Exception e) {
            log.error("请求第三方接口异常:{}", e.getMessage());

            throw new TriggerException(XianConstants.TRIGGER_ERROR);
        }
    }

    // 专题图产出
    @DataSource(value = DataSourceType.SLAVE)   // 使用从库数据源
    @Override
    public List<OutputDTO> thematicMap(ThematicQuery query) {

        try {
            QueryWrapper<AssessmentOutput> wrapper = new QueryWrapper<>();
            wrapper.eq("eq_id", query.getEqId());
            wrapper.eq("eqqueue_id", query.getEqqueueId());
            wrapper.eq("is_deleted", 0);

            List<AssessmentOutput> outputs = slaveAssessmentOutputMapper.selectList(wrapper);

            // 抛异常
            if (outputs == null || outputs.size() == 0) {
                throw new ParamsException(XianConstants.RESULT_EMPTY);
            }

            List<OutputDTO> outputsDTO = new ArrayList<>();
            for (AssessmentOutput output : outputs) {
                OutputDTO outputDTO = new OutputDTO();
                BeanUtils.copyProperties(output, outputDTO);
                outputsDTO.add(outputDTO);
            }

            return outputsDTO;

        } catch (Exception e) {
            log.error("获取专题图数据失败：{}", e.getMessage());
            e.printStackTrace();
        }

        throw new ParamsException(XianConstants.RESULT_EMPTY);
    }

    @DataSource(value = DataSourceType.SLAVE)   // 使用从库数据源
    // TODO 灾情报告产出
    @Override
    public List<OutputDTO> disasterReport(ThematicQuery query) {
        return null;
    }
}
