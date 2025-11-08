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
                @Param("datas") List<Map<String, Object>> datas);
    /**
     * 根据DataManagementVO更新数据库表中对应的记录
     * 如果id存在则使用id进行匹配，否则使用主键进行匹配
     * @param tableName 表名
     * @param idName id字段名
     * @param id id字段值
     * @param newData 待更新的数据
     */
    int updateInfo1(@Param("tableName") String tableName,
                   @Param("idName") List<String> idName,
                   @Param("id") List<String> id,
                   @Param("newData") List<Map<String, Object>> newData);
    /**
     * 根据DataManagementVO更新数据库表中对应的记录
     * 如果id不存在则使用旧数据进行匹配
     * @param tableName 表名
     * @param oldData 旧数据
     * @param newData 待更新的数据
     */
    int updateInfo2(@Param("tableName") String tableName,
                   @Param("oldData") List<Map<String, Object>> oldData,
                   @Param("newData") List<Map<String, Object>> newData);

}
