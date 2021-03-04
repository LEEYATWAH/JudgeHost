package com.onlinejudge.JudgeHost.validators;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 * @param
 * @author LeeYatWah
 * @return $
 * @date $ $
 * @description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = LanguageTypeAcceptedValidator.class)
public @interface LanguageTypeAccepted {
    String message() default "不支持的编程语言，请检查后重试！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}