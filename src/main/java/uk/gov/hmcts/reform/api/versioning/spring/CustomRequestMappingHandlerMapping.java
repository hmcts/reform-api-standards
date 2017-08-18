package uk.gov.hmcts.reform.api.versioning.spring;

import java.util.Map;
import java.util.Set;
import uk.gov.hmcts.reform.api.versioning.spring.version.VersionedResource;
import uk.gov.hmcts.reform.api.versioning.spring.version.VersionedResourceRequestCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private Map<String, Set<String>> availableVersions;

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
            !versionMapping.media().isEmpty() &&
            !versionMapping.supported().isEmpty()) {

            String media = versionMapping.media();
            String supported = versionMapping.supported();
            Set<String> versions = availableVersions.get(media);
            return new VersionedResourceRequestCondition(media, supported, versions);
        }

        return null;
    }

    public void setAvailableVersions(Map<String, Set<String>> availableVersions) {
        this.availableVersions = availableVersions;
    }

    public Map<String, Set<String>> getAvailableVersions() {
        return availableVersions;
    }
}
