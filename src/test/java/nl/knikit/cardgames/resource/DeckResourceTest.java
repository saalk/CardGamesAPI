package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeckResourceTest {

    @InjectMocks
    private DeckResource resourceTest = new DeckResource();

    //@Mock
    //private AbcEvent setAbcEventMock;

    @Mock
    private IDeckService deckService;
    
    @Mock
    private IGameService gameService;
    
    @Mock
    private IPlayerService playerService;
    
    private Deck deck = new Deck();
    private Player dealtTo = new Player();
    private Game game = new Game();
    
    @Mock
    private List<Deck> decks = new ArrayList<>();

    private TestFlowDto flowDto;
    final int deckId = 1;
    final int gameId = 2;
    final int playerId = 3;
    
    @Before
    public void setUp() {
        flowDto = new TestFlowDto();
        deck.setDeckId(deckId);

        game.setGameId(gameId);
        deck.setGame(game);
    
        dealtTo.setPlayerId(playerId);
        deck.setDealtTo(dealtTo);
        
        decks = new ArrayList<>();
        decks.add(deck);

        // when(AbcEventMock.fireEvent(flowDto)).thenReturn(EventOutput.success());
    
        when(gameService.findOne(gameId)).thenReturn(game);
        when(playerService.findOne(playerId)).thenReturn(dealtTo);
    
    }

    @Test
    public void call_getDeck_OK() throws Exception {
        when(deckService.findOne(deckId)).thenReturn(deck);
    
        final ResponseEntity result = this.resourceTest.getDeck(deckId);
        
        String body = result.getBody().toString();
	    org.springframework.http.MediaType contentType = result.getHeaders().getContentType();
        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
	    // message, expected, actual
        assertEquals("GET /api/decks/{deckId} should result in HTTP status OK", HttpStatus.OK, statusCode);
        assertEquals("GET /api/decks/{deckId} should result in HTTP status value 200", 200, statusCodeValue);
        
        //assertEquals("GET /api/decks/{deckId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/decks/{deckId} should result in a deck with deckId {deckId}", "deck", body);
        
    }

    @Test
    public void call_getDecks_OK() throws Exception {
        
        when(deckService.findAll((String) any(), anyString())).thenReturn(decks);
    
        final ResponseEntity result = this.resourceTest.getDecks();
    
        assertEquals("GET /Decks should result in HTTP status 200", 200, result.getStatusCodeValue());
    }
    
    @Test
    public void call_postDeck_noShuffle_OK() throws Exception {
        when(deckService.create((Deck) any())).thenReturn(deck);
    
        final ResponseEntity result = this.resourceTest.createDeck(deck, "false");
        
        String body = result.getBody().toString();
        org.springframework.http.MediaType contentType = result.getHeaders().getContentType();
        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
        // message, expected, actual
        assertEquals("POST /api/decks should result in HTTP status CREATED", HttpStatus.CREATED, statusCode);
        assertEquals("POST /api/decks should result in HTTP status value 201", 201, statusCodeValue);
        
        //assertEquals("GET /api/decks/{deckId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/decks/{deckId} should result in a deck with deckId {deckId}", "deck", body);
        
    }
    
    @Test
    public void call_postDeck_shuffle_OK() throws Exception {
	    when(deckService.create((Deck) any())).thenReturn(deck);
	
	    final ResponseEntity result = this.resourceTest.createDeck(deck, "true");
        
        String body = result.getBody().toString();
        org.springframework.http.MediaType contentType = result.getHeaders().getContentType();
        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
        // message, expected, actual
        assertEquals("POST /api/decks should result in HTTP status CREATED", HttpStatus.CREATED, statusCode);
        assertEquals("POST /api/decks should result in HTTP status value 201", 201, statusCodeValue);
        
        //assertEquals("GET /api/decks/{deckId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/decks/{deckId} should result in a deck with deckId {deckId}", "deck", body);
        
    }
    
    @Test
    public void test_getMethodAnnotations() throws Exception {
        final Method method = this.resourceTest.getClass()
                                            .getMethod("getDecks");
        assertThat("The method has the GET MAPPING annotation", method.isAnnotationPresent(GetMapping.class));
        assertThat("The method produces JSon", method.isAnnotationPresent(Produces.class));

        final Produces produces = method.getDeclaredAnnotation(Produces.class);
        assertThat("The produced mediatype is application/json", produces.value()[0], is(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_PathAnnotation() throws Exception {
        assertNotNull(this.resourceTest.getClass()
                                    .getAnnotations());
//        assertThat("The resourceTest has the annotation Path", this.resourceTest.getClass()
  //                                                                         .isAnnotationPresent(Path.class));

        //final Path path = this.resourceTest.getClass()
        //                                .getAnnotation(Path.class);
        //assertThat("The path is /api/decks", path.value(), is("/api/decks"));
    }

    @Test
    public void test_Scope() throws Exception {
        assertNotNull(this.resourceTest.getClass()
                                    .getAnnotations());
        assertThat("The resourceTest has the annotation RequestScoped", this.resourceTest.getClass()
                                                                                    .isAnnotationPresent(
                                                                                            RequestScoped.class));
    }
    public class TestFlowDto { // implements XyzEvent.XyzEventDto

        private String number = "123456";

        public String getNumber() {
            return number;
        }
    }

}
