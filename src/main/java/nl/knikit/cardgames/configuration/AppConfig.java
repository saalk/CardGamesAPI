package nl.knikit.cardgames.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/* These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "nl.knikit.cardgames")
public class AppConfig {
}
