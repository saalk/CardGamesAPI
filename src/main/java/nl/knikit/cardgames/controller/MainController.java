package nl.knikit.cardgames.controller;

/*
 * https://examples.javacodegeeks.com/enterprise-java/spring/mvc/angularjs-spring-integration
 * -tutorial/
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String homepage() {
        return "index";

    }

}
