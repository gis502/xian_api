package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.entity.XianDataBackup;
import com.ruoyi.system.domain.vo.DataManagementVO;
import com.ruoyi.system.mapper.DataManagementMapper;
import com.ruoyi.system.mapper.XianDataBackMapper;
import com.ruoyi.system.service.IDataManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataManagementServiceImpl implements IDataManagementService {

    @Resource
    private DataManagementMapper dataManagementMapper;

    @Resource
    private XianDataBackMapper xianDataBackupMapper;

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
        List<String> primaryKey = generatorKey(dataManagementMapper.selectPrimaryKey(dataManagementVO.getTableName()));
        Integer allPage = dataManagementMapper.selectAllPage(dataManagementVO.getTableName());


        List<String> fieldList = new ArrayList<>();
        setFieldList(fieldList, keyInfo);

        List<Map<String, Object>> InfoList = dataManagementMapper.selectTableList(dataManagementVO.getTableName(),dataManagementVO.getPageSize(),offset,dataManagementVO.getQueryInfo(), fieldList, primaryKey);

        datas.put("allPage",Math.ceil((allPage * 1.0) / dataManagementVO.getPageSize()));
        datas.put("primaryKey",primaryKey);
        datas.put("keyInfo",keyInfo);
        datas.put("tableInfo",InfoList);
        return datas;
    }

    @Override
    public boolean deleteInformation(DataManagementVO dataManagementVO){
        String tableName = dataManagementVO.getTableName();
        log.info("开始删除 {} 表的数据",tableName);
        List<Map<String, Object> > conditions = dataManagementVO.getConditions();
        // 先查询待删除的数据
        List<Map<String, Object>> oldData = dataManagementMapper.selectDeletedRecords(tableName,conditions);
        // 将待删除的记录存入操作记录表
        dataManagementVO.setOldData(oldData);
        OperationRecord("D",dataManagementVO);
        try {
            int affectedRows = dataManagementMapper.deleteInfo(tableName, conditions);
            if (affectedRows <= 0) {
                throw new BaseException("未找到匹配条件的记录，删除失败");
            }
            return true;
        } catch (Exception e){
            log.error("删除失败", e);
            throw new BaseException("删除失败：" + e.getMessage());
        }
    }

    @Override
    public boolean addInformation(DataManagementVO dataManagementVO){
        log.info("开始添加 {} 表的数据",dataManagementVO.getTableName());
        // 过滤null值：只保留value不为null的字段
        for (int i = 0; i < dataManagementVO.getDatas().size(); i++) {
            dataManagementVO.getDatas().set(i, removeNull(dataManagementVO.getDatas().get(i)));
        }

        OperationRecord("A",dataManagementVO);
        try {
            int addRows = dataManagementMapper.addInfo(dataManagementVO.getTableName(), dataManagementVO.getDatas(), dataManagementVO.getFieldTypes());
            if (addRows <= 0) {
                throw new BaseException("添加失败");
            }
            return true;
        } catch (Exception e){
            log.error("添加失败", e);
            throw new BaseException("添加失败：" + e.getMessage());
        }

    }

    @Override
    public boolean updateInformation(DataManagementVO dataManagementVO){
        // 过滤null值：只保留value不为null的字段
        dataManagementVO.getNewData().set(0, removeNull(dataManagementVO.getNewData().get(0)));
        if(!dataManagementVO.getOldData().isEmpty()) {
            dataManagementVO.getOldData().set(0, removeNull(dataManagementVO.getOldData().get(0)));
        }
        log.info("开始修改 {} 表的数据",dataManagementVO.getTableName());

        OperationRecord("U",dataManagementVO);

        try {
            List<String> fieldNames1 = new ArrayList<>(dataManagementVO.getNewData().get(0).keySet());
            List<Object> fieldValues1 = new ArrayList<>(dataManagementVO.getNewData().get(0).values());

            // 对传入数据进行判断
            if(dataManagementVO.getIdName()!=null && !dataManagementVO.getIdName().isEmpty()){
                // 如果存在主键，则使用主键进行匹配
                int updateRows = dataManagementMapper.updateInfo1(dataManagementVO.getTableName(), dataManagementVO.getIdName(), dataManagementVO.getId(), fieldNames1, fieldValues1, dataManagementVO.getFieldTypes());
                if (updateRows <= 0) {
                    throw new BaseException("修改失败");
                }
            }else {
                List<String> fieldNames2 = new ArrayList<>(dataManagementVO.getOldData().get(0).keySet());
                List<Object> fieldValues2 = new ArrayList<>(dataManagementVO.getOldData().get(0).values());
                // 如果不存在主键，则使用旧数据进行匹配
                int updateRows = dataManagementMapper.updateInfo2(dataManagementVO.getTableName(), fieldNames1, fieldValues1, fieldNames2, fieldValues2, dataManagementVO.getFieldTypes());
                if (updateRows <= 0) {
                    throw new BaseException("修改失败");
                }
            }
           return true;
        } catch (Exception e){
            log.error("修改失败", e);
            throw new BaseException("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除null
     * @param newData
     * @return
     */
    private Map<String, Object> removeNull(Map<String, Object> newData) {
        Map<String, Object> updateMap = new HashMap<>();
        if (newData != null && !newData.isEmpty()) {
            for (Map.Entry<String, Object> entry : newData.entrySet()) {
                if (entry.getValue() != null) {
                    updateMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return updateMap;
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

    /**
     * 构建主键列表
     * @param primaryKeys sql查询的主键列表
     * @return 主键列表
     */
    private List<String> generatorKey(List<Map<String, String>> primaryKeys){
        List<String> primaryKeyList = new ArrayList<>();
        for(Map<String, String> map : primaryKeys){
            String key = map.get("primarykey");
            primaryKeyList.add(key);
        }
        return primaryKeyList;
    }

    /**
     * 操作记录
     * @param operation 操作类型
     * @param dataManagementVO 数据管理对象
     */
    private void OperationRecord(String operation, DataManagementVO dataManagementVO){
        XianDataBackup xianDataBackup = new XianDataBackup();
        xianDataBackup.setType(operation);
        xianDataBackup.setData(JSON.toJSONString(dataManagementVO));
        xianDataBackup.setTime(LocalDateTime.now());
        xianDataBackup.setIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        xianDataBackupMapper.insert(xianDataBackup);
    }
}
