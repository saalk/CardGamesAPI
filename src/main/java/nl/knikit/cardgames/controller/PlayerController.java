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

    REST Endpoint 	HTTP Method 	Description
    /customers 	    GET 	        Returns the list of customers
    /customers/{id} GET 	        Returns customer detail for given customer {id}
    /customers 	    POST 	        Creates new customer from the post data
    /customers/{id} PUT 	        Replace the details for given customer {id}
    /customers/{id} DELETE 	        Delete the customer for given customer {id}
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.knikit.cardgames.Service.PlayerService;
import nl.knikit.cardgames.model.Player2;

import java.util.List;

@RestController
@Component
public class PlayerController {

    @Autowired
    private PlayerService PlayerService;

    @GetMapping("/players")
    public List getPlayers() {
        return PlayerService.list();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity getPlayer(@PathVariable("id") int id) {

        Player2 player = PlayerService.get(id);
        if (player == null) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(player, HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity createPlayer(@RequestBody Player2 player) {

        PlayerService.create(player);

        return new ResponseEntity(player, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable int id) {

        if (-1 == PlayerService.delete(id)) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(id, HttpStatus.OK);

    }

    @PutMapping("/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable int id, @RequestBody Player2 player) {

        player = PlayerService.update(id, player);

        if (null == player) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(player, HttpStatus.OK);
    }
}
