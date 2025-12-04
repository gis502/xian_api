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
                                              @Param("fieldList") List<String> fieldList,
                                              @Param("primaryKey") List<String> primaryKey);

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
     * 根据DataManagementVO查询数据库中的待删除数据
     * @param tableName 表名
     * @param conditions 数据库记录
     */
    List<Map<String, Object>> selectDeletedRecords(
            @Param("tableName") String tableName,
            @Param("conditions") List<Map<String, Object>> conditions);

    /**
     * 根据DataManagementVO删除数据库表中对应的记录
     * @param tableName 表名
     * @param conditions 数据库记录
     */
    int deleteInfo(@Param("tableName") String tableName,
                   @Param("conditions") List<Map<String, Object>> conditions);
    /**
     * 根据DataManagementVO添加数据库表中对应的记录
     * @param tableName 表名
     * @param datas 待添加的数据
     */
    int addInfo(@Param("tableName") String tableName,
                @Param("datas") List<Map<String, Object>> datas,
                @Param("fieldTypes") Map<String, String> fieldTypes);

    /**
     * 根据DataManagementVO更新数据库表中对应的记录
     * 如果id存在则使用id进行匹配，否则使用主键进行匹配
     * @param tableName
     * @param idName
     * @param id
     * @param fieldNames
     * @param fieldValues
     * @return
     */
    int updateInfo1(@Param("tableName") String tableName,
                    @Param("idName") List<String> idName,
                    @Param("id") List<String> id,
                    @Param("fieldNames") List<String> fieldNames, // 字段名列表（无null值字段）
                    @Param("fieldValues") List<Object> fieldValues,// 字段值列表（与字段名顺序一致）
                    @Param("fieldTypes") Map<String, String> fieldTypes);
    /**
     * 根据DataManagementVO更新数据库表中对应的记录
     * 如果id不存在则使用旧数据进行匹配
     * @param tableName 表名
     */
    int updateInfo2(@Param("tableName") String tableName,
                    @Param("fieldNames1") List<String> fieldNames1,
                    @Param("fieldValues1") List<Object> fieldValues1,
                    @Param("fieldNames2") List<String> fieldNames2,
                    @Param("fieldValues2") List<Object> fieldValues2,
                    @Param("fieldTypes") Map<String, String> fieldTypes);

}
