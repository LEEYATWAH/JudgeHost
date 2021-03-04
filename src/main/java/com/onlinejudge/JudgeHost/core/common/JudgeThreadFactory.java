package com.onlinejudge.JudgeHost.core.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */

public class JudgeThreadFactory  implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);
    public JudgeThreadFactory(String name) {
        namePrefix = "From JudgeThreadFactory's " + name + "-Worker-";
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        Thread thread = new Thread(null, task, name, 0);
        return thread;
    }

}
