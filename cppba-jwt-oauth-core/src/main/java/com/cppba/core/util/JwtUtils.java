package com.cppba.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.cppba.core.bean.JwtUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class JwtUtils {

    private final static String secret = "com.cppba";
    private final static long ttlMillis = 1800 * 1000;//有效期 30分钟
    private final static long newTokenMillis = 600*1000 ;//十分钟更换新的token

    /**
     * 创建一个jwt字符串
     * @param userJwt
     * @return
     * @throws Exception
     */
    public static String createJwt(JwtUser userJwt) throws Exception{
        Date now = new Date();
        long expMillis = now.getTime() + ttlMillis;
        Date exp = new Date(expMillis);
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtString = objectMapper.writeValueAsString(userJwt);
        JWTCreator.Builder builder = JWT.create();

        builder.withClaim("user",jwtString);//用户的信息
        builder.withIssuedAt(now);//授权时间
        builder.withExpiresAt(exp);//过期时间

        return builder.sign(Algorithm.HMAC256(secret));
    }

    /**
     * 解码jwt token
     * @param token
     * @return
     * @throws Exception
     */
    public static JwtUser decodeJwt(String token) throws Exception{
        JWT decode = JWT.decode(token);

        Claim user = decode.getClaim("user");
        String userString = user.as(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JwtUser userJwt = objectMapper.readValue(userString,JwtUser.class);
        return userJwt;
    }

    /**
     * 验证token是否合法
     * @param token
     * @return
     * @throws Exception
     */
    public static boolean verify(String token) throws Exception {
        try {
            //JWT decode = JWT.decode(token);
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            System.out.println("exception:"+e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 更换新的token，如果不需要更换，返回原token
     * @param token
     * @return
	 */
    public static String newToken(String token) throws Exception{
        JWT decode = JWT.decode(token);
        Claim user = decode.getClaim("user");
        String userString = user.as(String.class);

        Date issuedAt = decode.getIssuedAt();
        //不需要更换
        if(new Date().getTime() < issuedAt.getTime() + newTokenMillis ){
            return token;
        }

        Date now = new Date();
        long expMillis = now.getTime() + ttlMillis;
        Date exp = new Date(expMillis);
        JWTCreator.Builder builder = JWT.create();

        builder.withClaim("user",userString);//用户的信息
        builder.withIssuedAt(now);//授权时间
        builder.withExpiresAt(exp);//过期时间

        return builder.sign(Algorithm.HMAC256(secret));
    }

}

