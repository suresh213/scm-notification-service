package scm.notification.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RecipientValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRecipient {
    String message() default "Invalid recipient format for the selected channel";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
