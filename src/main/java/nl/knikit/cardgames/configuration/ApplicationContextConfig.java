package nl.knikit.cardgames.configuration;

import nl.knikit.cardgames.dao.PlayerDAOImpl;
import nl.knikit.cardgames.dao.PlayerDAO;
import nl.knikit.cardgames.utils.Password;

import org.hibernate.SessionFactory;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
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

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;



/*
 * These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

// 1: The @Configuration annotation is required for any Java-based configuration in Spring
// 2: The @ComponentScan annotation tells Spring to scan the specified package for annotated classes
// 3: The @EnableTransactionManager annotation enables Spring’s annotation-driven transaction management
// 4: TODO The @EnableJpaRepositories("*.dao.*") scan for Spring Data repositories
//

/*
Investigate the following:

@Import({xxxConfiguration.class, yyyConfiguration.class})
public class zzzConfiguration {

    @Bean // org.springframework.context.
    @Lazy
    @Autowired // org.springframework.beans.factory.
    @Qualifier("modelPackage")
    public EntityManagerFactory

    @Bean // org.springframework.context.
    @Lazy
    @Autowired // org.springframework.beans.factory.
    public PlatformTransactionManager

    @Bean
    public HibernateExceptionTranslator

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource

}
*/

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
    public DriverManagerDataSource getDataSource() throws SQLException, UnsupportedEncodingException {
        // MariaDbDataSource driverManagerDataSource = new MariaDbDataSource();
        // org.mariadb.jdbc.MySQLDataSource or com.mariadb.jdbc.Driver
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        // JDBC drivers are extensions for java to connect to the database
        // TODO get these 4 properties from property file

        // either these 3 or URL
        driverManagerDataSource.setDriverClassName("org.mariadb.jdbc.Driver");

        driverManagerDataSource.setUsername(Password.decode("cm9cdA=="));
        //driverManagerDataSource.setPassword(Password.decode("a2xhYXM="));

        //driverManagerDataSource.setUrl("jdbc:mariadb://127.0.0.1:3306/knikit");
        driverManagerDataSource.setUrl("jdbc:mariadb://192.168.2.100:3306/knikit");

        // driverManagerDataSource.setDriverClassName("org.mariadb.jdbc.MySQLDataSource");
        // URL: jdbc:mysql://{HOST}[:{PORT}][/{DB}]
        return driverManagerDataSource;
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

    // C: By configuring a transaction manager, code in the dao class doesn’t have to take care of
    // transaction management explicitly. Instead, the @Transactional annotation can be used.
    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);
        return transactionManager;
    }

    // D: The following method configures a bean which is a PlayerDAOImpl implementation:
    @Autowired
    @Bean(name = "playerDao")
    public PlayerDAO getPlayerDao(SessionFactory sessionFactory) {
        return new PlayerDAOImpl(sessionFactory);
    }

    // uses sessionBuilder.addProperties(getHibernateProperties()) in the session factory bean
    // or seat each property with sessionBuilder.setProperty("hibernate.show_sql", "true");
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        // @ing use org.hibernate.dialect.Oracle10gDialect
        // This property makes Hibernate generate the appropriate SQL for the chosen database.
        // do not use org.hibernate.dialect.MySQLInnoDBDialect
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        //properties.put("hibernate.connection.driver_class", "com.mariadb.jdbc.Driver");
        properties.put("hibernate.hbm2ddl.auto", "create");

        return properties;
    }
}
