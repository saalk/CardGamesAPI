package nl.knikit.cardgames.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This will register springs DispatcherServlet to use @Configuration annotated classed
 * Spring then scans at application startup this class that has one job: assemble the web app's
 * moving parts
 *
 * Servlet = a java class that responds to http requests;
 * - servlet is an API specification (javax.servlet.http.*) used to extended HttpServlet
 * - then override doGet, doPost, doPu, doDelete, init and destroy for the HTTP request duration
 * - it extend the server with http capabilities when deployed in a servlet container like tomcat
 * - the class file must be located in webapps/ROOT/WEB-INF/classes/..
.class by default
 * - webapps/ROOT/WEB-INF/web.xml contains the servlet class name and url pattern
 *
 * Frameworks manage the init and destroy ie. the lifetime of a servlet to do its responding
 * they also hide technicalities by supplying annotations
 *
 * Traditionally WEB-INF/web.xml was used to register Springs DispatcherServlet. Thanks to
 * Servlet 3.0 a class like this can now replace web.xml and map spring’s dispatcher servlet
 */

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

// so bootstrap DispatcherServlet
public class WebAppInitializer implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "nl.knikit.cardgames.configuration";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        System.out.println("***** Initializing Application for " + servletContext.getServerInfo() + " *****");

        // Create ApplicationContext
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setConfigLocation(CONFIG_LOCATION);

        // Add the servlet mapping manually and make it initialize automatically
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("mvc-dispatcher", dispatcherServlet);

        servlet.addMapping("/api/*");
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(1);
    }
}

