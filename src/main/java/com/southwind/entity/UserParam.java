package com.southwind.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 用户相关参数
 *
 * @author 吕茂陈
 * @date 2022/02/28 09:08
 */
@Data
public class UserParam {
    @NotNull(message = "用户id不能为空", groups = Update.class)
    private Long id;

    @NotBlank(message = "用户名不能为空", groups = CreateUser.class)
    @Length(min = 4, max = 12, message = "用户名长度为4-12位", groups = CreateUser.class)
    private String username;

    private List<Long> roleIds;

    private List<Long> companyIds;

    public interface Update {
    }

    public interface CreateUser {
    }
}
