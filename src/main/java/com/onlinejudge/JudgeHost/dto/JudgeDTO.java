package com.onlinejudge.JudgeHost.dto;
import com.onlinejudge.JudgeHost.core.enumerations.JudgeConfigDefaultEnum;
import com.onlinejudge.JudgeHost.core.enumerations.JudgePreferenceEnum;
import com.onlinejudge.JudgeHost.exception.http.NotFoundException;
import com.onlinejudge.JudgeHost.validators.LanguageTypeAccepted;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $2021/3/1
 * @description 判题数据传输对象
 */



@LanguageTypeAccepted
public class JudgeDTO {
    @NotNull(message = "代码不得为空")
    public String submissionCode;
    @DecimalMax(value = "10", message = "实际时间限制最大为10s")
    private Integer realTimeLimit;
    @DecimalMax(value = "10", message = "cpu时间限制最大为10s")
    private Integer cpuTimeLimit;
    @DecimalMin(value = "3000", message = "内存限制最小为3000kb")
    @DecimalMax(value = "65536", message = "内存限制最大为65536kb")
    private Integer memoryLimit;
    @DecimalMin(value = "10", message = "输出限制最小为10")
    @DecimalMax(value = "1000000", message = "输出限制最大为1000000")
    private Integer outputLimit;
    @NotNull(message = "语言不得为空")
    private String language;
    private String judgePreference;


    @NotNull(message = "期望输入、输出不得为空")
    @Size(message = "期望输入、输出长度最小为1、最大为10", min = 1, max = 10)
    @Valid
    private List<ResolutionDTO> resolutions;

    public Integer getRealTimeLimit() {
        return realTimeLimit == null ? JudgeConfigDefaultEnum.TIME_LIMIT_DEFAULT.getData() : realTimeLimit;
    }

    public Integer getOutputLimit() {
        return outputLimit == null ? JudgeConfigDefaultEnum.OUTPUT_LIMIT_DEFAULT.getData() : outputLimit;
    }

    public Integer getCpuTimeLimit() {
        return cpuTimeLimit == null ? JudgeConfigDefaultEnum.WALL_TIME_DEFAULT.getData() : cpuTimeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit == null ? JudgeConfigDefaultEnum.MEMORY_LIMIT_DEFAULT.getData() : memoryLimit;
    }


    public Boolean isAcmMode(){
        if(JudgePreferenceEnum.toJudgePreference(judgePreference) == null){
            throw new NotFoundException("A0005");
        }
        return judgePreference.equals(JudgePreferenceEnum.ACM.getPreference());
    }

    public String getJudgePreference() {
        return judgePreference == null ? JudgePreferenceEnum.ACM.getPreference() : judgePreference;
    }

    @Override
    public String toString(){
        return "JudgeDTO{" +
                "submissionCode='" + submissionCode + '\'' +
                ", realTimeLimit='" + realTimeLimit + '\'' +
                ", memoryLimit='" + memoryLimit + '\'' +
                ", cpuTimeLimit='" + cpuTimeLimit + '\'' +
                ", outputLimit='" + outputLimit + '\'' +
                ", language='" + language + '\'' +
                ", resolutions=" + resolutions +
                '}';
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubmissionCode() {
        return submissionCode;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }

    public void setRealTimeLimit(Integer realTimeLimit) {
        this.realTimeLimit = realTimeLimit;
    }


    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public void setCpuTimeLimit(Integer cpuTimeLimit) {
        this.cpuTimeLimit = cpuTimeLimit;
    }

    public void setOutputLimit(Integer outputLimit) {
        this.outputLimit = outputLimit;
    }

    public List<ResolutionDTO> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<ResolutionDTO> resolutions) {
        this.resolutions = resolutions;
    }

    public void setJudgePreference(String judgePreference){ this.judgePreference  = judgePreference ; }
}
