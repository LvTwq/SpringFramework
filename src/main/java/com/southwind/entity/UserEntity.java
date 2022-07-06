package com.southwind.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author 吕茂陈
 * @date 2022/02/28 09:10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class UserEntity extends BaseEntity {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
}
