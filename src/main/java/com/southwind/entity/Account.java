package com.southwind.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author 吕茂陈
 */
@Data
@Builder
@ToString
@TableName("account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Integer id;
    private String username;
    private String password;
    private String perms;
    private String role;
}
