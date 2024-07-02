package com.lvmc.vo;

import cn.hutool.json.JSONArray;
import lombok.Data;

import java.util.List;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/14 11:51
 */
@Data
public class ResTermsVo {

    private String name;

    private List<Bucket> buckets;


    @Data
    public static class Bucket {
        private String key;
        private Long docCountError;
        private Long docCount;
        private String keyAsString;
        private Boolean keyed;
        private JSONArray aggregations;
    }
}
