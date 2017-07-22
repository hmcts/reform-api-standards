package hello;

import java.util.Arrays;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig extends WebMvcConfigurerAdapter {
	
	  @Override
	  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	      //HeaderContentNegotiationStrategy
        configurer.favorPathExtension(false);
        configurer.useJaf(false);
        configurer.favorParameter(true);
        configurer.parameterName("mediatype");
        configurer.ignoreAcceptHeader(true);
        configurer.defaultContentType(new VersionAwareMediaType("application", "vnd.uk.gov.hmcts.test+json"));
        //NOTE: make sure ContentTypeStrategy is set AFTER ContentType otherwise it's overwritten
        configurer.defaultContentTypeStrategy(new VersionAwareHeaderContentNegotiationStrategy(Arrays.asList("1.0.1", "1.2.2", "1.3.0", "1.3.5", "2.5.1")));
	  }
}
