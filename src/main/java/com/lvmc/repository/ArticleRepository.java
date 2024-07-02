/*


package com.lvmc.repository;

import com.lvmc.entity.ArticleEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

*/
/**
 * spring data 会根据方法名，自动生成实现类，方法名符合一定规则就行
 *
 * @author 吕茂陈
 * @description 泛型的参数分别是实体类型和主键类型
 * @date 2022/12/19 17:05
 *//*

@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleEntity, String> {

    */
/**
     * 精确查找 方法名规则：finByxxx
     *
     * @param title
     * @return 员工数据集
     *//*

    List<ArticleEntity> findByTitle(String title);

    */
/**
     * AND 语句查询
     *
     * @param title
     * @param age
     * @return 员工数据集
     *//*

    List<ArticleEntity> findByTitleAndAge(String title, Integer age);

    */
/**
     * OR 语句查询
     *
     * @param title
     * @param age
     * @return 员工数据集
     *//*

    List<ArticleEntity> findByTitleOrAge(String title, Integer age);

    */
/**
     * 分页查询员工信息
     *
     * @param name
     * @param page
     * @return 员工数据集 注：等同于下面代码 @Query("{\"bool\" : {\"must\" : {\"term\" : {\"name\" : \"?0\"}}}}")
     *//*

    Page<ArticleEntity> findByName(String name, Pageable page);

    */
/**
     * NOT 语句查询
     *
     * @param name
     * @param page
     * @return 员工数据集
     *//*

    Page<ArticleEntity> findByNameNot(String name, Pageable page);

    */
/**
     * LIKE 语句查询
     *
     * @param name
     * @param page
     * @return 员工数据集
     *//*

    Page<ArticleEntity> findByNameLike(String name, Pageable page);
}
*/
