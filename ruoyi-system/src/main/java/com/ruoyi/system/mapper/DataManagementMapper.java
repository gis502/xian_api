package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface DataManagementMapper {

    /**
     * 根据String名查询数据库表
     * @param remark 匹配字段
     */
    List<Map<String, String>> selectTable(@Param("remark") String remark);

    /**
     * 根据DataManagementVO查询数据库表记录
     * @param tableName 表名
     * @param pageSize 一页数量
     * @param offset 偏移量
     * @param queryInfo 模糊匹配字段
     */
    List<Map<String, Object>> selectTableList(@Param("tableName") String tableName,
                                              @Param("pageSize") Integer pageSize,
                                              @Param("offset") Integer offset,
                                              @Param("queryInfo") String queryInfo,
                                              @Param("fieldList") List<String> fieldList);

    /**
     * 根据DataManagementVO查询数据库表每一个字段对应的注释
     * @param tableName 表名
     */
    List<Map<String, String>> selectKeyInfo(@Param("tableName") String tableName);

    /**
     * 根据DataManagementVO查询数据库表对应的主键
     * @param tableName 表名
     */
    List<Map<String, String>> selectPrimaryKey(@Param("tableName") String tableName);

    /**
     * 根据DataManagementVO查询数据库表对应的总记录
     * @param tableName 表名
     */
    Integer selectAllPage(@Param("tableName") String tableName);

    /**
     * 根据DataManagementVO删除数据库表中对应的记录
     * @param tableName 表名
     * @param id 数据库记录索引
     * @param primaryKey 数据库表的主键
     */

}
