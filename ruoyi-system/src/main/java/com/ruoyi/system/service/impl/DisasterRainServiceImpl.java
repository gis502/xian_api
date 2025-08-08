package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.utils.GeometryUtils;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.DisasterRain;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.DisasterRainMapper;
import com.ruoyi.system.service.IDisasterRainService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-08-07 23:04
 * @description: 暴雨灾害实现类
 */

@Slf4j
@Service
public class DisasterRainServiceImpl extends ServiceImpl<DisasterRainMapper, XianDisasterRain> implements IDisasterRainService {

    @Resource
    private DisasterRainMapper disasterRainMapper;

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

        disasterRainMapper.insert(disasterRain);

        // 获取到rainDisasterId后，可以让其他地方拿到这个Id，进行操作
        Long rainDisasterId = disasterRain.getDisasterId();

        log.info("存储暴雨灾害数据成功！");
    }
}
