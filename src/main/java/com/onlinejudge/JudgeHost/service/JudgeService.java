package com.onlinejudge.JudgeHost.service;

import com.alibaba.fastjson.JSON;
import com.onlinejudge.JudgeHost.core.configuration.JudgeEnvironmentConfiguration;
import com.onlinejudge.JudgeHost.core.enumerations.JudgeResultEnum;
import com.onlinejudge.JudgeHost.core.enumerations.LanguageScriptEnum;
import com.onlinejudge.JudgeHost.dto.ResolutionDTO;
import com.onlinejudge.JudgeHost.dto.SingleJudgeResultDTO;
import com.onlinejudge.JudgeHost.exception.http.NotFoundException;
import com.onlinejudge.JudgeHost.network.HttpRequest;
import com.onlinejudge.JudgeHost.utils.DataReFormat;
import com.onlinejudge.JudgeHost.utils.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.onlinejudge.JudgeHost.dto.JudgeDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JudgeService {
    private String runningPath;
    private String submissionId;
    private List<String> extraInfo;;
    private JudgeDTO judgeConfig;
    private final JudgeEnvironmentConfiguration judgeEnvironmentConfiguration;
    private final Runtime runner;



    public JudgeService(JudgeEnvironmentConfiguration judgeEnvironmentConfiguration){
        this.judgeEnvironmentConfiguration = judgeEnvironmentConfiguration;
        this.runner = Runtime.getRuntime();
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setJudgeConfig(JudgeDTO judgeConfig) {
        this.judgeConfig = judgeConfig;
    }

    /**
     * @return void
     * @author LeeYatWah
     * @description 调用判题核心 执行判题
     * @date 2020-12-13
     */
    private SingleJudgeResultDTO startJudging(String stdInPath) {
        String judgeCoreScript = judgeEnvironmentConfiguration.getScriptPath() + "/judger";
        String[] command = {
                judgeCoreScript,
                "-r", runningPath,
                "-o", getSubmitWorkingPath() + "/" + UUID.randomUUID() + ".out",
                "-t", String.valueOf(judgeConfig.getRealTimeLimit()),
                "-c", String.valueOf(judgeConfig.getCpuTimeLimit()),
                "-m", String.valueOf(judgeConfig.getMemoryLimit()),
                "-f", String.valueOf(judgeConfig.getOutputLimit()),
                "-e", getSubmitWorkingPath() + "/" + UUID.randomUUID() + ".err",
                "-i", stdInPath
        };
        List<String> result = new ArrayList<>(0);
        try{

            Process process = runner.exec(command);
            process.waitFor();
            result = readStdout(process);
        }catch (IOException | InterruptedException ioException){
            ioException.printStackTrace();;
        }
        // 将判题核心的stdout转换成数据传输对象
        SingleJudgeResultDTO singleJudgeResult = JSON.parseObject(
                DataReFormat.stringListToString(result),
                SingleJudgeResultDTO.class
        );
        System.out.println(singleJudgeResult);
        return singleJudgeResult;
    }

    /**
     * @return void
     * @author LeeYatWah
     * @description 调用compile.sh 生成脚本
     * @date 2020-12-13
     */
    private List<String>  compileSubmission(){
        String compileScript = this.judgeEnvironmentConfiguration.getScriptPath()+ "/compile.sh";
        if(!FileHelper.isFileIn(compileScript)){
            throw new NotFoundException("B1002");
        }
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeConfig.getLanguage());
        //用户代码
        //用户代码
        String codePath = getSubmitWorkingPath() + "/Main." + language.getExtensionName();
        // 对应语言的编译脚本
        String buildScript = language.getBuildScriptByRunningPath(getSubmitWorkingPath(), codePath);

        List<String> result = new ArrayList<>();
        //Runtime 对象，准备执行生成脚本
        try{
            Process process = runner.exec(
                    new String[]{
                            compileScript,
                            getSubmitWorkingPath(),
                            codePath,
                            judgeConfig.getSubmissionCode(),
                            buildScript
                    });
            process.waitFor();
            result = readStdout(process);
        }catch (IOException | InterruptedException ioException){
            //TODO：异常处理
            ioException.printStackTrace();
        }
        return result;
    }


    /**
     * @return String
     * @author LeeYatWah
     * @date 2020-12-13
     * @description 返回本次提交的工作目录
     */
    private String getSubmitWorkingPath() {
        return judgeEnvironmentConfiguration.getWorkPath() + "/submissions/" + getSubmissionId();
    }

    /**
     * @return String
     * @author LeeYatWah
     * @date 2021-3-1
     * @description 返回本次提交的解答目录(即期望输入输出存储的地方)
     */
    private String getSubmitResolutionPath() {
        return judgeEnvironmentConfiguration.getResolutionPath() + "/" + getSubmissionId();
    }

    /**
     * @return
     * @author LeeYatWah
     * @date 2021-3-1
     * @description 比较用户输出和期望输出
     */

    private Boolean compareOutputWithResolutions(String submissionOutput, String expectedOutput) {
        String exitCode = "0";
        try {
            String compareScript = judgeEnvironmentConfiguration.getScriptPath() + "/compare.sh";

            Process process = runner.exec(new String[]{
                    compareScript,
                    submissionOutput,
                    expectedOutput
            });
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            exitCode = reader.readLine();
        } catch (IOException | InterruptedException ioException) {
            // TODO：异常处理
            ioException.printStackTrace();
        }
        return "0".equals(exitCode);
    }

    private ResolutionDTO getResolutionInputAndOutputFile(ResolutionDTO resolution) {
        String inputFile = resolution.getInput();
        String outputFile = resolution.getExpectedOutput();

        // 下载、获取输入和期望输出
        Resource inputFileResource = HttpRequest.getFile(inputFile);
        Resource outputFileResource = HttpRequest.getFile(outputFile);
        // TODO: 将基础路径转移到配置文件中
        String inPath =  getSubmitResolutionPath() + "/" + UUID.randomUUID() + ".in";
        String outPath = getSubmitResolutionPath() + "/" + UUID.randomUUID() + ".out";

        try {
            File inFile = new File(inPath);
            FileUtils.copyInputStreamToFile(inputFileResource.getInputStream(), inFile);
            File outFile = new File(outPath);
            FileUtils.copyInputStreamToFile(outputFileResource.getInputStream(), outFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        resolution.setInput(inPath);
        resolution.setExpectedOutput(outPath);
        return resolution;
    }


    @SuppressWarnings("DuplicatedCode")
    @Async(value = "asyncServiceExecutor")
    public CompletableFuture<List<SingleJudgeResultDTO>>  runJudge(JudgeDTO judgeDTO) {
        // 判断配置合法性
        this.judgeEnvironmentConfiguration.checkJudgeEnvironmentBaseFileIn();
        // 为本次提交提供唯一id
        this.setSubmissionId(UUID.randomUUID().toString());
        // 判题基础配置
        setJudgeConfig(judgeDTO);
        // 设置执行目录
        runningPath = getSubmitWorkingPath() + "/run";
        // 编译用户的提交
        List<String> compileResult = compileSubmission();
        this.extraInfo  = compileResult;
        List<SingleJudgeResultDTO> result = new ArrayList<>();
        if (isCompileSuccess(compileResult)) {
            List<ResolutionDTO> totalResolution = judgeDTO.getResolutions();
            for(ResolutionDTO resolutionDTO : totalResolution){
                SingleJudgeResultDTO singleJudgeResult = runForSingleJudge(resolutionDTO);
                boolean isAccept = singleJudgeResult.getCondition().equals(JudgeResultEnum.ACCEPTED.getNumber());
                // 这个测试点没有通过，并且是acm模式
                result.add(singleJudgeResult);
                if (!isAccept && judgeDTO.isAcmMode()) {
                    break;
                }
                // oi模式，继续执行判题
            }

        }else{
            SingleJudgeResultDTO resolution = new SingleJudgeResultDTO();
            resolution.setCondition(JudgeResultEnum.COMPILE_ERROR.getNumber());
            resolution.setMessageWithCondition();
            result.add(resolution);
        }
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("DuplicatedCode")
    public List<SingleJudgeResultDTO> judgeWithoutThreadPoolForTest(JudgeDTO judgeDTO){
        this.judgeEnvironmentConfiguration.checkJudgeEnvironmentBaseFileIn();
        this.setSubmissionId(UUID.randomUUID().toString());
        setJudgeConfig(judgeDTO);
        runningPath = getSubmitWorkingPath() + "/run";
        List<String> compileResult = compileSubmission();
        this.extraInfo = compileResult;
        List<SingleJudgeResultDTO> result = new ArrayList<>();
        if (isCompileSuccess(compileResult)) {
            List<ResolutionDTO> totalResolution = judgeDTO.getResolutions();
            for (ResolutionDTO resolutionDTO : totalResolution) {
                SingleJudgeResultDTO singleJudgeResult = runForSingleJudge(resolutionDTO);
                boolean isAccept = singleJudgeResult.getCondition().equals(JudgeResultEnum.ACCEPTED.getNumber());
                result.add(singleJudgeResult);
                if (!isAccept && judgeDTO.isAcmMode()) {
                    break;
                }
            }
        } else {
            SingleJudgeResultDTO resolution = new SingleJudgeResultDTO();
            resolution.setCondition(JudgeResultEnum.COMPILE_ERROR.getNumber());
            resolution.setMessageWithCondition();
            result.add(resolution);
        }
        return result;
    }

    private SingleJudgeResultDTO runForSingleJudge(ResolutionDTO singleResolution) {
        ResolutionDTO resolution = getResolutionInputAndOutputFile(singleResolution);
        SingleJudgeResultDTO singleJudgeResult = startJudging(resolution.getInput());
        List<String> judgeCoreStderr = getJudgeCoreStderr(singleJudgeResult.getStderrPath());
        // 没有stderr输出时:
        if (judgeCoreStderr.size() == 0) {
            // 如果通过，将condition设置为 0
            Boolean isPass = compareOutputWithResolutions(singleJudgeResult.getStdoutPath(), singleResolution.getExpectedOutput());
            if (isPass) {
                singleJudgeResult.setCondition(JudgeResultEnum.ACCEPTED.getNumber());
            }
        } else {
            this.extraInfo = judgeCoreStderr;
            singleJudgeResult.setCondition(JudgeResultEnum.RUNTIME_ERROR.getNumber());
        }
        singleJudgeResult.setMessageWithCondition();
        return singleJudgeResult;
    }


    //获取运行的脚本/可执行文件的输出
    private List<String> readStdout(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> stringList = new ArrayList<>();
        String temp;
        while ((temp = reader.readLine()) != null) {
            stringList.add(temp);
        }
        BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((temp = errReader.readLine()) != null) {
            // TODO:处理错误输出
            stringList.add(temp);
        }
        // TODO:处理错误输出
        return stringList;
    }


    private Boolean isCompileSuccess(List<String> compileResult) {
        LanguageScriptEnum language = LanguageScriptEnum.toLanguageType(judgeConfig.getLanguage());
        boolean isCppFamily = (language == LanguageScriptEnum.C || language == LanguageScriptEnum.C_PLUS_PLUS);
        boolean isJava =(language == LanguageScriptEnum.JAVA);
        for (String str : compileResult) {
            boolean isbad = str.contains("error:") || str.contains("错误：");
            if (isCppFamily && isbad) {
                return false;
            }
            isbad = str.contains("Error:") || str.contains("错误:");
            if (isJava && isbad) {
                return false;
            }
        }
        return true;
    }

    private List<String> getJudgeCoreStderr(String stderrPath) {
        List<String> judgeErrors = null;
        try {
            judgeErrors = FileHelper.readFileByLines(stderrPath);
        } catch (IOException ioException) {
            //TODO:找不到stderr的错误处理
            ioException.printStackTrace();
        }
        return judgeErrors;
    }

    public List<String> getExtraInfo() {
        return extraInfo;
    }

}
