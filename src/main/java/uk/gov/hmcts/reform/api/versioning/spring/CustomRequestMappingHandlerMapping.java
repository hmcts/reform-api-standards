package uk.gov.hmcts.reform.api.versioning.spring;

import uk.gov.hmcts.reform.api.versioning.spring.version.VersionedResource;
import uk.gov.hmcts.reform.api.versioning.spring.version.VersionedResourceRequestCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        VersionedResource typeAnnotation = AnnotationUtils.findAnnotation(handlerType, VersionedResource.class);
        return createCondition(typeAnnotation);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        VersionedResource methodAnnotation = AnnotationUtils.findAnnotation(method, VersionedResource.class);
        return createCondition(methodAnnotation);
    }

    private RequestCondition<?> createCondition(VersionedResource versionMapping) {
        if (versionMapping != null &&
            versionMapping.supported() != null &&
            !versionMapping.supported().isEmpty()) {
            return new VersionedResourceRequestCondition(versionMapping.media(), versionMapping.supported());
        }

        return null;
    }

}