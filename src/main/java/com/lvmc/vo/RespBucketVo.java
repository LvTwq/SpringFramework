package com.lvmc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/14 16:00
 */
@Data
public class RespBucketVo {

    private Long docCount;
    private String name;
    private List<AggregationVo> aggregations;

    @Data
    public static class AggregationVo {
        private Double value;
        private String valueAsString;
        private String name;
    }
}
