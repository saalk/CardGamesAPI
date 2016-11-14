/*
package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.CardNotFoundForIdException;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.service.ICardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Card.class)
@Slf4j
@Scope("prototype")
public class CardResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private ICardService cardService;

    @GetMapping("/cards")
    public ResponseEntity<ArrayList<Card>> getCards() {

        ArrayList<Card> cards;
        cards = (ArrayList<Card>) cardService.findAll("cardId", "ASC");
        return new ResponseEntity(cards, HttpStatus.OK);
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity getCard(
            @PathVariable("cardId") String cardId) throws CardNotFoundForIdException {

        Card card = cardService.findOneWithString(cardId);
        if (card == null) {

            throw new CardNotFoundForIdException(cardId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(card);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromLabel the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/cards", params = { "suit" } )
    public ResponseEntity<ArrayList<Card>> findAllWhere(
            @RequestParam(value = "suit", required = true) String param) {

        Card classCard ;
        // ternary operator = if for conditional assignment -> The ? : operator in Java
        // boolean isHumanBoolean = ( param=="true")?true:false;
        // classCard.setHuman( isHumanBoolean );

        try {

            ArrayList<Card> cards = (ArrayList) cardService.findAllWhere("suit", param);
            if (cards == null || cards.isEmpty()) {
                throw new CardNotFoundForIdException(param);
            }

            return new ResponseEntity(cards, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Card>() );
        }
    }


    // To cardle an exception, we need to create an exception method annotated with @ExceptionCardler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(CardNotFoundForIdException.class)
    public ModelAndView handleCardNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
*/
