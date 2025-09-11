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
    private List<Map<String, Object>> conditions; // 删除记录信息

}
