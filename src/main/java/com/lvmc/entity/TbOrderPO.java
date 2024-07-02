package com.lvmc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单表(TbOrder)表实体类
 *
 * @author 吕茂陈
 * @since 2023-11-23 10:14:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_order")
public class TbOrderPO {

    /**
    * 主键ID
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 订单编码
    */
    private String orderCode;

    /**
    * 订单状态
    */
    private Integer status;

    /**
    * 订单名称
    */
    private String name;

    /**
    * 价格
    */
    private Double price;

    /**
    * 删除标记，0未删除  1已删除
    */
    private Integer deleteFlag;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 创建人
    */
    private String createUserCode;

    /**
    * 更新人
    */
    private String updateUserCode;

    /**
    * 版本号
    */
    private Integer version;

    /**
    * 备注
    */
    private String remark;

}
