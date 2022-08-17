package com.southwind.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 *
 * @author 吕茂陈
 * @date 2022/02/28 09:06
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 12, message = "用户名长度为4-12位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max = 12, message = "密码长度为4-12位")
    private String password;

    private Boolean success;



}
