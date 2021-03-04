package com.onlinejudge.JudgeHost.dto;
/**
 * @return $
 * @param $
 * @author LeeYatWah
 * @date $ $
 * @description
 */

import com.onlinejudge.JudgeHost.core.enumerations.JudgeResultEnum;

public class SingleJudgeResultDTO {
    private String realTimeCost;
    private String memoryCost;
    private String cpuTimeCost;
    private Integer condition;
    private String stdinPath;
    private String stdoutPath;
    private String stderrPath;
    private String message;

    public void setMessageWithCondition()  {
        JudgeResultEnum type = JudgeResultEnum.toJudgeResultType(this.condition);
        this.message = type.getMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRealTimeCost() {
        return realTimeCost;
    }

    public void setRealTimeCost(String realTimeCost) {
        this.realTimeCost = realTimeCost;
    }

    public String getMemoryCost() {
        return memoryCost;
    }

    public void setMemoryCost(String memoryCost) {
        this.memoryCost = memoryCost;
    }

    public String getCpuTimeCost() {
        return cpuTimeCost;
    }

    public void setCpuTimeCost(String cpuTimeCost) {
        this.cpuTimeCost = cpuTimeCost;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getStdinPath() {
        return stdinPath;
    }

    public void setStdinPath(String stdinPath) {
        this.stdinPath = stdinPath;
    }

    public String getStdoutPath() {
        return stdoutPath;
    }

    public void setStdoutPath(String stdoutPath) {
        this.stdoutPath = stdoutPath;
    }

    public String getStderrPath() {
        return stderrPath;
    }

    public void setStderrPath(String stderrPath) {
        this.stderrPath = stderrPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageWithCondition(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "SingleJudgeResultDTO{" +
                "realTimeCost='" + realTimeCost + '\'' +
                ", memoryCost='" + memoryCost + '\'' +
                ", cpuTimeCost='" + cpuTimeCost + '\'' +
                ", condition=" + condition +
                ", stdinPath='" + stdinPath + '\'' +
                ", stdoutPath='" + stdoutPath + '\'' +
                ", stderrPath='" + stderrPath + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}