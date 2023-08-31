package com.southwind.entity;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * @author 吕茂陈
 * @description
 * @date 2022/11/29 11:50
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LogCollectModel {

	/**
	 * account
	 */
	private String actorId;
	/**
	 * 设备 featurecode
	 */
	private String clientId;
	/**
	 * 浏览器
	 */
	private String clientUserAgentBrowser;
	/**
	 * 操作系统
	 */
	private String clientUserAgentOs;
	/**
	 * user-agent
	 */
	private String clientUserAgentRawUserAgent;
	/**
	 * 出口ip
	 */
	private String clientIpAddress;
	/**
	 * "Mobile" 或 "Computer"
	 */
	private String clientDevice;
	/**
	 * url
	 */
	private String targetAlternateId;
	/**
	 * 要直接展示的业务数据
	 */
	private String targetDisplayName;
	/**
	 * 请求体（具体业务数据，json 格式）
	 */
	private String targetDetail;
	/**
	 * "x-forwarded-for"
	 */
	private String requestIp;
	/**
	 * session-id
	 */
	private String requestSource;
	/**
	 * 事件
	 */
	private String eventEventType;
	/**
	 * 时间戳
	 */
	@Builder.Default
	private long eventPublished = DateUtil.current();
	/**
	 * LogConst.SUCCESS 或 LogConst.FAILURE
	 */
	private String eventOutcomeResult;
	/**
	 * 失败原因
	 */
	private String eventOutcomeReason;
	

}
