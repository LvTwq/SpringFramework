

package com.lvmc.entity;


import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/6/26 15:30
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class AdminLog {

//    @JsonProperty("@timestamp")
    private String timestamp;

//    @JsonProperty("@mtime")
    private long mtime;
    private String account;
    private String name;
    private String session;
    private String sip;
    private String method;
    private String url;
    private String operation;
    private String code;
    private String info;
    private String moduleCode;
    private String moduleName;
    private String secondModuleCode;
    private String secondModuleName;
    private String groupIds;
    private String adminUserId;
    private String targetType;
    private String actorType;
    private String phone;
    private String device;


    public AdminLog() {
        this.timestamp = DateUtil.now();
        this.mtime = DateUtil.current();
        this.account = "lmc";
        this.name = "lmc";
        this.method = "post";
        this.url = "http://es.com";
        this.operation = "测试";
        this.device = "PC";
    }
}
