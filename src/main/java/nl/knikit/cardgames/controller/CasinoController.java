package nl.knikit.cardgames.controller;

/*
    http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/

    This class is annotated with @RestController annotation. Also note that we are using
    new annotations @GetMapping, @PostMapping, @PutMapping and @DeleteMapping instead of
    standard @RequestMapping.

    These annotations are available since Spring MVC 4.3 and are standard way of defining REST
    endpoints. They act as wrapper to @RequestMapping.
    For example @GetMapping is a composed annotation that acts as a shortcut for
    @RequestMapping(method = RequestMethod.GET).

    6 REST Endpoint                 HTTP Method 	Description
    /players 	                    GET             Returns the list of players
    /players/{id}                   GET             Returns player detail for given player {id}
    /players 	                    POST            Creates new player from the post data
    /players/{id}                   PUT             Replace the details for given player {id}
    /players 	                    DELETE          Deletes all players
    /players/{id}                   DELETE          Delete the player for given player {id}
*/


/*  The log levels in Java Logging API are different to other standard logging libraries
    Log4j/Logback	Java Logging
    fatal	        SEVERE
    error	        SEVERE
    warn	        WARNING
    info	        INFO
                    CONFIG
    debug	        FINE
                    FINER
    trace	        FINEST
*/


import lombok.extern.slf4j.Slf4j;
import nl.knikit.cardgames.exception.CasinoNotFoundForIdException;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.ICasinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

// @RestController = @Controller + @ResponseBody
@RestController
@Component
@ExposesResourceFor(Casino.class)
@Slf4j
@Scope("prototype")
public class CasinoController {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private ICasinoService casinoService;

    @GetMapping("/casinos")
    public ResponseEntity<ArrayList<Casino>> getCasinos() {

        ArrayList<Casino> casinos;
        casinos = (ArrayList) casinoService.findAll();
        return new ResponseEntity(casinos, HttpStatus.OK);
    }

    @GetMapping("/casinos/{id}")
    public ResponseEntity getCasino(@PathVariable("id") int id) throws CasinoNotFoundForIdException {

        Casino casino = casinoService.findOne(id);
        if (casino == null) {

            throw new CasinoNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(casino);
    }

    @PostMapping("/casinos")
    public ResponseEntity createCasino(@RequestBody Casino casino) {

        if (casino == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Casino supplied to create: " + casino);
        }
        casinoService.create(casino);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(casino);
    }

    @PutMapping("/casinos/{id}")
    public ResponseEntity updateCasino(@PathVariable int id, @RequestBody Casino
            casino) {
        Casino newCasino = casinoService.update(casino);
        if (null == newCasino) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Casino found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newCasino);
    }

    @DeleteMapping("/casinos/{id}")
    public ResponseEntity deleteCasinos(@PathVariable("id") int id) {

        try {
            Casino classCasino = new Casino();
            classCasino.setId(id);
            casinoService.delete(classCasino);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Casino with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Casino with /{id}: " + id + " deleted");
    }

    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(CasinoNotFoundForIdException.class)
    public ModelAndView handleCasinoNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
