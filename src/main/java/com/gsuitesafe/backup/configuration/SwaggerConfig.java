package com.gsuitesafe.backup.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Primary
@Configuration
@EnableSwagger2
public class SwaggerConfig implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        final SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName("default");
        swaggerResource.setLocation("/api-docs-swagger.yaml");
        swaggerResource.setSwaggerVersion("1.0");

        List<SwaggerResource> list = new ArrayList<>();
        list.add(swaggerResource);
        return list;
    }

    @Bean
    public Docket apiDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}

