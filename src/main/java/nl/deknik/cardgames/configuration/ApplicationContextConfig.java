package nl.deknik.cardgames.configuration;

import nl.deknik.cardgames.controller.HomeController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * ApplicationContextConfig (1) configures beans
 *
 */

@Configuration
@ComponentScan(excludeFilters={@ComponentScan.Filter(Controller.class)})
public class ApplicationContextConfig {

    //This configuration defines non servlet application beans

}