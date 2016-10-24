package nl.knikit.cardgames.resource;

/*
 * https://examples.javacodegeeks.com/enterprise-java/spring/mvc/angularjs-spring-integration
 * -tutorial/
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainResource {

    @RequestMapping("/")
    public String homepage() {
        return "index";

    }

}
