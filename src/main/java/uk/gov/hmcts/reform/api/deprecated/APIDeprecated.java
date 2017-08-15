package uk.gov.hmcts.reform.api.deprecated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface APIDeprecated {
    String name();
    String docLink();
    String expireDate();
    String note() default "";
}
