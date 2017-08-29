package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class InterceptorConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public MappedInterceptor deprecatedApiInterceptor() {
        return new MappedInterceptor(null, new DeprecatedApiInterceptor());
    }
}
