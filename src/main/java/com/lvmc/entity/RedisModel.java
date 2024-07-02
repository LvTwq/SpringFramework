

package com.lvmc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/4/6 15:35
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RedisModel {

    String key;
    String field;
    String value;
}
