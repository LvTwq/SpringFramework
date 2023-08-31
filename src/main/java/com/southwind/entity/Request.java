package com.southwind.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/8/2 14:12
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Request {


    private boolean needsHandlingByHandler1;
    private boolean needsHandlingByHandler2;

}
