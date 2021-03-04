package com.onlinejudge.JudgeHost.exception.http;

public class HttpException extends RuntimeException {
    protected String code;

    protected Integer httpStatusCode;

    public String getCode() {
        return code;
    }
    public Integer getHttpStatusCode(){
        return httpStatusCode;
    }
}
