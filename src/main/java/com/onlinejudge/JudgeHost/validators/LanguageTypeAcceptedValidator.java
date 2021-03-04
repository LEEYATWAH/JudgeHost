package com.onlinejudge.JudgeHost.validators;

import com.onlinejudge.JudgeHost.core.enumerations.LanguageScriptEnum;
import com.onlinejudge.JudgeHost.dto.JudgeDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * @return $
 * @param
 * @author LeeYatWah
 * @date $ $
 * @description
 */


public class LanguageTypeAcceptedValidator implements ConstraintValidator<LanguageTypeAccepted, JudgeDTO> {
    @Override
    public boolean isValid(JudgeDTO judgeDTO, ConstraintValidatorContext constraintValidatorContext) {
        String language = judgeDTO.getLanguage();
        return LanguageScriptEnum.isLanguageAccepted(language);
    }
}