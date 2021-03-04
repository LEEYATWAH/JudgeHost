package com.onlinejudge.JudgeHost.core;

public class UnifiedResponse {
    private final String code;
    private final String message;
    private final String request;

    public String getMessage(){
        return message;
    }
    public String getCode() {
        return code;
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
