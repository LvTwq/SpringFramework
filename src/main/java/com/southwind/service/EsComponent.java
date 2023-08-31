

package com.southwind.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/6/29 9:28
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EsComponent {


    private final RestHighLevelClient restHighLevelClient;


    public void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest("your_index_name");
        request.settings(Settings.builder()
                // 设置分片数
                .put("index.number_of_shards", 1)
                // 设置副本数
                .put("index.number_of_replicas", 1)
        );
        request.mapping("your_type_name",
                "{ \"properties\": { \"field1\": { \"type\": \"text\" }, \"field2\": { \"type\": \"keyword\" } } }",
                XContentType.JSON);
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 索引创建请求是否得到了确认
        boolean acknowledged = createIndexResponse.isAcknowledged();
        // 索引的分片是否都得到了确认
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

    }

    /**
     * 时间标签统一化
     */
    public static String tsToString(long ts) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return fmt.withZone(DateTimeZone.UTC).print(ts);
    }

    public static String tsToStringZ8(long ts) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return fmt.withZone(DateTimeZone.forID("Asia/Shanghai")).print(ts);
    }

    public static String ts2YMZ8(long ts) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMM");
        return fmt.withZone(DateTimeZone.forID("Asia/Shanghai")).print(ts);
    }

    /**
     * 添加批量数据
     */
    public <T> void addBulkData(List<T> dataList) {
        BulkRequest request = new BulkRequest();
        try {
            dataList.forEach(data -> {
                JSONObject jsonObject = JSONUtil.parseObj(data);
                request.add(
                        new IndexRequest().index("admin-2023.08").opType(
                                        DocWriteRequest.OpType.INDEX)
                                .source(JSONUtil.toJsonStr(jsonObject), XContentType.JSON).type("_doc"));
            });
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     *
     * @param index 指数
     */
    public void deleteIndex(String index) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            AcknowledgedResponse deleteResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            if (deleteResponse.isAcknowledged()) {
                log.info("ESUtil deleteIndex " + index);
            }
        } catch (Exception e) {
            log.error("ESUtil deleteIndex error" + e.getMessage());
        }
    }
}
