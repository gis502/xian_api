package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.exception.base.TriggerException;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.http.HttpRestClient;
import com.ruoyi.system.domain.dto.*;
import com.ruoyi.system.domain.entity.AssessmentOutput;
import com.ruoyi.system.domain.entity.RainAssessmentOutput;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.params.ThematicQuery;
import com.ruoyi.system.domain.vo.EqGeneralVO;
import com.ruoyi.system.domain.vo.RainGeneralVO;
import com.ruoyi.system.domain.vo.TokenVO;
import com.ruoyi.system.mapper.SlaveAssessmentOutputMapper;
import com.ruoyi.system.mapper.SlaveRainAssessmentOutputMapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private SlaveRainAssessmentOutputMapper slaveRainAssessmentOutputMapper;

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
            ParameterizedTypeReference<EqGeneralVO> triggerType = new ParameterizedTypeReference<EqGeneralVO>() {};

            JSONObject requestBody = buildEqRequestBody(triggerDTO);

            // 触发地震接口
            EqGeneralVO eqGeneralVO = httpRestClient.post(XianConstants.TRIGGER_EARTHQUAKE_URL, tokenVO.getToken(), requestBody, triggerType);
            if (!eqGeneralVO.getCode().equals(HttpStatus.SUCCESS)) {
                throw new ParamsException(XianConstants.RESULT_EMPTY);
            }
            log.info("地震参数已导入模型，开始评估...");

            return eqGeneralVO.getData();

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
                throw new ParamsException(XianConstants.THEMATIC_EMPTY);
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
// 灾情报告产出
    @Override
    public List<OutputDTO> disasterReport(ThematicQuery query) {
        return null;
    }

    // 暴雨触发
    @Override
    public RainQuery trigger(RainTriggerDTO triggerDTO) {

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
            JSONObject requestBody = buildRainRequestBody(triggerDTO);
            ParameterizedTypeReference<RainGeneralVO> triggerType = new ParameterizedTypeReference<RainGeneralVO>() {
            };
            // 触发地震接口
            RainGeneralVO rainGeneralVO = httpRestClient.post(XianConstants.TRIGGER_STORM_URL, tokenVO.getToken(), requestBody, triggerType);
            if (!rainGeneralVO.getCode().equals(HttpStatus.SUCCESS)) {
                throw new ParamsException(XianConstants.RESULT_EMPTY);
            }
            log.info("地震参数已导入模型，开始评估...");

            return rainGeneralVO.getData();

        } catch (Exception e) {
            log.error("请求第三方接口异常:{}", e.getMessage());

            throw new TriggerException(XianConstants.TRIGGER_ERROR);
        }
    }

    // 暴雨专题图产出
    @DataSource(value = DataSourceType.SLAVE)   // 使用从库数据源
    @Override
    public List<RainOutputDTO> thematicMap(RainQuery query) {
        try {
            QueryWrapper<RainAssessmentOutput> wrapper = new QueryWrapper<>();
            wrapper.eq("rain_id", query.getRainId());
            wrapper.eq("rain_queue_id", query.getRainQueueId());
            wrapper.eq("is_deleted", 0);

            List<RainAssessmentOutput> outputs = slaveRainAssessmentOutputMapper.selectList(wrapper);

            // 抛异常
            if (outputs == null || outputs.size() == 0) {
                throw new ParamsException(XianConstants.RESULT_EMPTY_RAIN);
            }

            List<RainOutputDTO> outputsDTO = new ArrayList<>();
            for (RainAssessmentOutput output : outputs) {
                RainOutputDTO outputDTO = new RainOutputDTO();
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

    // 构建地震请求体
    private JSONObject buildEqRequestBody(TriggerDTO triggerDTO) {

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


        return requestBody;
    }

    // 构建暴雨请求体
    private JSONObject buildRainRequestBody(RainTriggerDTO triggerDTO) {

        JSONObject requestBody = new JSONObject();
        requestBody.put("position", triggerDTO.getPosition());
        requestBody.put("rainfall", triggerDTO.getRainfall());
        requestBody.put("duration", triggerDTO.getDuration());
        requestBody.put("occurrenceTime", triggerDTO.getOccurrenceTime());
        requestBody.put("longitude", triggerDTO.getLongitude());
        requestBody.put("latitude", triggerDTO.getLatitude());
        requestBody.put("rainType", triggerDTO.getRainType());

        return requestBody;
    }

    @DataSource(value = DataSourceType.SLAVE)
    @Override
    public String downloadReport(ReportDTO reportDTO){
        String url = "";
        try {
            QueryWrapper<AssessmentOutput> wrapper = new QueryWrapper<>();
            wrapper.eq("eq_id", reportDTO.getEqId());
            wrapper.eq("eqqueue_id", reportDTO.getEqqueueId());
            wrapper.eq("type", 2);
            wrapper.eq("is_deleted", 0);

            AssessmentOutput output = slaveAssessmentOutputMapper.selectOne(wrapper);

            // 抛异常
            if (output == null) {
                throw new ParamsException(XianConstants.RESULT_EMPTY);
            }

            url = output.getSourceFile();
        } catch (Exception e) {
            log.error("获取报告失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return url;
    }
}
