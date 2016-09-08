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

// MySQL JDBC Driver com.mysql.jdbc.jdbc2.optional.MysqlDataSource class
// Oracle database driver oracle.jdbc.pool.OracleDataSource class
import org.mariadb.jdbc.*;

import java.sql.SQLException;

/*
 * These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"nl.knikit.cardgames"})
public class ApplicationContextConfig extends WebMvcConfigurerAdapter {

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
    @Bean(name = "viewresolver")
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }


    @Bean(name = "dataSource")
    public MariaDbDataSource getDataSource() throws SQLException {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        // driver extention for java to connect the database
        //dataSource.setDriverClassName("com.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://localhost:3306/cardgamesdb");
        dataSource.setUser("root");
        dataSource.setPassword("klaas");

        return dataSource;
    }
}
