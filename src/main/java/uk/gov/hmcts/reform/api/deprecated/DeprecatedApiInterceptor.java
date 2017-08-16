package uk.gov.hmcts.reform.api.deprecated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.WARNING;


public class DeprecatedApiInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(DeprecatedApiInterceptor.class);
    protected static final String DEPRECATED_AND_REMOVED = " is deprecated and will be removed by ";
    private Map<Object, Map<String, String>> responseHeadersByHandler = new IdentityHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Map<String, String> headers = getHeadersFromClass(handlerMethod);
        if (headers == null) {
            headers = getHeadersFromMethod(handlerMethod);
            if (headers != null) {
                response.setHeader(WARNING, headers.get(WARNING));
            } else {
                log.trace("No @APIDeprecated headers found for request handler: {}", handler);
            }
        } else {
            response.setHeader(WARNING, headers.get(WARNING));
        }
        return true;
    }

    private Map<String, String> getHeadersFromClass(HandlerMethod handlerMethod) {
        Class javaClass = handlerMethod.getBeanType();
        Map<String, String> headers = responseHeadersByHandler.get(javaClass);
        if (headers == null) {
            APIDeprecated apiDeprecated = AnnotationUtils.findAnnotation(javaClass, APIDeprecated.class);
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(javaClass, RequestMapping.class);
            if (apiDeprecated != null) {
                headers = new HashMap<>();
                headers.put(WARNING, getWarningMessage(apiDeprecated, requestMapping));
                responseHeadersByHandler.put(javaClass, headers);
                return headers;
            }
            return null;
        } else {
            return headers;
        }
    }

    private Map<String, String> getHeadersFromMethod(HandlerMethod handlerMethod) {
        Method javaMethod = handlerMethod.getMethod();
        Map<String, String> headers = responseHeadersByHandler.get(javaMethod);
        if (headers == null) {
            APIDeprecated apiDeprecated = AnnotationUtils.findAnnotation(javaMethod, APIDeprecated.class);
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(javaMethod, RequestMapping.class);
            if (apiDeprecated != null) {
                headers = new HashMap<>();
                headers.put(WARNING, getWarningMessage(apiDeprecated, requestMapping));
                responseHeadersByHandler.put(javaMethod, headers);
                return headers;
            }
            return null;
        } else {
            return headers;
        }
    }

    private String getWarningMessage(APIDeprecated apiDeprecated, RequestMapping requestMapping) {
        StringBuilder sb = new StringBuilder();
        sb.append("The ")
            .append(requestMapping != null && requestMapping.value().length > 0 ? requestMapping.value()[0] : "")
            .append(" ")
            .append(apiDeprecated.name()).append(DEPRECATED_AND_REMOVED)
            .append(apiDeprecated.expireDate())
            .append(". Please see ")
            .append(apiDeprecated.docLink())
            .append(" for details. ")
            .append(apiDeprecated.note());
        return sb.toString();
    }
}
