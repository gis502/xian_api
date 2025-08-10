package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.utils.GeometryUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.service.IXianDisasterRainService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Slf4j
@Service
public class XianDisasterRainImpl extends ServiceImpl<XianDisasterRainMapper, XianDisasterRain> implements IXianDisasterRainService {

    @Resource
    private XianDisasterRainMapper disasterRainMapper;

    // 暴雨灾害存库
    @Async("taskExecutor")
    @Override
    public void saveDisasterRain(DisasterRainDTO disasterRainDTO) {

        if (disasterRainDTO == null) {
            throw new ParamsException(XianConstants.PARAMS_EMPTY);
        }

        log.info("正在存储暴雨灾害数据...");

        XianDisasterRain disasterRain = new XianDisasterRain();

        BeanUtils.copyProperties(disasterRainDTO, disasterRain);
        // 处理 geom
        disasterRain.setIsDeleted(0);
        disasterRain.setCreateTime(LocalDateTime.now());
        disasterRain.setUpdateTime(LocalDateTime.now());
        disasterRain.setGeom(GeometryUtils.convertPoint(disasterRainDTO.getLongitude(), disasterRainDTO.getLatitude()));

        log.info("正在处理数据...{}", disasterRain);

        disasterRainMapper.insert(disasterRain);
        log.info("存储暴雨灾害数据成功！");
    }

    // 获取所有暴雨灾害数据
    @Override
    public List<DisasterRainDTO> selectAllEq() {

        List<XianDisasterRain> rainslist = disasterRainMapper.selectList(new QueryWrapper<XianDisasterRain>().eq("is_deleted", 0));

        // 抛出异常
        if (rainslist == null) {
            throw new ParamsException(XianConstants.RESULT_EMPTY);
        }

        List<DisasterRainDTO> listDto = new ArrayList<>();
        // 转换数据
        for (XianDisasterRain rain : rainslist) {

            log.info("正在处理数据...{}", rain);

            DisasterRainDTO disasterRainDTO = new DisasterRainDTO();
            BeanUtils.copyProperties(rain, disasterRainDTO);

            // 处理经纬度
//            Point point = GeometryUtils.convertPoint(
//                    rain.getGeom().getCoordinate().x,
//                    rain.getGeom().getCoordinate().y
//            );
//            disasterRainDTO.setLongitude(point.getX());
//            disasterRainDTO.setLatitude(point.getY());
            // 加入到列表
            listDto.add(disasterRainDTO);
        }
        return listDto;
    }

    // 根据 Id 获取所有暴雨灾害数据
    @Override
    public DisasterRainDTO getDisasterRainById(Long Id) {

        if (!StringUtils.isNull(Id)) {

            XianDisasterRain disasterRain = disasterRainMapper.
                    selectOne(new QueryWrapper<XianDisasterRain>()
                            .eq("disaster_id", Id)
                            .eq("is_deleted", 0));

            if (!StringUtils.isNull(disasterRain)){
                DisasterRainDTO disasterRainDTO = new DisasterRainDTO();
                BeanUtils.copyProperties(disasterRain, disasterRainDTO);
                return disasterRainDTO;
            }
            // 处理经纬度
//            Point point = GeometryUtils.convertPoint(
//                    disasterRain.getGeom().getCoordinate().x,
//                    disasterRain.getGeom().getCoordinate().y
//            );
//            disasterRainDTO.setLongitude(point.getX());
//            disasterRainDTO.setLatitude(point.getY());
        }
        // 抛出异常
        throw new ParamsException(XianConstants.RESULT_EMPTY);
    }

}
