package com.onlinejudge.JudgeHost.core.configuration;

import com.onlinejudge.JudgeHost.utils.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "judgehost-exceptions")
@PropertySource(value = "classpath:config/exception-codes.yml" ,factory = YamlPropertySourceFactory.class)
public class ExceptionCodeConfiguration {
    private Map<String,String> codes = new HashMap<>();
    public void setCodes(Map<String,String> codes){
        this.codes = codes;
    }

    public Map<String, String> getCodes() {
        return codes;
    }
    public String getMessage(String code){
        return codes.get(code);
    }
}
