package com.onlinejudge.JudgeHost.core.authorization;

import com.onlinejudge.JudgeHost.core.configuration.AuthorizationConfiguration;
import com.onlinejudge.JudgeHost.exception.http.ForbiddenException;
import com.onlinejudge.JudgeHost.utils.TokenHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description 权限验证器类
 */

@EnableConfigurationProperties(AuthorizationConfiguration.class)
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthorizationConfiguration authorizationConfiguration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        //  如果不是反射到方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 寻找 AuthorizationRequired注解
        AuthorizationRequired authorization = method.getAnnotation(AuthorizationRequired.class);
        if (authorization != null) {
            // 获取、解码accessToken
            String accessToken = request.getHeader("accessToken");
            if (StringUtils.isEmpty(accessToken)) {
                throw new ForbiddenException("A0004");
            }
            String uid = authorizationConfiguration.getUserId();
            String secret = authorizationConfiguration.getSecretKey();
            Boolean isPass = TokenHelper.checkAuthToken(accessToken, uid, secret);
            if(!isPass){
                throw new ForbiddenException("A0004");
            }
        }
        return true;
    }

}
