

package com.southwind.config;

import com.southwind.constant.DBSourceEnum;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/5/22 9:01
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DBSourceEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(DBSourceEnum dbSourceEnum) {
        CONTEXT_HOLDER.set(dbSourceEnum);
    }

    public static DBSourceEnum getDataSource() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}

