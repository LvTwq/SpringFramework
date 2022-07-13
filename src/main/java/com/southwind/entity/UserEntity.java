package com.southwind.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author 吕茂陈
 * @date 2022/02/28 09:10
 */
@Data
public class UserEntity {
    @Id
    private Long id;
    private String name;

    public UserEntity() {
    }

    public UserEntity(String name) {
        this.name = name;
    }
}
