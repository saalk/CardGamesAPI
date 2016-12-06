package nl.knikit.cardgames.configuration;

/**
 * This will register springs DispatcherServlet to use @Configuration annotated classed
 * Spring then scans at application startup this class that has one job: assemble the web app's
 * moving parts
 * <p>
 * Servlet = a java class that responds to http requests;
 * - servlet is an API specification (javax.servlet.http.*) used to extended HttpServlet
 * - then override doGet, doPost, doPu, doDelete, init and destroy for the HTTP request duration
 * - it extend the server with http capabilities when deployed in a servlet container like tomcat
 * - the class file must be located in webapps/ROOT/WEB-INF/classes/..
 * .class by default
 * - webapps/ROOT/WEB-INF/web.xml contains the servlet class name and url pattern
 * <p>
 * Frameworks manage the init and destroy ie. the lifetime of a servlet to do its responding
 * they also hide technicalities by supplying annotations
 * <p>
 * Traditionally WEB-INF/web.xml was used to register Springs DispatcherServlet. Thanks to
 * Servlet 3.0 a class like this can now replace web.xml and map spring’s dispatcher servlet
 * <p>
 * http://www.codejava.net/frameworks/spring/spring-4-and-hibernate-4-integration-tutorial-part-2-java-based-configuration
 */

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

// so bootstrap DispatcherServlet without web.xml
public class SpringWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        System.out.println("***** Initializing Application for " + servletContext.getServerInfo() + " *****");

        // 1: Create the 'root' spring web aware application context, the central interface.
        // Difference between AC and BF in spring (both fromLabel beans) is :
        // - application context = creates singleton bean when container is started
        // - beanfactory = only instantiates bean when called
        // (both use getBean from spring ioc)
        // for the non-servlet related components like dao so anything but controllers
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        // 2: register the root configuration using Spring
        applicationContext.register(ApplicationContextConfig.class);

        // 3: Add the servlet mapping manually and make it initialize automatically
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        // addition to throw an exception
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("SpringDispatcher", dispatcherServlet);

        servlet.addMapping("/api/*");
        // 4: By default, servlet and filters do not support asynchronous operations.
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(1);
    }
}

