package com.onlinejudge.JudgeHost.service;

import com.onlinejudge.JudgeHost.core.configuration.AuthorizationConfiguration;
import com.onlinejudge.JudgeHost.dto.AccessTokenDTO;
import com.onlinejudge.JudgeHost.dto.AuthorizationDTO;
import com.onlinejudge.JudgeHost.exception.http.ForbiddenException;
import com.onlinejudge.JudgeHost.utils.TokenHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description 权限、认证相关业务模块
 */
@Service
public class AuthorizationService {
    private final AuthorizationConfiguration authorizationConfiguration;

    public AuthorizationService(AuthorizationConfiguration authorizationConfiguration) {
        this.authorizationConfiguration = authorizationConfiguration;
    }

    // 获取判题服务器接口调用凭据
    public String getAccessToken(AuthorizationDTO authorizationDTO){
        String userId = authorizationDTO.getUserId();
        Boolean isPass = isUserSecretChecked(userId, authorizationDTO.getUserSecret());
        if (!isPass) {
            throw new ForbiddenException("A0003");
        }
        Integer expiredIn = authorizationConfiguration.getExpiredIn();
        String secretKey = authorizationConfiguration.getSecretKey();
        return TokenHelper.generateAuthToken(userId, secretKey, expiredIn);
    }

    public Boolean checkAccessToken(@Validated AccessTokenDTO accessTokenDTO) {
        String accessToken = accessTokenDTO.getAccessToken();
        String userId = authorizationConfiguration.getUserId();
        String secretKey = authorizationConfiguration.getSecretKey();
        return TokenHelper.checkAuthToken(accessToken, userId, secretKey);
    }

    /**
     * @param userId     用户名
     * @param userSecret 用户密码
     * @return Boolean 验证用户名或密码是否正确
     * @author LeeYatWah
     * @description 验证接口调用凭据合法性
     * 由于此服务器(judgehost)专门用来执行判题，个人觉得无需在额外搞一个数据库用来存储用户信息
     * 另外需要存储的数据也不多（可能就是用户名和密码了）
     * 规定的用户名和密码来自文件，你可以在配置文件中修改之（注意区分开发环境和生产环境）
     * @date
     */

    private Boolean isUserSecretChecked(String userId, String userSecret){
        return userId.equals(getUserId()) && userSecret.equals(getUserSecret());
    }

    private String getUserId() {
        return authorizationConfiguration.getUserId();
    }

    private String getUserSecret() {
        return authorizationConfiguration.getUserSecret();
    }

    public Integer getExpiredTime() {
        return authorizationConfiguration.getExpiredIn();
    }
}
