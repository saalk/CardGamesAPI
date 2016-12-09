package nl.knikit.cardgames.configuration;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
// TODO import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.base.Preconditions;

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


/*
 * These will bootstrap the spring mvc application and set package to scan controllers and
 * resources.
 */

// 1: The @Configuration annotation is required for any Java-based configuration in Spring
// 2: The @ComponentScan annotation tells Spring to scan the specified package for annotated classes
// 3: The @EnableTransactionManager annotation enables Spring’s annotation-driven transaction management
// 4: TODO The @EnableJpaRepositories("*.dao.*") scan for Spring Data repositories
//

@Configuration
@EnableTransactionManagement
@EnableWebMvc
///TODO @PropertySource({ "classpath:persistence-mysql.properties" })
@ComponentScan(basePackages = {"nl.knikit.cardgames"})
public class ApplicationContextConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

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

    // A: Configure a session bean for Player from the session factory etc
    // SessionFactory is to create and manage Sessions, one per app and singleton
    // Session is to provide a CRUD interface for mapped classes, one per client
    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws UnsupportedEncodingException, SQLException {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        // If you want to add more classes -> (User.class, Object.class);
        // sessionBuilder.addAnnotatedClasses(Player.class);
        // or just scan the model package
        sessionFactory.setDataSource(restDataSource());
        sessionFactory.setPackagesToScan(new String[]{"nl.knikit.cardgames"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    // B: create a DataSource is to be used with Hibernate’s SessionFactory
    // DataSource is a "Connection" to the database
    // Sessions work on the data via a datasource
    @Bean(name = "restDataSource")
    public DriverManagerDataSource restDataSource() {
        //MariaDbDataSource driverManagerDataSource = new MariaDbDataSource();
        // org.mariadb.jdbc.MySQLDataSource or com.mariadb.jdbc.Driver
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        // JDBC drivers are extensions for java to connect to the database

        // either these 3 or URL
        driverManagerDataSource.setDriverClassName(Preconditions.checkNotNull("org.mariadb.jdbc.Driver"));
        driverManagerDataSource.setUsername(Preconditions.checkNotNull("root"));
        driverManagerDataSource.setPassword(Preconditions.checkNotNull("klaas"));
        driverManagerDataSource.setUrl(Preconditions.checkNotNull("jdbc:mariadb://192.168.2.100:3306/knikit"));

        return driverManagerDataSource;

        /*BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("org.mysql.jdbc.Driver"));
        dataSource.setUrl(env.getProperty("jdbc:mysql://192.168.2.100:3306/knikit"));
        dataSource.setUsername(env.getProperty("admin"));
        dataSource.setPassword(env.getProperty("klaas"));*/
        //dataSource.setUsername(env.getProperty(Password.decode("cm9cdA==")));
        //dataSource.setPassword(env.getProperty(Password.decode("a2xhYXM=")));
        //return dataSource;

    }


    // C: By configuring a transaction manager, code in the dao class doesn’t have to take care of
    // transaction management explicitly. Instead, the @Transactional annotation can be used.

    @Bean(name = "transactionManager")
    @Autowired
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

    // D: Translate all errors generated during the persistence process (HibernateExceptions, PersistenceExceptions...)
    // into DataAccessException objects
    @Bean(name = "exceptionTranslation")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean(name = "modelmapper")
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    // uses sessionBuilder.addProperties(getHibernateProperties()) in the session factory bean
    // or seat each property with sessionBuilder.setProperty("hibernate.show_sql", "true");
    final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.show_sql", "true");
        // @ing use org.hibernate.dialect.Oracle10gDialect
        // This property makes Hibernate generate the appropriate SQL for the chosen database.
        // do not use org.hibernate.dialect.MySQLInnoDBDialect or org.hibernate.dialect.MySQLDialect
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        //properties.put("hibernate.connection.driver_class", "com.mariadb.jdbc.Driver");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
        hibernateProperties.put("hibernate.hbm2ddl.import_files", "initial_data.sql");
        hibernateProperties.setProperty("hibernate.connection.autocommit", "false");
        hibernateProperties.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
        hibernateProperties.setProperty("hibernate.id.new_generator_mappings", "true");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        return hibernateProperties;
    }
}