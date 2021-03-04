package com.onlinejudge.JudgeHost.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
** 
 * @description: token生成的工具类
 * @param:  * @param null 
 * @return:  
 * @author lyh15
 * @date: 2020/12/15 21:47
 */

public class TokenHelper {

    public static String generateAuthToken(String userId,String secretKey,Integer expiredIn){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Map<String, Date> map = calculateExpiredInfo(expiredIn);
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(map.get("expiredTime"))
                .withIssuedAt(map.get("now"))
                .sign(algorithm);
    }


    public static Map<String, Date> calculateExpiredInfo(Integer expiredIn) {
        Map<String, Date> map = new HashMap<>(2);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, expiredIn);
        map.put("now", now);
        map.put("expiredIn", calendar.getTime());
        return map;
    }

    public static Boolean checkAuthToken(String token,String userId,String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String uid = decodedJWT.getClaim("userId").asString();
            if(!userId.equals(uid)){
                return false;
            }
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
