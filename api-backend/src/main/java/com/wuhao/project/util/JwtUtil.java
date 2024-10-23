package com.wuhao.project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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
        return "";
    }

    /**
     * 创建token
     * @param claims
     * @return
     */
    public  static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
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
        final Claims claims = getClaims(token);
        if (claims == null)
            return false;
        String userId = claims.getSubject();
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
    public static Claims getClaims(String token) {
         return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
