package com.onlinejudge.JudgeHost.utils;

import org.apache.catalina.LifecycleState;

import java.util.List;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */

public class DataReFormat {
    public static String stringListToString(List<String> stringList) {
        StringBuilder result = new StringBuilder();
        stringList.forEach(result::append);
        return result.toString();
    }
}
