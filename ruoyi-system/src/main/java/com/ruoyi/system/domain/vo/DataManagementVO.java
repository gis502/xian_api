package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DataManagementVO implements Serializable {
    private String tableName; //数据库表名
    private Integer pageSize; // 一页数量
    private Integer pageNum; // 页码
    private String queryInfo; // 模糊匹配字段
    private List<String> idName; // 主键字段名(删除时使用)
    private List<String> id; // 主键字段值(删除时使用)
    private List<Map<String, Object>> conditions; // 删除数据列表
    private List<Map<String, Object>> datas; // 新增数据列表
    private List<Map<String, Object>> newData; // 修改数据新列表
    private List<Map<String, Object>> oldData; // 修改数据旧列表（数据库表没有主键时通过旧数据匹配）
    private Map<String, String> fieldTypes;         // 字敦类型
}
