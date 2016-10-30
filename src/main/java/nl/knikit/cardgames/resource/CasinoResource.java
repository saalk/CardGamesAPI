package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.exception.CasinoNotFoundForIdException;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.service.ICasinoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@RestController
@Component
@ExposesResourceFor(Casino.class)
@Slf4j
@Scope("prototype")
public class CasinoResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private ICasinoService casinoService;

    @GetMapping("/casinos")
    public ResponseEntity<ArrayList<Casino>> getCasinos() {

        ArrayList<Casino> casinos;
        casinos = (ArrayList) casinoService.findAll("playingOrder", "ASC");

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
            casinoService.deleteOne(classCasino);
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
