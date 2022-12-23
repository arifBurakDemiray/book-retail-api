package com.bookretail.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ClientIdValidator.class)
public @interface ClientId {
    String message() default "{login_request.client_id.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}