package nl.knikit.cardgames.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Bean (name = "swaggerapi")
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                       .apis(RequestHandlerSelectors.any())
                       .paths(PathSelectors.any())
                       .build();
    }
    
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("CardGames REST API", "Api for playing a cardDto gameDto; for now only the highlow gameDto.", "API v1", "Terms of service", "myeaddress@company.com", "License of API", "API license URL");
        return apiInfo;
    }
}
