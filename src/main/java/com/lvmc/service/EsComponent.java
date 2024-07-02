

package com.lvmc.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
     * 添加批量数据
     */
    public <T> void addBulkData(List<T> dataList, String index) {
        BulkRequest request = new BulkRequest();
        try {
            dataList.forEach(data -> {
                request.add(
                        new IndexRequest().index(index).opType(DocWriteRequest.OpType.INDEX)
                                .id(IdUtil.getSnowflakeNextIdStr())
                                .source(JSONUtil.toJsonStr(data), XContentType.JSON).type("_doc"));
            });
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("批量添加数据失败", e);
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
                log.info("ESUtil deleteIndex: {}", index);
            }
        } catch (Exception e) {
            log.error("ESUtil deleteIndex error", e);
        }
    }
}
