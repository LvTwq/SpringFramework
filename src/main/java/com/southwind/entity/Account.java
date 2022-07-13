package com.southwind.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author 吕茂陈
 */
@Data
@Builder
@TableName("account")
public class Account {

    private Integer id;
    private String username;
    private String password;
    private String perms;
    private String role;
}
