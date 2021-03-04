package com.onlinejudge.JudgeHost.core.configuration;

import com.onlinejudge.JudgeHost.exception.http.NotFoundException;
import com.onlinejudge.JudgeHost.utils.FileHelper;
import com.onlinejudge.JudgeHost.utils.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "judge-environment")
@Component
@Configuration
@PropertySource(value = "classpath:config/judge-environment.yml", factory = YamlPropertySourceFactory.class)
public class JudgeEnvironmentConfiguration {
    private String workPath;
    private String scriptPath;
    private String resolutionPath;

    public String getResolutionPath() {
        return resolutionPath;
    }
    public void checkJudgeEnvironmentBaseFileIn() {
        if (!FileHelper.isDirectory(resolutionPath)) {
            throw new NotFoundException("B1002");
        }
        if (!FileHelper.isDirectory(workPath)) {
            throw new NotFoundException("B1002");
        }
        if (!FileHelper.isDirectory(scriptPath)) {
            throw new NotFoundException("B1002");
        }
    }

    public void setResolutionPath(String resolutionPath) {
        this.resolutionPath = resolutionPath;
    }

    public String getScriptPath(){
        return scriptPath;
    }

    public void setScriptPath(String scriptPath){
        this.scriptPath = scriptPath;
    }

    public String getWorkPath(){
        return workPath;
    }

    public void setWorkPath(String workPath){
        this.workPath = workPath;
    }



    @Override
    public String toString(){
        return  "JudgeEnvironmentConfiguration{" +
                "path='" + workPath + '\'' +
                '}';
    }

}
