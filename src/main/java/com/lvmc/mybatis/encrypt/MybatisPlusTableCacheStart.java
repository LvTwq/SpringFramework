package com.lvmc.mybatis.encrypt;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:55
 */
@Slf4j
@Component
public class MybatisPlusTableCacheStart implements ApplicationListener<ContextRefreshedEvent> {

    public static Map<String, List<String>> cacheColumnEncrypt = new HashMap<>(32);

    public static Map<String, Class<? extends IEncryptor>> cacheColumnEncryptor = new HashMap<>(32);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (null == event.getApplicationContext().getParent()) {
            ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
            resolverUtil.find(new ResolverUtil.IsA(Mapper.class), "com.lvmc");
            Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
            for (Class<?> mapperClass : mapperSet) {
                Class<?> modelClass = ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
                if (null != modelClass) {
                    TableName tableName = modelClass.getAnnotation(TableName.class);
                    if (null != tableName) {
                        Field[] declaredFields = modelClass.getDeclaredFields();
                        if (ArrayUtils.isNotEmpty(declaredFields)) {
                            for (Field field : declaredFields) {
                                // 取出所有被FieldEncrypt注解的字段
                                FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                                if (fieldEncrypt != null) {
                                    String column = field.getName();
                                    /* 开启字段下划线申明 */
                                    column = StringUtils.camelToUnderline(column);
                                    TableField tableField = field.getAnnotation(TableField.class);
                                    if (null != tableField) {
                                        column = tableField.value();
                                    }
                                    cacheColumnEncryptor.put(column, fieldEncrypt.encryptor());
                                    List<String> columnList = cacheColumnEncrypt.getOrDefault(mapperClass.getName(), new ArrayList<>());
                                    columnList.add(column);
                                    cacheColumnEncrypt.put(mapperClass.getName(), columnList);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
