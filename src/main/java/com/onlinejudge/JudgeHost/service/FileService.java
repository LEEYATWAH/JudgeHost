package com.onlinejudge.JudgeHost.service;/**
 * @return $
 * @param $
 * @author LeeYatWah
 * @date $ $
 * @description
 */

import com.onlinejudge.JudgeHost.core.configuration.JudgeEnvironmentConfiguration;
import com.onlinejudge.JudgeHost.exception.http.NotFoundException;
import com.onlinejudge.JudgeHost.utils.FileHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */
@Service
public class FileService {
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    public FileService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration) {
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
    }

    //读取某次提交的文件夹，将内容压缩过之后返回
    public String getSubmissionDataById(String submissionId) {
        String submisstionPath = getSubmissionPathById(submissionId);
        // 压缩目标文件
        return zipSubmissionFolder(submisstionPath);
    }

    //获取某次提交的工作目录
    private String getSubmissionPathById(String submissionId) {
        return this.judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + submissionId;
    }

    //返回压缩后的压缩包目录
    private String zipSubmissionFolder(String submissionPath) {
        if (!FileHelper.isDirectory(submissionPath)) {
            throw new NotFoundException("B1003");
        }
        String zippedPath = submissionPath + "/" + UUID.randomUUID() + ".zip";
        boolean isZipped = false;
        try {
            isZipped = FileHelper.zipDictionary(zippedPath, submissionPath);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        if (!isZipped) {
            throw new NotFoundException("B1003");
        }
        return zippedPath;
    }
}
