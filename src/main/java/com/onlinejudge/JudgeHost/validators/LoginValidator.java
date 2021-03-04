package com.onlinejudge.JudgeHost.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.onlinejudge.JudgeHost.dto.AccessTokenDTO;
import com.onlinejudge.JudgeHost.dto.AuthorizationDTO;

/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */

public class LoginValidator implements ConstraintValidator<LoginValidated, AccessTokenDTO> {
    @Override
    public void initialize(LoginValidated constraintAnnotation) {

    }


    @Override
    public boolean isValid(AccessTokenDTO accessTokenDTO, ConstraintValidatorContext constraintValidatorContext) {
        String test1 = accessTokenDTO.getAccessToken();
        return false;
    }
}