package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.entity.FactorValue;
import com.ruoyi.system.domain.vo.FactorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FactorValueMapper extends BaseMapper<FactorValue> {

    @Select("select  c.hide_id,b.attribute_id,c.value_id,b.attribute_name,c.factor_value,b.unit, b.attribute_name_alias from\n" +
            "     xian_factor_value as c ,\n" +
            "     xian_factor_attribute as b\n" +
            "where c.attribute_id = b.attribute_id and c.hide_id = #{hideId} ")
    // 根据 hideId 查询
    public List<FactorVO> getFactorValueByHideId(Integer hideId);


}
