/*
package nl.knikit.cardgames.controller;

import nl.knikit.cardgames.Service.PlayerService;
import nl.knikit.cardgames.model.Player2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

*/
/*
 * Created by Gebruiker on 24-7-2016.
 * (3)
 * This REST homecontroller implements the controller for which a bean is created in (1)
 *//*



@Path("/api")
@RequestScoped
public class HomeController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        return new ModelAndView("api");
    }

    @Autowired
    private nl.knikit.cardgames.Service.PlayerService PlayerService;

    @GetMapping("/api/players")
    public List getPlayers() {
        return PlayerService.list();
    }

    @GetMapping("/api/players/{id}")
    public ResponseEntity getPlayer(@PathVariable("id") int id) {

        Player2 player = PlayerService.get(id);
        if (player == null) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(player, HttpStatus.OK);
    }

    @PostMapping(value = "/api/players")
    public ResponseEntity createPlayer(@RequestBody Player2 player) {

        PlayerService.create(player);

        return new ResponseEntity(player, HttpStatus.OK);
    }

    @DeleteMapping("/api/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable int id) {

        if (-1 == PlayerService.delete(id)) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(id, HttpStatus.OK);

    }

    @PutMapping("/api/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable int id, @RequestBody Player2 player) {

        player = PlayerService.update(id, player);

        if (null == player) {
            return new ResponseEntity("No Player found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(player, HttpStatus.OK);
    }

}


*/
