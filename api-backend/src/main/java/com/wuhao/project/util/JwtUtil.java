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

    /**
     * 创建token
     * @param claims
     * @return
     */
    public  static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
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
        return StringUtils.isNotEmpty(userId);
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
