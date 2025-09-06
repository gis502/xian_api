package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianImpactInAreaEntity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface XianImpactInAreaMapper extends BaseMapper<XianImpactInAreaEntity> {
    List<XianImpactInAreaEntity> queryPeople(Long disasterId);

    List<XianImpactInAreaEntity> queryTraffic(Long disasterId);

    List<XianImpactInAreaEntity> queryDanger(Long disasterId);

    List<XianImpactInAreaEntity> queryStation(Long disasterId);
}