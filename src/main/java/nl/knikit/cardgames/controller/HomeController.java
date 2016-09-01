/*
package nl.knikit.cardgames.controller;

import nl.knikit.cardgames.model.Hello;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

*/
/**
 * Created by Gebruiker on 24-7-2016.
 * (3)
 * This REST homecontroller implements the controller for which a bean is created in (1)


@Path("/home")
@RequestScoped
public class HomeController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        return new ModelAndView("home");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Hello get() {
        // use a builder pattern and the Response from java class
        return new Hello("stranger");
    }

}*//*


*/
