package uk.ac.ebi.ampt2d.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfiguration {

    private Logger log = LoggerFactory.getLogger(this.getClass());
//    @Bean
//    public TestRestTemplate testRestTemplate() {
//        return new TestRestTemplate();
//    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}
