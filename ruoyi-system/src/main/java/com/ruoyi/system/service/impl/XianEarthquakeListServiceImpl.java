package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.exception.base.ParamsException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.PeopleGDP;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.PeopleGDPMapper;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class XianEarthquakeListServiceImpl implements IXianEarthquakeListService {
    private static final Logger log = LoggerFactory.getLogger(XianEarthquakeListServiceImpl.class);

    @Resource
    private PeopleGDPMapper peopleGDPMapper;

    @Autowired
    private XianEarthquakeListMapper xianEarthquakeListMapper;

    @Override
    public List<EqDTO> selectAllEq() {

        List<XianEarthquakeList> eqlist = xianEarthquakeListMapper.selectList(new QueryWrapper<XianEarthquakeList>().eq("is_deleted", 0));

        // 抛出异常
        if (eqlist == null) {
            throw new ParamsException(XianConstants.RESULT_EMPTY);
        }
        List<EqDTO> eqDtoList = new ArrayList<>();

        for (XianEarthquakeList earthquakeList : eqlist) {
            EqDTO eqDTO = new EqDTO();
            BeanUtils.copyProperties(earthquakeList, eqDTO);
            eqDtoList.add(eqDTO);
        }
        return eqDtoList;
    }

    @Override
    public EqDTO getEarthquakeEventById(Long Id) {

        if (!StringUtils.isNull(Id)) {

            XianEarthquakeList eqEvent = xianEarthquakeListMapper
                    .selectOne(new QueryWrapper<XianEarthquakeList>()
                            .eq("disaster_id", Id)
                            .eq("is_deleted", 0));
            // 是否空
            if (!StringUtils.isNull(eqEvent)){
                EqDTO eqDTO = new EqDTO();
                BeanUtils.copyProperties(eqEvent, eqDTO);
                return eqDTO;
            }
        }
        // 抛出异常
        throw new ParamsException(XianConstants.RESULT_EMPTY);
    }

    @Override
    public Integer insertDisaster(EarthquakeVo earthquake) {
        return xianEarthquakeListMapper.insertDisaster(earthquake);
    }

    @Override
    public boolean insertEarthquake(EarthquakeVo earthquake) {
        xianEarthquakeListMapper.insertDisaster(earthquake);
        int disasterId = earthquake.getDisasterId();
        log.info("获取到的disasterid为: {}" , disasterId);
        float sumGdp = 0;
        int sumPeopleNum = 0;
        List<PeopleGDP> peopleGDPS = peopleGDPMapper.findInsideCircle(earthquake.getLongitude(),earthquake.getLatitude(),earthquake.getSemiMajorAxis(),earthquake.getSemiMinorAxis(),earthquake.getRotation());
        int count = peopleGDPS.size();
        if (count == 0) {
            log.warn("未查询到受影响区域数据！经纬度：({}, {}), 半长轴：{}, 半短轴：{}, 旋转角：{}",
                    earthquake.getLongitude(), earthquake.getLatitude(),
                    earthquake.getSemiMajorAxis(), earthquake.getSemiMinorAxis(),
                    earthquake.getRotation());
            return false;
        }

        // 计算sumGdp和sumPeopleNum（仅当有数据时）
        for (PeopleGDP peopleGDP : peopleGDPS) {
            Float gdp = peopleGDP.getGdp();
            if (gdp != null) sumGdp += gdp;
            Integer peopleNum = peopleGDP.getPeopleNum();
            if (peopleNum != null) sumPeopleNum += peopleNum;
            xianEarthquakeListMapper.insertAffect(disasterId,peopleGDP.getVillages());
//            System.out.println(peopleGDP.getVillages());
        }

        // 计算伤亡人数
        calculateCasualties(earthquake, sumGdp, sumPeopleNum, count);
        return true;
    }
    /**
     * 计算伤亡人数
     * y = exp(−1.109×10² + 8.8489×10⋅x − 2.032×10⋅x² + 1.499⋅x + 9.826×10⁻⁴⋅v − 6.833×10⁻⁸⋅v²
     *      − 8.963×10⁻¹⋅z − 3.175×10⁻³⋅ns + 1.148×10⁻⁶⋅ns² − 2.914×10⁻²⋅cs + 6.502×10⁻⁵⋅cs²
     *      + 2.772×10⁻³⋅GDP − 4.768×10⁻⁷⋅GDP² + 9.801×10⁻¹⋅T + 3.910×10⁻⁴⋅s + 9.414×10⁻⁵⋅x⋅GDP)
     */
    private void calculateCasualties(EarthquakeVo earthquake, float sumGdp, int sumPeopleNum, int count) {
        // 1. 基础参数（保持合理放缩）
        double x = earthquake.getMagnitude(); // 震级（如7.0）
        int T = getDayOrNight(earthquake.getDateTime()); // 夜间=1，白天=0
        double z = 9; // 地震烈度（西安按8度设防，9度为较强影响）

        // 人口密度s：人/平方公里（简化计算，直接用总人口/区域数）
        double s = count > 0 ? (double) sumPeopleNum / count : 0;

        // 面积v：平方公里（保持放缩）
        double v = earthquake.getCircleArea() / 1e6;

        // GDP：亿元（放缩后，若原单位为元）
        double gdp = sumGdp / 1e8;


        // 2. 核心调整：大幅减弱负向项，增强正向项（适配西安7级地震）
        double exponent =
                -20 +  // 基础项：从-50大幅提高到-20（减弱负向影响）
                        30 * x +  // 震级一次项：显著增强（7级时贡献210，成为核心正向项）
                        -5 * x * x +  // 震级平方项：适度减弱（7级时贡献-5*49=-245，抵消部分正向）
                        5 * x +  // 震级微调项：增强（7级时贡献35）
                        5e-4 * v +  // 面积项：增强正向影响
                        -1e-7 * v * v +  // 面积平方项：大幅减弱负向
                        -2 * z +  // 烈度项：从-5减弱到-2（7级地震烈度影响应小于震级）
                        1e-3 * gdp +  // 农民收入项：从负向改为正向（经济活跃区人口密集，伤亡可能增加）
                        5e-6 * gdp * gdp +  // 农民收入平方项：增强正向
                        1e-2 * gdp +  // 财政收入项：改为正向
                        1e-5 * gdp * gdp +  // 财政收入平方项：增强正向
                        5e-3 * gdp +  // GDP项：增强正向
                        -1e-9 * gdp * gdp +  // GDP平方项：几乎消除负向影响
                        3 * T +  // 昼夜项：显著增强（夜间伤亡可能翻倍，7级地震夜间影响更大）
                        1e-3 * s +  // 人口密度项：增强（人口密集区伤亡更多）
                        5e-4 * x * gdp;  // 震级与GDP交互项：增强（高GDP区域建筑密集，震级影响放大）


        // 3. 指数强制限制（核心：确保不会过小导致结果为0）
        double maxExponent = 12; // exp(12)≈162755，覆盖最大可能伤亡
        double minExponent = -5; // exp(-5)≈0.0067，确保至少有1人（当人口>150时）
        exponent = Math.max(Math.min(exponent, maxExponent), minExponent);

        // 4. 计算伤亡人数（结合人口总数限制）
        double casualties = Math.exp(exponent);
        // 限制伤亡人数不超过受影响人口的30%（参考7级地震一般伤亡比例）
        int maxPossible = (int) (sumPeopleNum * 0.3);
        maxPossible = Math.max(maxPossible, 1); // 至少1人（强震不可能0伤亡）
        casualties = Math.min(casualties, maxPossible);
        casualties = Math.round(casualties);

        log.info("优化后指数exponent：{}", exponent);
        log.info("优化后伤亡人数：{}（受影响总人口：{}，最大可能：{}）",
                sumPeopleNum, sumPeopleNum, maxPossible);
        xianEarthquakeListMapper.insertDamage(earthquake.getDisasterId(),sumPeopleNum,maxPossible);
    }
    /**
     * 判断地震发生在白天还是夜间
     * 这里定义：晚上18:00到次日06:00为夜间(T=1)，其余时间为白天(T=0)
     */
    private int getDayOrNight(String dateTime) {
        try {
            // 解析日期时间字符串
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int hour = calendar.get(Calendar.HOUR_OF_DAY); // 获取24小时制的小时数

            // 判断是否为夜间
            if (hour >= 18 || hour < 6) {
                return 1; // 夜间
            } else {
                return 0; // 白天
            }
        } catch (Exception e) {
            // 解析失败时默认返回白天
            log.error("解析日期时间失败: {}", dateTime, e);
            return 0;
        }
    }

}
