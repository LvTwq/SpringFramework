package com.southwind.utils;

import java.time.Duration;
import java.util.Date;

import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

/**
 * @author 吕茂陈
 * @date 2022/02/27 15:13
 */
@UtilityClass
public class JwtUtil {

    /**
     * 这个秘钥是防止JWT被篡改的关键，随便写什么都好，但决不能泄露
     */
    private String secretKey = "whatever";

    /**
     * 过期时间目前设置成2天，这个配置随业务需求而定
     */
    private Duration expiration = Duration.ofHours(2);

    /**
     * 生成JWT
     *
     * @param userName
     * @return
     */
    public String generate(String userName) {
        // 过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());
        // 将userName放进JWT
        return Jwts.builder().setSubject(userName)
                // 设置JWT签发时间
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(expiryDate)
                // 设置加密算法和秘钥
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 解析JWT
     * @param token
     * @return
     */
    public Claims parse(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
