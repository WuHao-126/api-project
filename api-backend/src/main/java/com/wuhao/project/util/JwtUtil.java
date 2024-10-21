package com.wuhao.project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key"; // 更换为自己的密钥
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 天

    /**
     * 获取Token
     * @param userId
     * @return
     */
    public static String getToken(String userId ) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    /**
     * 创建token
     * @param claims
     * @param userId
     * @return
     */
    private static String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 验证token是否有效（包括时间）
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        final String userId = getUserId(token);
        return (StringUtils.isNotEmpty(userId) && !isTokenExpired(token));
    }

    /**
     * token是否过期
     * @param token
     * @return
     */
    private static boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     *从 JWT 中提取过期时间
     * @param token
     * @return
     */
    private static Date getExpiration(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }

    /**
     *  从 JWT 中提取用户Id
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
