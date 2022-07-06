package com.southwind.annotation;

import lombok.Data;

/**
 * @author 吕茂陈
 */
@Data
public class CreateUserAPI {
    private String name;
    private String identity;
    private String mobile;
    private int age;
}
