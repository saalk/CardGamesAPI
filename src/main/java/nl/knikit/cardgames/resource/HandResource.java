package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Hand;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    private IHandService handService;
    
    @Autowired
    private IPlayerService playerService;
    
    @Autowired
    private ICasinoService casinoService;
    
   
    //@Autowired
    //private ICardService cardService;
    
    @GetMapping("/hands")
    public ResponseEntity<ArrayList<Hand>> getHands() {

        ArrayList<Hand> Hands;
        Hands = (ArrayList) handService.findAll("human", "DESC");
        return new ResponseEntity(Hands, HttpStatus.OK);
    }

    @GetMapping("/hands/{id}")
    public ResponseEntity getHand(
            @PathVariable("id") int id) {

        Hand Hand = handService.findOne(id);
        if (Hand == null) {
    
            return ResponseEntity
                           .status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(new ArrayList<>());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Hand);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean human
    // you fromLabel the boolean human with value 'true' for ?human=true

    @GetMapping(value = "/hands", params = { "casino" } )
    public ResponseEntity<ArrayList<Hand>> findAllWhere(
            @RequestParam(value = "casino", required = true) String param) {

        try {
            ArrayList<Hand> Hands = (ArrayList<Hand>) handService.findAllWhere("playerId", param);
            if (Hands == null || Hands.isEmpty()) {
                return ResponseEntity
                               .status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(new ArrayList<Hand>());
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
    
        // check for casino and player
        if ( !(Hand.getPlayer().getPlayerId()>0) ||
             !(Hand.getCasino().getCasinoId()>0) ||
             !(Hand.getCard().getCardId().isEmpty()) ) {
            
            return ResponseEntity
                           .status(HttpStatus.NOT_ACCEPTABLE)
                           .body("No Casino or Player or Card supplied for Hand to create " + Hand);
        }
        
        Casino currentCasino = casinoService.findOne(Hand.getCasino().getCasinoId());
        Player currentPlayer = playerService.findOne(Hand.getPlayer().getPlayerId());
        if (currentCasino == null || currentPlayer == null ) {
            return ResponseEntity
                           .status(HttpStatus.NOT_ACCEPTABLE)
                           .body("No Casino or Player supplied to relate Hand to: " + Hand);
        }

        handService.create(Hand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Hand);
    }

    @PutMapping("/hands/{id}")
    public ResponseEntity updateHand(
            @PathVariable int id, @RequestBody Hand Hand) {

        Hand newHand = handService.update(Hand);
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
            classHand.setHandId(id);
            handService.deleteOne(classHand);
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
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean human
    // you fromLabel the boolean human with value 'true' for ?human=true

    // /Hands?id=1,2,3,4
    @DeleteMapping(value = "/hands", params = { "id" } )
    public ResponseEntity deleteHandsById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Hand classHand = new Hand();

        try {
            handService.deleteAllByIds(classHand, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Hands with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Hand with ?id= : " + ids + " deleted");
    }
    
}
