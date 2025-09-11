package com.ruoyi.system.service;


import com.ruoyi.system.domain.vo.DataManagementVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDataManagementService {

    //根据传来的字符串对表的注释进行模糊搜索
    List<Map<String, String>> selectTable(String remark);

    // 根据表名获取对应的数据内容
    Map<String,Object> queryInformation(DataManagementVO dataManagementVO);

    // 根据传来的信息删除数据库表中对应的记录
    boolean deleteInformation(DataManagementVO dataManagementVO);

}
