package com.onlinejudge.JudgeHost.core.enumerations;

import java.util.stream.Stream;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */


public enum JudgePreferenceEnum {
    //ACM 模式,期间有一个点出现非ACCETPT的情况则结束判题
    ACM("ACM"),

    //OI 模式, 每一个点都会被评测
    OI("OI");

    private final String preference;

    public String getPreference() {
        return preference;
    }

    JudgePreferenceEnum(String preference) {
        this.preference = preference;
    }

    public static JudgePreferenceEnum toJudgePreference(String preference){
        return Stream.of(JudgePreferenceEnum.values()).filter(c->c.preference.equals(preference))
                .findAny()
                .orElse(null);
    }
}
