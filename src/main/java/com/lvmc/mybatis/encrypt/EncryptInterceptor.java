package com.lvmc.mybatis.encrypt;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:47
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class})})
@Slf4j
public class EncryptInterceptor implements Interceptor {

    private final Map<Class<? extends IEncryptor>, IEncryptor> collectors = new HashMap<>();

    private IEncryptor encryptor;

    /**
     * 变量占位符正则
     */
    private static final Pattern PARAM_PAIRS_RE = Pattern
            .compile("#\\{ew\\.paramNameValuePairs\\.(" + Constants.WRAPPER_PARAM + "\\d+)\\}");

    public EncryptInterceptor(IEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        String mapperClass = statement.getId().substring(0, statement.getId().lastIndexOf("."));
        if (!MybatisPlusTableCacheStart.cacheColumnEncrypt.containsKey(mapperClass)) {
            return invocation.proceed();
        }
        Object parameter = args[1];
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        BoundSql sql = statement.getBoundSql(parameter);

        Configuration configuration = statement.getConfiguration();
        MetaObject metaObject = configuration.newMetaObject(parameter);


        if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT) {
            beforeUpdate(statement, parameter, metaObject);
            return invocation.proceed();
        }
        if (sqlCommandType == SqlCommandType.SELECT) {
            // 入参加密
            List<String> columnList = MybatisPlusTableCacheStart.cacheColumnEncrypt.get(mapperClass);
            encrypt(sql.getParameterObject(), columnList, metaObject);
            Object returnValue = invocation.proceed();
            // 结果解密
            if (returnValue instanceof ArrayList<?>) {
                List<?> list = (ArrayList<?>) returnValue;
                for (Object val : list) {
                    // 通过反射获取修改参数
                    decrypt(val, metaObject);
                }
            } else {
                decrypt(returnValue, metaObject);
            }
            return returnValue;
        }
        return invocation.proceed();
    }


    private void beforeUpdate(MappedStatement statement, Object parameter, MetaObject metaObject) throws IllegalAccessException {
        // 通过MybatisPlus自带API（save、insert等）新增数据库时
        if (!(parameter instanceof Map)) {
            encryptObject(parameter, metaObject);
            return;
        }
        Map paramMap = (Map) parameter;
        Object param;
        // 通过MybatisPlus自带API（update、updateById等）修改数据库时
        if (paramMap.containsKey(Constants.ENTITY) && null != (param = paramMap.get(Constants.ENTITY))) {
            encryptObject(param, metaObject);
            return;
        }
        // 通过在mapper.xml中自定义API修改数据库时
        if (paramMap.containsKey("entity") && null != (param = paramMap.get("entity"))) {
            encryptObject(param, metaObject);
            return;
        }
        // 通过UpdateWrapper、LambdaUpdateWrapper修改数据库时
        if (paramMap.containsKey(Constants.WRAPPER) && null != (param = paramMap.get(Constants.WRAPPER))
                && param instanceof Update && param instanceof AbstractWrapper) {
            Class<?> entityClass = statement.getParameterMap().getType();
            encryptWrapper(entityClass, param, metaObject);
        }

    }

    private void encryptWrapper(Class<?> entityClass, Object ewParam, MetaObject metaObject) {
        AbstractWrapper updateWrapper = (AbstractWrapper) ewParam;
        String sqlSet = updateWrapper.getSqlSet();
        String[] elArr = StringUtils.split(sqlSet, ",");
        Map<String, String> propMap = new HashMap<>(elArr.length);
        Arrays.stream(elArr).forEach(el -> {
            String[] elPart = StringUtils.split(el, "=");
            propMap.put(elPart[0], elPart[1]);
        });

        //取出parameterType的类
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
            if (null != fieldEncrypt) {
                String el = propMap.get(field.getName());
                if (null != el) {
                    Matcher matcher = PARAM_PAIRS_RE.matcher(el);
                    if (matcher.matches()) {
                        String valueKey = matcher.group(1);
                        Object value = updateWrapper.getParamNameValuePairs().get(valueKey);

                        IEncryptor encryptorUsed = getEncryptor(fieldEncrypt.encryptor());
                        if (null != encryptorUsed) {
                            updateWrapper.getParamNameValuePairs().put(valueKey, encryptorUsed.encrypt(value.toString(), metaObject));
                        }
                    }
                }
            }
        }

    }

    public <T> T encrypt(T parameterObject, List<String> columnList, MetaObject metaObject) throws IllegalAccessException {
        if (parameterObject == null) {
            return null;
        }
        // 多个参数
        if (parameterObject instanceof Map) {
            Map paramMap = (Map) parameterObject;
            Set keySet = paramMap.keySet();
            for (Object key : keySet) {
                if (key instanceof String && ((String) key).startsWith("param")) {
                    Object o = paramMap.get(key);
                    if (o instanceof QueryWrapper || o instanceof LambdaQueryWrapper) {
                        AbstractWrapper q = (AbstractWrapper) o;
                        MergeSegments expression = q.getExpression();
                        if (null != expression) {
                            String sqlSegment = expression.getSqlSegment().toUpperCase();
                            ISqlSegment keyWord = expression.getNormal().stream().filter(obj -> SqlKeyword.class.isInstance(obj))
                                    .findFirst().orElse(null);
                            // 如果不是like 或者 not like 或者in 对参数进行加密处理
                            if (null != keyWord &&
                                    !StringUtils.equalsAny(keyWord.getSqlSegment(), SqlKeyword.LIKE.getSqlSegment(), SqlKeyword.NOT_LIKE.getSqlSegment(), SqlKeyword.IN.getSqlSegment())) {
                                columnList.forEach(s -> {
                                    if (sqlSegment.contains(s.toUpperCase())) {
                                        // 获取需要加密字段的值
                                        String pKey = sqlSegment.split(s.toUpperCase())[1]
                                                .split("PARAMNAMEVALUEPAIRS.")[1].split("}")[0];
                                        // 覆盖为加密后内容
                                        IEncryptor encryptorUsed = getEncryptor(MybatisPlusTableCacheStart.cacheColumnEncryptor.get(s));
                                        q.getParamNameValuePairs().put(pKey,
                                                encryptorUsed.encrypt(q.getParamNameValuePairs().get(pKey).toString(), metaObject));
                                    }
                                });
                            }

                        }
                    } else if (o != null) {
                        encryptObject(o, metaObject);
                    }
                }
            }
        }
        return parameterObject;
    }

    public <T> T decrypt(T result, MetaObject metaObject) throws IllegalAccessException {
        //取出resultType的类
        if (result == null) {
            return null;
        }
        Class<?> resultClass = result.getClass();
        Field[] declaredFields = resultClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //取出所有被FieldEncrypt注解的字段
            FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
            if (fieldEncrypt != null) {
                field.setAccessible(true);
                Object object = field.get(result);
                //只支持String的解密
                if (object instanceof String) {
                    String value = (String) object;
                    //对注解的字段进行逐一解密
                    IEncryptor encryptorUsed = getEncryptor(fieldEncrypt.encryptor());
                    if (encryptorUsed != null && encryptorUsed.supportDecrypt()) {
                        field.set(result, encryptorUsed.decrypt(value, metaObject));
                    }
                }
            }
        }
        return result;
    }

    private void encryptObject(Object parameterObject, MetaObject metaObject) throws IllegalAccessException {
        Field[] declaredFields = parameterObject.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
            if (null != fieldEncrypt) {
                field.setAccessible(true);
                Object object = field.get(parameterObject);
                if (object instanceof String) {
                    String value = (String) object;
                    //加密
                    IEncryptor encryptorUsed = getEncryptor(fieldEncrypt.encryptor());
                    if (null != encryptorUsed) {
                        field.set(parameterObject, encryptorUsed.encrypt(value, metaObject));
                    }
                }
            }
        }
    }

    private IEncryptor getEncryptor(Class<? extends IEncryptor> encryptorClass) {
        IEncryptor encryptorUse = encryptor;
        if (encryptorClass != encryptor.getClass()) {
            return collectors.computeIfAbsent(encryptorClass, k -> {
                try {
                    return k.getDeclaredConstructor().newInstance();
                } catch (Exception exception) {
                    log.error("fieldEncrypt encryptor newInstance error", exception);
                }
                return null;
            });
        }
        return encryptorUse;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
