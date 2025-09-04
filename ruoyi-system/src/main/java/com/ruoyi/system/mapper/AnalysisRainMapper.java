package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.AnalysisRain;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnalysisRainMapper extends BaseMapper<AnalysisRain> {



    @Select("SELECT * FROM xian_api_df_platform_ods_xasqxj_sksj_xssk " +
            "WHERE admin_code_chn = #{adminCode} " +
            "AND TO_TIMESTAMP(datetime, 'YYYYMMDDHH24MISS') IN (" +
            "    SELECT DISTINCT TO_TIMESTAMP(datetime, 'YYYYMMDDHH24MISS') " +
            "    FROM xian_api_df_platform_ods_xasqxj_sksj_xssk " +
            "    WHERE admin_code_chn = #{adminCode} " +
            "    ORDER BY TO_TIMESTAMP(datetime, 'YYYYMMDDHH24MISS') DESC " +
            "    LIMIT 12" +
            ") " +
            "ORDER BY station_name DESC")
            // "ORDER BY TO_TIMESTAMP(datetime, 'YYYYMMDDHH24MISS') DESC")
    List<AnalysisRain> getRainStation(@Param("adminCode") String adminCode);


}
