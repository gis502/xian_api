package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.vo.DataManagementVO;
import com.ruoyi.system.mapper.DataManagementMapper;
import com.ruoyi.system.service.IDataManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataManagementServiceImpl implements IDataManagementService {

    @Resource
    private DataManagementMapper dataManagementMapper;

    @Override
    public List<Map<String, String>> selectTable(String remark) {
        log.info("开始匹配关键词: '{}'", remark);
        List<Map<String, String>> Tables = dataManagementMapper.selectTable(remark);
        log.info("获取的数据库表 {}",Tables);
        return Tables;
    }

    @Override
    public Map<String,Object> queryInformation(DataManagementVO dataManagementVO){
        Map<String,Object> datas = new HashMap<>();
        int offset = (dataManagementVO.getPageNum()-1)*dataManagementVO.getPageSize();

        // 获取相关信息
        List<Map<String, String>> keyInfo = dataManagementMapper.selectKeyInfo(dataManagementVO.getTableName());
        List<Map<String, String>> primaryKey = dataManagementMapper.selectPrimaryKey(dataManagementVO.getTableName());
        Integer allPage = dataManagementMapper.selectAllPage(dataManagementVO.getTableName());


        List<String> fieldList = new ArrayList<>();
        setFieldList(fieldList, keyInfo);

        List<Map<String, Object>> InfoList = dataManagementMapper.selectTableList(dataManagementVO.getTableName(),dataManagementVO.getPageSize(),offset,dataManagementVO.getQueryInfo(), fieldList);

        datas.put("allPage",allPage);
        datas.put("primaryKey",primaryKey);
        datas.put("keyInfo",keyInfo);
        datas.put("tableInfo",InfoList);
        return datas;
    }

    @Override
    public boolean deleteInformation(DataManagementVO dataManagementVO){
        dataManagementMapper.deleteInfo(dataManagementVO.getTableName(),dataManagementVO.getConditions());
        return true;
    }

    /**
     * 设置字段列表
     * @param fieldList 字段列表
     * @param keyInfo 字段信息
     */
    private void setFieldList(List<String> fieldList, List<Map<String, String>> keyInfo) {
        for(Map<String,String> map : keyInfo){
            if(map.get("type").toUpperCase().equals("VARCHAR") || map.get("type").toUpperCase().equals("CHAR") || map.get("type").toUpperCase().equals("TEXT")){
                String key = map.get("key");
                fieldList.add(key);
            }
        }
    }
}
