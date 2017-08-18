package uk.gov.hmcts.reform.api.versioning.spring.version;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Gather information about versions of mediatypes from Spring beans.
 */
public class CacheVersionInitProcessor implements BeanPostProcessor {
    private final Map<String, Set<String>> versions = new HashMap<>();

    public Map<String, Set<String>> getVersions() {
        return Collections.unmodifiableMap(versions);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {

            VersionedResource annotation = AnnotationUtils
                    .getAnnotation(method, VersionedResource.class);

            if (annotation != null && !annotation.supported().isEmpty()) {
                Set<String> strings = versions.getOrDefault(annotation.media(), new HashSet<>());
                strings.add(annotation.supported());
                versions.put(annotation.media(), strings);
            }
        });
        return bean;
    }
}
