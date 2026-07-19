package com.goodlab.server.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretValue;

    private static String KEY;

    @PostConstruct
    public void init() {
        if (secretValue == null || secretValue.length() < 32 || secretValue.contains("INSECURE_PLACEHOLDER")) {
            throw new IllegalStateException(
                "JWT密钥未配置或过短：请设置至少32个字符的环境变量 JWT_SECRET");
        }
        KEY = secretValue;
    }

    // 接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        Object idObj = claims.get("id");
        Integer id = null;
        if (idObj instanceof Long) {
            id = ((Long) idObj).intValue();
        } else if (idObj instanceof Integer) {
            id = (Integer) idObj;
        }
        
        return JWT.create()
                .withClaim("id", id)
                .withClaim("username", (String) claims.get("username"))
                .withClaim("isFirstLogin", (Boolean) claims.get("isFirstLogin"))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .sign(Algorithm.HMAC256(KEY));
    }

    // 接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", jwt.getClaim("id").asInt());
        claims.put("username", jwt.getClaim("username").asString());
        claims.put("isFirstLogin", jwt.getClaim("isFirstLogin").asBoolean());

        return claims;
    }

}
