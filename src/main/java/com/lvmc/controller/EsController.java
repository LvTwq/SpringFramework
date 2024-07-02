

package com.lvmc.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lvmc.entity.AdminLog;
import com.lvmc.entity.Car;
import com.lvmc.service.EsComponent;
import com.lvmc.vo.ResTermsVo;
import com.lvmc.vo.RespBucketVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/12/19 17:06
 */
@Slf4j
@RestController
@RequestMapping("elk")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EsController {

    private final RestHighLevelClient restHighLevelClient;
    private final EsComponent esComponent;

    @GetMapping("admin")
    public List<AdminLog> allRes() throws IOException {
        //指定查询索引名
        SearchRequest searchRequest = new SearchRequest().indices("admin-2023.08");
        //构造查询对象
        //设置时间查询以及匹配查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("account", "lmc"))
                .filter(QueryBuilders.rangeQuery("mtime").gte("1685073234000").lte("1688004333027"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder)/*.sort("@mtime.keyword", SortOrder.DESC)*/;
        //开始点
        searchSourceBuilder.from(0);
        //指定单次查询个数
        searchSourceBuilder.size(100);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //拿到查询出的数据
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map<String, Object>> buckets = Arrays.stream(searchHits).map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
        List<AdminLog> list = new ArrayList<>();
        for (Map<String, Object> bucket : buckets) {
            AdminLog respLogVo = JSONUtil.toBean(JSONUtil.toJsonStr(bucket), AdminLog.class);
            list.add(respLogVo);
        }
        return list;
    }


    @PostMapping("admin")
    public void addAdminLog() {
        esComponent.addBulkData(List.of(new AdminLog()), "admin-2023.08");
    }


    @PostMapping("car")
    public void addCar() {
        List<Car> cars = List.of(
                new Car(10000, "red", "honda", "2014-10-28"),
                new Car(20000, "green", "honda", "2014-06-28"),
                new Car(30000, "red", "toyota", "2014-07-28"),
                new Car(15000, "blue", "honda", "2014-12-28"),
                new Car(18000, "red", "ford", "2014-11-28")
        );
        esComponent.addBulkData(cars, "test-agg-cars");
    }


    @GetMapping("car/terms")
    public ResTermsVo termsCar() throws IOException {
        SearchRequest searchRequest = new SearchRequest().indices("test-agg-cars");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("popular_colors")
                .field("color")
                .size(99)
                .subAggregation(AggregationBuilders.avg("avg_price").field("price"));
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        Aggregation popularColors = aggregationMap.get("popular_colors");
        JSONObject jsonObject = JSONUtil.parseObj(popularColors);
        return JSONUtil.toBean(jsonObject, ResTermsVo.class);
    }

    @GetMapping("car/terms2")
    public RespBucketVo termsCar2() throws IOException {
        SearchRequest searchRequest = new SearchRequest().indices("test-agg-cars");
        // 构建查询请求
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 设置查询条件
        FilterAggregationBuilder makeByAggregation = AggregationBuilders.filter("make_by",
                QueryBuilders.termQuery("make", "honda"));

        AvgAggregationBuilder avgPriceAggregation = AggregationBuilders.avg("avg_price")
                .field("price");

        makeByAggregation.subAggregation(avgPriceAggregation);
        sourceBuilder.aggregation(makeByAggregation);
        // 设置 size 为 0，因为你不需要实际的搜索结果
        sourceBuilder.size(0);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        Aggregation popularColors = aggregationMap.get("make_by");
        JSONObject jsonObject = JSONUtil.parseObj(popularColors);
        return JSONUtil.toBean(jsonObject, RespBucketVo.class);
    }

/*    @GetMapping("car/terms3")
    public RespBucketVo termsCar3() throws IOException {
        SearchRequest searchRequest = new SearchRequest().indices("test-agg-cars");
        // 构建查询请求
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建聚合
        FiltersAggregator.KeyedFilter infosFilter = new FiltersAggregator.KeyedFilter("infos", QueryBuilders.matchQuery("body", "info"));
        FiltersAggregator.KeyedFilter warningsFilter = new FiltersAggregator.KeyedFilter("warnings", QueryBuilders.matchQuery("body", "warning"));

        TermsAggregationBuilder messagesAggregation = AggregationBuilders.filters("messages", infosFilter, warningsFilter)
                .otherBucketKey("other_messages");

        sourceBuilder.aggregation(messagesAggregation);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        Aggregation popularColors = aggregationMap.get("make_by");
        JSONObject jsonObject = JSONUtil.parseObj(popularColors);
        return JSONUtil.toBean(jsonObject, RespBucketVo.class);
    }*/
}
