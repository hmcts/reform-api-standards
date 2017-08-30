package uk.gov.hmcts.reform.api.deprecated;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.MappedInterceptor;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MappedInterceptor deprecatedApiInterceptor() {
        return new MappedInterceptor(null, new DeprecatedApiInterceptor());
    }
}
