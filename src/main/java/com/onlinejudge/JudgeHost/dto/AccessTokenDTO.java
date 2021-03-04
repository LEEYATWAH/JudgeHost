package com.onlinejudge.JudgeHost.dto;

import com.onlinejudge.JudgeHost.validators.LoginValidated;

import javax.validation.constraints.NotNull;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */

@LoginValidated
public class AccessTokenDTO {
    @NotNull
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    @Override
    public String toString() {
        return "AuthorizationDTO{" +
                "token='" + accessToken + '\'' +
                '}';
    }
}
