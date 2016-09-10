package nl.knikit.cardgames.configuration;

import nl.knikit.cardgames.DAO.PlayerDAO;
import nl.knikit.cardgames.DAO.PlayerDAOInterface;
import nl.knikit.cardgames.model.Player;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.registry.infomodel.User;



/*
 * These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

// 1: The @Configuration annotation is required for any Java-based configuration in Spring
// 2: The @ComponentScan annotation tells Spring to scan the specified package for annotated classes
//    (like controllers)
// 3: The @EnableTransactionManager annotation enables Spring’s annotation-driven transaction management
//
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"nl.knikit.cardgames"})
@EnableTransactionManagement
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

    // Converts logical view names to actual JSP pages
    @Bean(name = "viewresolver")
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    // A: create a DataSource is to be used with Hibernate’s SessionFactory
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

    // B: Configure a session bean for Player from the session factory etc
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {

        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        // If you want to add more classes -> (User.class, Object.class);
        // sessionBuilder.addAnnotatedClasses(Player.class);
        // or just scan the model package
        sessionBuilder.scanPackages("nl.knikit.cardgames.model");
        sessionBuilder.addProperties(getHibernateProperties());
        return sessionBuilder.buildSessionFactory();
    }

    // C: The following method configures a bean which is a PlayerDAO implementation:
    @Autowired
    @Bean(name = "playerDao")
    public PlayerDAOInterface getPlayerDao(SessionFactory sessionFactory) {
        return new PlayerDAO(sessionFactory);
    }

    // uses sessionBuilder.addProperties(getHibernateProperties()) in the session factory bean
    // or seat each property with sessionBuilder.setProperty("hibernate.show_sql", "true");
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        // @ing use org.hibernate.dialect.Oracle10gDialect
        // This property makes Hibernate generate the appropriate SQL for the chosen database.
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return properties;
    }
}
