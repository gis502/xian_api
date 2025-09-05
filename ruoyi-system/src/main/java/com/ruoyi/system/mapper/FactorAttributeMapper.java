package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.FactorAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FactorAttributeMapper extends BaseMapper<FactorAttribute> {

    @Select("select attribute_name from xian_factor_attribute")
    List<String> getFactorAttributeName();


}
