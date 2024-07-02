package com.lvmc.mybatis.encrypt;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:48
 */
@Getter
@Slf4j
@AllArgsConstructor
public class DefaultEncryptor implements IEncryptor {


    private String key;


    @Override
    public String encrypt(String content, Object metaObject) {
        return aesEncrypt(content, key);
    }

    @Override
    public String decrypt(String content, Object metaObject) {
        return aesDecrypt(content, key);
    }

    /**
     * 加密
     *
     * @param plain  明文
     * @param aesKey 加密key
     * @return
     */
    private String aesEncrypt(String plain, String aesKey) {
        if (StrUtil.isBlank(plain)) {
            return "";
        }
        try {
            SecretKeySpec key = generateSecretKeySpec(aesKey, StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plain.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return Hex.encodeHexString(ciphertextBytes);
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        //加密失败返回原文
        return plain;
    }

    /**
     * 解密
     *
     * @param content 密文
     * @param aesKey  解密key
     * @return
     */
    public String aesDecrypt(String content, String aesKey) {
        if (StrUtil.isEmpty(content)) {
            return "";
        }
        try {
            SecretKey key = generateSecretKeySpec(aesKey, StandardCharsets.US_ASCII);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Hex.decodeHex(content.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("decrypt error", e);
        }
        //解密失败返回原文
        return content;
    }

    /**
     * 生成SecretKeySpec
     *
     * @param key
     * @return
     */
    private SecretKeySpec generateSecretKeySpec(String key, Charset charset) {
        byte[] finalKey = new byte[16];
        int i = 0;
        for (byte b : key.getBytes(charset)) {
            finalKey[i++ % 16] ^= b;
        }
        return new SecretKeySpec(finalKey, "AES");
    }

}
