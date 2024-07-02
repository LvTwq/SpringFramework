package com.lvmc.mybatis.encrypt;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:48
 */
public interface IEncryptor {

    /**
     * 加密
     *
     * @param content
     * @return
     */
    String encrypt(String content, Object metaObject);

    /**
     * 解密
     *
     * @param content
     * @return
     */
    String decrypt(String content, Object metaObject);

    /**
     * @description: 是否支持解密操作，针对部分加密处理只需要加密，不需要解密 如md5 sha256 等
     * @author: rui.ma
     * @date: 2023-7-28 11:41
     * @param:
     * @return: boolean
     **/
    default boolean supportDecrypt() {
        return true;
    }
}
