package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.HandNotFoundForIdException;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.service.IHandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Hand.class)
@Slf4j
@Scope("prototype")
public class HandResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IHandService HandService;

    @GetMapping("/hands")
    public ResponseEntity<ArrayList<Hand>> getHands() {

        ArrayList<Hand> Hands;
        Hands = (ArrayList) HandService.findAll("isHuman", "DESC");
        return new ResponseEntity(Hands, HttpStatus.OK);
    }

    @GetMapping("/hands/{id}")
    public ResponseEntity getHand(
            @PathVariable("id") int id) throws HandNotFoundForIdException {

        Hand Hand = HandService.findOne(id);
        if (Hand == null) {

            throw new HandNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Hand);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromRankName the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromRankName the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/hands", params = { "casino" } )
    public ResponseEntity<ArrayList<Hand>> findAllWhere(
            @RequestParam(value = "casino", required = true) String param) {

        try {
            ArrayList<Hand> Hands = (ArrayList) HandService.findAllWhere("isHuman", param);
            if (Hands == null || Hands.isEmpty()) {
                throw new HandNotFoundForIdException(999);
            }

            return new ResponseEntity(Hands, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Hand>() );
        }
    }

    @PostMapping("/hands")
    public ResponseEntity createHand(
            @RequestBody Hand Hand) {

        if (Hand == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Hand supplied to create: " + Hand);
        }
        HandService.create(Hand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Hand);
    }

    @PutMapping("/hands/{id}")
    public ResponseEntity updateHand(
            @PathVariable int id, @RequestBody Hand Hand) {

        Hand newHand = HandService.update(Hand);
        if (null == newHand) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hand found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newHand);
    }

    @DeleteMapping("/hands/{id}")
    public ResponseEntity deleteHands(
            @PathVariable("id") int id) {

        try {
            Hand classHand = new Hand();
            classHand.setId(id);
            HandService.deleteOne(classHand);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hands with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hand with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromRankName the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromRankName the boolean isHuman with value 'true' for ?isHuman=true

    // /Hands?id=1,2,3,4
    @DeleteMapping(value = "/hands", params = { "id" } )
    public ResponseEntity deleteHandsById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Hand classHand = new Hand();

        try {
            HandService.deleteAllByIds(classHand, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hands with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hand with ?id= : " + ids + " deleted");
    }


    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(HandNotFoundForIdException.class)
    public ModelAndView handleHandNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
