package nl.deknik.cardgames.configuration;

import nl.deknik.cardgames.controller.HomeController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * DispatcherConfig (1) configures beans for servlets like controllers and filters
 */

@Configuration
@ComponentScan(basePackageClasses = ApplicationContextConfig.class, useDefaultFilters = false, includeFilters = {@ComponentScan.Filter(Controller.class)})
public class DispatcherConfig extends WebMvcConfigurerAdapter {

    //This configuration defines two beans, one for a typical Spring MVC’s view resolver and one for a controller.

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "/")
    public Controller getHomeController() {
        return new HomeController();
    }
}