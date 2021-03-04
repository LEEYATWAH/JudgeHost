package com.onlinejudge.JudgeHost.exception.http;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String code){
        this.httpStatusCode = 403;
        this.code = code;
    }
}
