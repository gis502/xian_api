package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataManagementVO implements Serializable {
    private String tableName; //数据库表名
    private Integer pageSize; // 一页数量
    private Integer pageNum; // 页码
    private String queryInfo; // 模糊匹配字段
    private List<Conditions> conditions; // 删除记录信息
    public DataManagementVO(){
        this.conditions = new ArrayList<>();
    }

    @Data
    public static class Conditions{
        private Integer id; // 数据库表的记录条数
        private String primaryKey; // 数据库主键
    }
}
