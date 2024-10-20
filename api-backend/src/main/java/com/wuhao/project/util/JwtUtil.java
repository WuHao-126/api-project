package com.wuhao.project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class JwtUtil {

    // 密钥，用于签名和解密
    private static final String SECRET_KEY = "wuhao";

    // 生成JWT
    public static String getToken(Long userId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000; // 设置过期时间为1小时
        Date exp = new Date(expMillis);
        return Jwts.builder()
                .setSubject(userId.toString()) // 将用户名存入JWT
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(exp) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 设置签名算法和密钥
                .compact();
    }
    public static Claims parseJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 签名异常处理
            System.out.println("Invalid JWT signature");
            return null;
        } // 可以根据需要添加更多的异常处理
    }
    public static void main(String[] args) {
        String token = getToken(1000001l);
        System.out.println(token);

        Claims claims = parseJwt(token);
        String subject = claims.getSubject();
        System.out.println(subject);
    }
}
