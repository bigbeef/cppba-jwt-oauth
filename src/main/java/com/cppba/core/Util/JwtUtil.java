package com.cppba.core.Util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.cppba.core.bean.UserJwt;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtUtil {

    private static String secret = "com.cppba";

    /**
     * 创建一个jwt字符串
     * @param userJwt
     * @return
     * @throws Exception
     */
    public static String createJwt(UserJwt userJwt) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtString = objectMapper.writeValueAsString(userJwt);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("user",jwtString);
        return builder.sign(Algorithm.HMAC256(secret));
    }

    /**
     * 解码jwt token
     * @param token
     * @return
     * @throws Exception
     */
    public static UserJwt decodeJwt(String token) throws Exception{
        JWT decode = JWT.decode(token);
        Claim user = decode.getClaim("user");
        String userString = user.as(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        UserJwt userJwt = objectMapper.readValue(userString,UserJwt.class);
        return userJwt;
    }

    /**
     * 验证token签名是否合法
     * @param token
     * @return
     * @throws Exception
     */
    public static boolean verify(String token) throws Exception {
        try {
            JWT decode = JWT.decode(token);
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

