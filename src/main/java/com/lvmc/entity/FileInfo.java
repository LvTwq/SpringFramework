package com.lvmc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 控制器--文件服务 数据传输实体
 *
 * @author 吕茂陈
 * @date 2022-08-30 11:48
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {


	/**
	 * tb_pkg_version_info 的id
	 */
	private String id;

	/**
	 * 该客户端适用的系统
	 */
	private String os;

	/**
	 * 系统支持 32/64 位
	 */
	private String bit;

	/**
	 * 下载链接
	 */
	@NotBlank(message = "downloadUrl 不能为空")
	private String downloadUrl;


	/**
	 * 类型：enclient/entools/endocument
	 */
	private String centerType;
	/**
	 * 版本
	 */
	private String version;

	/**
	 * 文件本身的md5值
	 */
	private String centerFileMd5;

	/**
	 * 状态 1 正常 2 已删除
	 */
	private String state;

	/**
	 * 文件名
	 */
	private String name;

	/**
	 * 描述
	 */
	private String des;

	private String createTime;

	private String updateTime;

}
