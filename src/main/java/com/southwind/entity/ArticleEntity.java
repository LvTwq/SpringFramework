

package com.southwind.entity;

import cn.hutool.core.date.DateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/12/19 17:04
 */
@Builder
//@Document(indexName = "article")
@Data
public class ArticleEntity {

    @Id
    private String id;

//    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String title;

//    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String name;

//    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String content;

    private Integer userId;

    private Integer age;

    @Default
    private Date createTime = DateTime.now();
}
