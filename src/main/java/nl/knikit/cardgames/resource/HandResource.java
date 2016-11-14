package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.HandNotFoundForIdException;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IHandService;
import nl.knikit.cardgames.service.IPlayerService;

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
@ExposesResourceFor(hand.class)
@Slf4j
@Scope("prototype")
public class HandResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IHandService handService;
    
    @Autowired
    private IPlayerService playerService;
    
    @Autowired
    private ICasinoService casinoService;
    
   
    //@Autowired
    //private ICardService cardService;
    
    @GetMapping("/hands")
    public ResponseEntity<ArrayList<hand>> getHands() {

        ArrayList<hand> hands;
        hands = (ArrayList) handService.findAll("isHuman", "DESC");
        return new ResponseEntity(hands, HttpStatus.OK);
    }

    @GetMapping("/hands/{id}")
    public ResponseEntity getHand(
            @PathVariable("id") int id) throws HandNotFoundForIdException {

        hand hand = handService.findOne(id);
        if (hand == null) {

            throw new HandNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hand);
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

    @GetMapping(value = "/hands", params = { "casino" } )
    public ResponseEntity<ArrayList<hand>> findAllWhere(
            @RequestParam(value = "casino", required = true) String param) {

        try {
            ArrayList<hand> hands = (ArrayList<hand>) handService.findAllWhere("playerId", param);
            if (hands == null || hands.isEmpty()) {
                throw new HandNotFoundForIdException(999);
            }

            return new ResponseEntity(hands, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<hand>() );
        }
    }

    @PostMapping("/hands")
    public ResponseEntity createHand(
            @RequestBody hand hand) {
    
        // check for casinoObj and playerObj
        if ( !(hand.getPlayerObj().getPlayerId()>0) ||
             !(hand.getCasinoObj().getCasinoId()>0) ||
             !(hand.getCardObj().getCardId().isEmpty()) ) {
            
            return ResponseEntity
                           .status(HttpStatus.NOT_ACCEPTABLE)
                           .body("No Casino or Player or Card supplied for hand to create " + hand);
        }
        
        Casino currentCasino = casinoService.findOne(hand.getCasinoObj().getCasinoId());
        Player currentPlayer = playerService.findOne(hand.getPlayerObj().getPlayerId());
        if (currentCasino == null || currentPlayer == null ) {
            return ResponseEntity
                           .status(HttpStatus.NOT_ACCEPTABLE)
                           .body("No Casino or Player supplied to relate hand to: " + hand);
        }
            
        if (!Card.isValidCard(hand.getCardObj().getCardId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Not a valid card supplied to create for hand: " + hand);
        }
        handService.create(hand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hand);
    }

    @PutMapping("/hands/{id}")
    public ResponseEntity updateHand(
            @PathVariable int id, @RequestBody hand hand) {

        hand newHand = handService.update(hand);
        if (null == newHand) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No hand found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newHand);
    }

    @DeleteMapping("/hands/{id}")
    public ResponseEntity deleteHands(
            @PathVariable("id") int id) {

        try {
            hand classHand = new hand();
            classHand.setHandId(id);
            handService.deleteOne(classHand);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hands with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("hand with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromLabel the boolean isHuman with value 'true' for ?isHuman=true

    // /Hands?id=1,2,3,4
    @DeleteMapping(value = "/hands", params = { "id" } )
    public ResponseEntity deleteHandsById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        hand classHand = new hand();

        try {
            handService.deleteAllByIds(classHand, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hands with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("hand with ?id= : " + ids + " deleted");
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
