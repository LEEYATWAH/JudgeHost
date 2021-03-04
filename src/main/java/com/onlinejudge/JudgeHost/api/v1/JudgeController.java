package com.onlinejudge.JudgeHost.api.v1;



import com.onlinejudge.JudgeHost.core.authorization.AuthorizationRequired;
import com.onlinejudge.JudgeHost.dto.SingleJudgeResultDTO;
import com.onlinejudge.JudgeHost.exception.http.ForbiddenException;
import com.onlinejudge.JudgeHost.service.JudgeService;
import com.onlinejudge.JudgeHost.dto.JudgeDTO;
import com.onlinejudge.JudgeHost.vo.JudgeConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/judge")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JudgeController {
    private final JudgeService judgeService;
    @Autowired
    public JudgeController(JudgeService judgeService){
        this.judgeService = judgeService;
    }

    @PostMapping("/run")
    @AuthorizationRequired
    public Object runJudge(@RequestBody @Validated JudgeDTO judgeDTO) throws ExecutionException, InterruptedException{
        CompletableFuture<List<SingleJudgeResultDTO>> judgeResults;
        try{
            judgeResults= judgeService.runJudge(judgeDTO);
        }catch(Exception e){
            throw new ForbiddenException("B1005");
        }
        List<String> extraResult = judgeService.getExtraInfo();
        return new JudgeConditionVO(judgeResults.get(), extraResult, judgeService.getSubmissionId());
    }

    @PostMapping("/run_for_test")
    @AuthorizationRequired
    public Object runJudgeWithoutThreadPoolForTest(@RequestBody @Validated JudgeDTO judgeDTO) {
        List<SingleJudgeResultDTO> judgeResults = judgeService.judgeWithoutThreadPoolForTest(judgeDTO);
        List<String> extraResult = judgeService.getExtraInfo();
        return new JudgeConditionVO(judgeResults, extraResult, judgeService.getSubmissionId());
    }

}
