package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.DeckNotFoundForIdException;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.service.IDeckService;

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
@ExposesResourceFor(Deck.class)
@Slf4j
@Scope("prototype")
public class DeckResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IDeckService DeckService;

    @GetMapping("/Decks")
    public ResponseEntity<ArrayList<Deck>> getDecks() {

        ArrayList<Deck> Decks;
        Decks = (ArrayList) DeckService.findAll("isHuman", "DESC");
        return new ResponseEntity(Decks, HttpStatus.OK);
    }

    @GetMapping("/Decks/{id}")
    public ResponseEntity getDeck(
            @PathVariable("id") int id) throws DeckNotFoundForIdException {

        Deck Deck = DeckService.findOne(id);
        if (Deck == null) {

            throw new DeckNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Deck);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you get the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you get the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/Decks", params = { "isHuman" } )
    public ResponseEntity<ArrayList<Deck>> findAllWhere(
            @RequestParam(value = "isHuman", required = true) String param) {

        Deck classDeck = new Deck();
        // ternary operator = shorthand if for conditional assignment -> The ? : operator in Java
        // boolean isHumanBoolean = ( param=="true")?true:false;
        // classDeck.setHuman( isHumanBoolean );

        try {

            ArrayList<Deck> Decks = (ArrayList) DeckService.findAllWhere(classDeck, "isHuman", param);
            if (Decks == null || Decks.isEmpty()) {
                throw new DeckNotFoundForIdException(999);
            }

            return new ResponseEntity(Decks, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Deck>() );
        }
    }

    @PostMapping("/Decks")
    public ResponseEntity createDeck(
            @RequestBody Deck Deck) {

        if (Deck == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Deck supplied to create: " + Deck);
        }
        DeckService.create(Deck);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Deck);
    }

    @PutMapping("/Decks/{id}")
    public ResponseEntity updateDeck(
            @PathVariable int id, @RequestBody Deck Deck) {

        Deck newDeck = DeckService.update(Deck);
        if (null == newDeck) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Deck found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newDeck);
    }

    @DeleteMapping("/Decks/{id}")
    public ResponseEntity deleteDecks(
            @PathVariable("id") int id) {

        try {
            Deck classDeck = new Deck();
            classDeck.setId(id);
            DeckService.deleteOne(classDeck);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Decks with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deck with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you get the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you get the boolean isHuman with value 'true' for ?isHuman=true

    // /Decks?id=1,2,3,4
    @DeleteMapping(value = "/Decks", params = { "id" } )
    public ResponseEntity deleteDecksById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Deck classDeck = new Deck();

        try {
            DeckService.deleteAllByIds(classDeck, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Decks with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deck with ?id= : " + ids + " deleted");
    }


    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(DeckNotFoundForIdException.class)
    public ModelAndView handleDeckNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
