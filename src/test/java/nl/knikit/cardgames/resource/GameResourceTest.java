package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.IGameService;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameResourceTest {

    @InjectMocks
    private GameResource resourceTest = new GameResource();

    //@Mock
    //private AbcEvent setAbcEventMock;

    @Mock
    private IGameService gameService;
    
    
    @Mock
    private ModelMapperUtil mapUtil;
    
    private Game gameFixture = new Game();
    private GameDto gameDtoFixture = new GameDto();
    private List<Game> gamesFixture = new ArrayList<>();

    private TestFlowDto flowDto;

    @Before
    public void setUp() {
    
        // Given for GET
        flowDto = new TestFlowDto();
        gameFixture.setGameId(1);
        gamesFixture = new ArrayList<>();
        gamesFixture.add(gameFixture);
        when(gameService.findOne(anyInt())).thenReturn(gameFixture);
    
        // Given for GET, DELETE
        when(gameService.findAll(anyString(), anyString())).thenReturn(gamesFixture);
        when(gameService.findAllWhere(anyString(), anyString())).thenReturn(gamesFixture);
    
        // Given for POST, PUT
        gameDtoFixture.setGameId(1);
    
    
    }

    @Test
    public void call_getGame_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getGame(1);
        
//      String body = result.getBody().toString();
//	    org.springframework.http.MediaType contentType = result.getHeaders().getContentType();

        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
	    // message, expected, actual
        assertEquals("GET /api/games/{gameId} should result in HTTP status OK", HttpStatus.OK, statusCode);
        assertEquals("GET /api/games/{gameId} should result in HTTP status value 200", 200, statusCodeValue);
        
        //assertEquals("GET /api/gamesFixture/{gameId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/gamesFixture/{gameId} should result in a gameFixture with gameId {gameId}", "gameFixture", body);
        
    }

    @Test
    public void call_getGames_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getGames();
        assertEquals("GET /Games should result in HTTP status 200", 200, result.getStatusCodeValue());
    }
    
    @Test
    public void test_getMethodAnnotations() throws Exception {
        final Method method = this.resourceTest.getClass()
                                            .getMethod("getGames");
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
        //assertThat("The path is /api/gamesFixture", path.value(), is("/api/gamesFixture"));
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
