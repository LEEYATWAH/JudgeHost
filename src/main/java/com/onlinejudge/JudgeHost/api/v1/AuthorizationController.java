package com.onlinejudge.JudgeHost.api.v1;

import com.onlinejudge.JudgeHost.core.UnifiedResponse;
import com.onlinejudge.JudgeHost.core.authorization.AuthorizationRequired;
import com.onlinejudge.JudgeHost.dto.AccessTokenDTO;
import com.onlinejudge.JudgeHost.dto.AuthorizationDTO;
import com.onlinejudge.JudgeHost.exception.http.ForbiddenException;
import com.onlinejudge.JudgeHost.service.AuthorizationService;
import com.onlinejudge.JudgeHost.utils.TokenHelper;
import com.onlinejudge.JudgeHost.vo.AuthorizationVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */

@RestController
@RequestMapping("/auth")
@Validated
public class AuthorizationController {
   private final AuthorizationService authorizationService;
   public AuthorizationController(AuthorizationService authorizationService) {
       this.authorizationService = authorizationService;
   }

   // 获取判题服务器接口调用凭据，以供后续接口调用使用
    @PostMapping("/get_access_token")
    public AuthorizationVO getAccessToken(@RequestBody @Validated AuthorizationDTO authorizationDTO){
       String accessToken = authorizationService.getAccessToken(authorizationDTO);
       return new AuthorizationVO(accessToken,authorizationService.getExpiredTime());
    }

    @PostMapping("/check")
    public UnifiedResponse checkAuthToken(@RequestBody AccessTokenDTO accessTokenDTO){
       Boolean isPass = authorizationService.checkAccessToken(accessTokenDTO);
       if(!isPass){
           throw new ForbiddenException("A0003");
       }
       return new UnifiedResponse("00000", "ACCESS_TOKEN_PASS!", null);
    }

}