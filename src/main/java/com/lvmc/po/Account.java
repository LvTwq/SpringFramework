package com.lvmc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 吕茂陈
 */
@Data
@Builder
@TableName("account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;

    private String password;
    private String perms;
    private String role;
}
