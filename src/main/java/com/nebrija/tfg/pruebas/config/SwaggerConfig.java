package com.nebrija.tfg.pruebas.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.function.Predicate;

import static com.nebrija.tfg.pruebas.constants.Constants.MODULE_NAME;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiV1(@Value("${chen.base_path}") String basePath) {
        return new Docket(DocumentationType.SWAGGER_2).groupName(MODULE_NAME).apiInfo(apiInfo()).select()
                .paths(apiV1Paths(basePath)).build();
    }

    private Predicate<String> apiV1Paths(String basePath) {
        return PathSelectors.regex(basePath + ".*");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(MODULE_NAME)
                .description("proyecto back de microservicio para " + MODULE_NAME).build();
    }
}
