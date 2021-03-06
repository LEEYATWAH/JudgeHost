package com.onlinejudge.JudgeHost.core.common;/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description 统一返回格式
 *   包括：错误码、错误信息、请求的地址
 */


public class UnifiedResponse {
    private final String code;
    private final String message;
    private final String request;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

    public UnifiedResponse(String code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }
}
