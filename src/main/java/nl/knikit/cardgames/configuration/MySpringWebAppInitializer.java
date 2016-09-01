/*
package nl.knikit.cardgames.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

*/
/**
 * MySpringWebAppInitializer (2) registers the specified in beans in
 * (1) ApplicationContextConfig
 * (2) DispatcherConfig
 *
 * this replaces the web.xml with code-based approach due to Servlet 3.0 features
 * tomcat 7.0.15 is compliant with this version and therefore can look for a implementation
 * of WebApplicationInitializer and invoke it's startup() method
 *//*



public class MySpringWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        // create the 'root' spring application context, the central interface
        // for the non-servlet related components like dao so anything but controllers
        AnnotationConfigWebApplicationContext rootContext = new
                AnnotationConfigWebApplicationContext();

        // register the root configuration from (1) using Spring
        rootContext.register(ApplicationContextConfig.class);

        // otherwise we would need to register the applicationContext.xml file with the beans in it
        // - XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        // - appContext.setConfigLocation("/WEB-INF/applicationContext.xml");

        // manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // create the dispatcher servlet's Spring application context
        // for the servlet related components like controllers
        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();

        // register the dispatcher configuration Spring
        dispatcherContext.register(DispatcherConfig.class);

        // register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher =
                container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
*/
