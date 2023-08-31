package com.southwind.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/12 15:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResultVo {

    private int exitValue;
    private String result;
}
