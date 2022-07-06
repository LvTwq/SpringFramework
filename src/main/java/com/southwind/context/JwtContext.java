package com.southwind.context;

import lombok.experimental.UtilityClass;

/**
 * @author 吕茂陈
 * @date 2022/02/27 15:45
 */
@UtilityClass
public class JwtContext {

    private ThreadLocal<String> user = new ThreadLocal<>();

    public void add(String userName) {
        user.set(userName);
    }

    public void remove() {
        user.remove();
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getCurrentUserName() {
        return user.get();
    }
}
