package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.entity.XianImpactInAreaEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.Map;

@Mapper
public interface XianImpactInAreaMapper extends BaseMapper<XianImpactInAreaEntity> {
    List<XianImpactInAreaEntity> queryPeople(Long disasterId);

    List<XianImpactInAreaEntity> queryTraffic(Long disasterId);

    List<XianImpactInAreaEntity> queryDanger(Long disasterId);

    List<XianImpactInAreaEntity> queryStation(Long disasterId);


    @Select("SELECT people_num FROM xian_people WHERE ST_Contains(point, ST_SetSRID(ST_MakePoint(#{lon}::DOUBLE PRECISION, #{lat}::DOUBLE PRECISION), 4490))")
    Long getPeopleByLatLon(@Param("lat") String lat, @Param("lon") String lon);

    /**
     * 根据多边形坐标查询相交区域的人口数量
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交区域的人口数量列表
     */
    @Select("SELECT people_num FROM xian_people WHERE ST_Intersects(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Integer> getPeople(@Param("polygonWkt") String polygonWkt);

    /**
     * 获取与多边形相交的所有记录
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的完整记录列表
     */
    @Select("SELECT * FROM xian_people WHERE ST_Intersects(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingRecords(@Param("polygonWkt") String polygonWkt);

    /**
     * 计算与多边形相交区域的总人口数
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 总人口数
     */
    @Select("SELECT COALESCE(SUM(people_num), 0) FROM xian_people WHERE ST_Intersects(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getTotalPeopleInPolygon(@Param("polygonWkt") String polygonWkt);

            // ==================== 高速相关查询方法 ====================
    /**
     * 查询与多边形相交的高速公路数据
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的高速公路记录列表
     */
    @Select("SELECT * FROM xian_highway WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingHighways(@Param("polygonWkt") String polygonWkt);

    /**
     * 计算与多边形相交的高速公路数量
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的高速公路数量
     */
    @Select("SELECT COUNT(*) FROM xian_highway WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getIntersectingHighwayCount(@Param("polygonWkt") String polygonWkt);

    /**
     * 获取与多边形相交的高速公路的详细信息（如果有名称等字段）
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的高速公路详细信息列表
     */
    @Select("SELECT id, name, geom FROM xian_highway WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingHighwayDetails(@Param("polygonWkt") String polygonWkt);


    // ==================== 国道相关查询方法 ====================

    /**
     * 查询与多边形相交的国道数据
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的国道记录列表
     */
    @Select("SELECT * FROM xian_national_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingNationalRoads(@Param("polygonWkt") String polygonWkt);

    /**
     * 计算与多边形相交的国道数量
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的国道数量
     */
    @Select("SELECT COUNT(*) FROM xian_national_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getIntersectingNationalRoadCount(@Param("polygonWkt") String polygonWkt);

    /**
     * 获取与多边形相交的国道详细信息
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的国道详细信息列表
     */
    @Select("SELECT id, name, geom FROM xian_national_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingNationalRoadDetails(@Param("polygonWkt") String polygonWkt);

    // ==================== 普通道路相关查询方法 ====================

    /**
     * 查询与多边形相交的普通道路数据
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的普通道路记录列表
     */
    @Select("SELECT * FROM xian_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingRoads(@Param("polygonWkt") String polygonWkt);

    /**
     * 计算与多边形相交的普通道路数量
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的普通道路数量
     */
    @Select("SELECT COUNT(*) FROM xian_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getIntersectingRoadCount(@Param("polygonWkt") String polygonWkt);

    /**
     * 获取与多边形相交的普通道路详细信息
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的普通道路详细信息列表
     */
    @Select("SELECT id, road_name, geom FROM xian_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getIntersectingRoadDetails(@Param("polygonWkt") String polygonWkt);

    // ==================== 综合道路统计方法 ====================

    /**
     * 计算与多边形相交的所有道路总数量（高速+国道+普通道路）
     * @param polygonWkt 多边形的WKT格式字符串
     * @return 相交的所有道路总数量
     */
    @Select("SELECT (" +
            "(SELECT COUNT(*) FROM xian_highway WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))) + " +
            "(SELECT COUNT(*) FROM xian_national_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490))) + " +
            "(SELECT COUNT(*) FROM xian_road WHERE ST_Intersects(geom, ST_GeomFromText(#{polygonWkt}, 4490)))" +
            ") AS total_road_count")
    Integer getTotalIntersectingRoadCount(@Param("polygonWkt") String polygonWkt);

    // ==================== 地铁站统计方法 ====================

    // 查询地铁站点 - 获取与多边形相交的地铁站点记录
    @Select("SELECT * FROM xian_subway_stations_have_attributes WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getSubwayStationsInPolygon(@Param("polygonWkt") String polygonWkt);

    // 查询地铁站点 - 统计与多边形相交的地铁站点数量
    @Select("SELECT COUNT(*) FROM xian_subway_stations_have_attributes WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getSubwayStationCountInPolygon(@Param("polygonWkt") String polygonWkt);

    // 查询地铁站点 - 获取与多边形相交的地铁站点详细信息（如果有特定字段需要）
    @Select("SELECT station_name, point FROM xian_subway_stations_have_attributes WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Map<String, Object>> getSubwayStationDetailsInPolygon(@Param("polygonWkt") String polygonWkt);

    // ==================== 地铁站统计方法 ====================

    // 查询危险源 - 获取与多边形相交的危险源记录
    @Select("SELECT * FROM xian_dangerous_source WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Object> getDangerousSourceInPolygon(@Param("polygonWkt") String polygonWkt);

    // 查询危险源 - 统计与多边形相交的危险源数量
    @Select("SELECT COUNT(*) FROM xian_dangerous_source WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    Integer getDangerousSourceCountInPolygon(@Param("polygonWkt") String polygonWkt);

    // 查询危险源 - 获取与多边形相交的危险源详细信息
    @Select("SELECT name,  point FROM xian_dangerous_source WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490))")
    List<Map<String, Object>> getDangerousSourceDetailsInPolygon(@Param("polygonWkt") String polygonWkt);

    // 查询危险源 - 按类型统计与多边形相交的危险源数量
    @Select("SELECT name, COUNT(*) as count FROM xian_dangerous_source WHERE ST_Within(point, ST_GeomFromText(#{polygonWkt}, 4490)) GROUP BY source_type")
    List<Map<String, Object>> getDangerousSourceCountByTypeInPolygon(@Param("polygonWkt") String polygonWkt);

    /**
     * 批量插入影响区域数据
     * @param entityList 实体列表
     * @return 插入成功的记录数
     */
    @Insert({"<script>",
            "INSERT INTO xian_impact_in_area (disaster_id, second_disaster_id, people, national_road, heightway, street, dangerous_point, station, dangerous_point_pos, station_pos, district) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.disasterId}, #{item.secondDisasterId}, #{item.people}, #{item.nationalRoad}, #{item.heightway}, #{item.street}, #{item.dangerousPoint}, #{item.station}, #{item.dangerousPointPos}, #{item.stationPos}, #{item.district})",
            "</foreach>",
            "</script>"})
    int insertBatch(@Param("list") List<XianImpactInAreaEntity> entityList);
}
