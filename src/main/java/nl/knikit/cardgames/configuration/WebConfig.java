package nl.knikit.cardgames.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/*
 * These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"nl.knikit.cardgames"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webapp/javascript/**/*")
                .addResourceLocations("/webapp/javascript/");
        registry.addResourceHandler("/webapp/css/**/*")
                .addResourceLocations("/webapp/css/");
        registry.addResourceHandler("/webapp/partials/**/*")
                .addResourceLocations("/webapp/partials/");
        // registry.addResourceHandler("/webapp/**/*")
        //        .addResourceLocations("/webapp/");
        registry.addResourceHandler("/webapp/vendor/**/*")
                .addResourceLocations("/webapp/vendor/");
    }

    /* At startup, because of @Bean, Spring automatically executes jspViewResolver() and stores the
     * returned ViewResolver, which will be used by Spring when needed.
     */
    @Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
