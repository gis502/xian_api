package com.ruoyi.web.controller.system;


import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admins/graph")
public class GraphController {

    private final Neo4jClient neo4jClient;

    public GraphController(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @GetMapping("/getGraph")
    public List getFullGraph() {
        String query = "MATCH (n)-[r]->(m) RETURN n, r, m";
        return new ArrayList<>(neo4jClient.query(query)
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("source", record.get("n").asNode().asMap());
                    Relationship rel = record.get("r").asRelationship();
                    Map<String, Object> relMap = new HashMap<>(rel.asMap());
                    relMap.put("type", rel.type()); // 可选：添加关系类型
                    result.put("target", record.get("m").asNode().asMap());
                    result.put("value", relMap);
                    return result;
                })
                .all());
    }


    @GetMapping("/getGraphBy")
    public List getGraphBy(
            @RequestParam("eqid") String eqid,
            @RequestParam(value = "disasterType") String disasterType) {

        // 构建基础查询
        String query = "";
        // 你也可以根据 disasterType 修改 query，比如只查 earthquake 类型的图谱：
        // 建立灾害类型与对应ID字段的映射
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("earthquake", "earthquakeDisasterId");
        fieldMap.put("rain", "rainDisasterId");
        fieldMap.put("snow", "snowDisasterId");
        fieldMap.put("coldDamage", "coldDamageDisasterId");
        fieldMap.put("collapse", "collapseDisasterId");
        fieldMap.put("landslide", "landslideDisasterId");
        fieldMap.put("debrisFlow", "debrisFlowDisasterId");
        fieldMap.put("galeHail", "galeHailDisasterId");
        fieldMap.put("sandstorm", "sandstormDisasterId");
        fieldMap.put("drought", "droughtDisasterId");
        fieldMap.put("heatwave", "heatwaveDisasterId");
        fieldMap.put("wildfire", "wildfireDisasterId");
        fieldMap.put("bioDisaster", "bioDisasterId");
        fieldMap.put("safetyAccident", "safetyAccidentDisasterId");

        // 获取对应字段名，默认为空
        String idField = fieldMap.get(disasterType);
        if (idField != null) {
            query = String.format("MATCH (n{%s: '%s'})-[r]->(m) RETURN n, r, m", idField, eqid);
        } else {
            // 处理不支持的灾害类型，可根据需求返回空列表或抛出异常
            query = "";
        }

        return new ArrayList<>(neo4jClient.query(query)
                .fetchAs(Map.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("source", record.get("n").asNode().asMap());
                    Relationship rel = record.get("r").asRelationship();
                    Map<String, Object> relMap = new HashMap<>(rel.asMap());
                    relMap.put("type", rel.type());
                    result.put("target", record.get("m").asNode().asMap());
                    result.put("value", relMap);
                    return result;
                })
                .all());
    }


}



