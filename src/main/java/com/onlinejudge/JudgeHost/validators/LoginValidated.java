package com.onlinejudge.JudgeHost.validators;
import com.auth0.jwt.interfaces.Payload;

import javax.validation.Constraint;
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
@Target({ElementType.TYPE})
@Constraint(validatedBy = LoginValidator.class)
public @interface LoginValidated {
    String message() default "bad validate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}