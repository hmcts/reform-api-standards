package uk.gov.hmcts.reform.api.versioning.spring.version;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RequestMapping
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionedResource {
    /**
     * Media type without the version or representation for example if a full media type is
     * application/vnd.app.resource-1.1+json , then this value should contain application/vnd.app.resource
     *
     * @return
     */
    String media() default "";

    String supported() default "";
}
