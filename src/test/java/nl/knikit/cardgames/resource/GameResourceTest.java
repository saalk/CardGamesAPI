package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;
import nl.knikit.cardgames.testdata.TestData;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
// @ContextConfiguration defines class-level metadata that is used to determine how to
// load and configure an ApplicationContext for integration tests.
//@ContextConfiguration(classes = { ApplicationContextConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GameResourceTest extends TestData{

    @InjectMocks
    private GameResource resourceTest = new GameResource();

    @Mock
    private IGameService gameService;
	
	@Mock
	private IPlayerService playerService;
	
    @Mock
    private ModelMapperUtil mapUtil = new ModelMapperUtil();
    
    @Before
    public void setUp() throws Exception {
    
        // Given for GET one
        when(gameService.findOne(anyInt())).thenReturn(MakeGameEntityWithIdAndDecksAndWinner(1,1,0));
        when(playerService.findOne(anyInt())).thenReturn(MakePlayerEntityWithIdAndGamesWon(10,0));
    
        // Given for GET all
	    List<Game> gameFixtures = new ArrayList<>();
	    gameFixtures.add(MakeGameEntityWithIdAndDecksAndWinner(2,2,10));
	    gameFixtures.add(MakeGameEntityWithIdAndDecksAndWinner(3,2,0));
        when(gameService.findAll(anyString(), anyString())).thenReturn(gameFixtures);
        when(gameService.findAllWhere(anyString(), anyString())).thenReturn(gameFixtures);
	
	    // Given for POST, PUT
	    when(gameService.update((Game) any())).thenReturn(MakeGameEntityWithIdAndDecksAndWinner(3,2,10));
	    when(gameService.create((Game) any())).thenReturn(MakeGameEntityWithIdAndDecksAndWinner(3,2,0));
	
	    when(mapUtil.convertToDto((Game) any())).thenReturn(MakeGameDtoWithIdAndDecksAndWinner(1,2,3));
	    when(mapUtil.convertToDto((Player) any())).thenReturn(MakePlayerDtoWithIdAndGamesWon(10,2));
	    
	    when(mapUtil.convertToEntity((GameDto) any())).thenReturn(MakeGameEntityWithIdAndDecksAndWinner(1,2,3));
	    when(mapUtil.convertToEntity((PlayerDto) any())).thenReturn(MakePlayerEntityWithIdAndGamesWon(10,2));
    }

    @Test
    public void call_getGame_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getGame(1);
        
//      String body = result.getBody().toString();
//	    org.springframework.http.MediaType contentType = result.getHeaders().getContentType();

        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
	    // message, expected, actual
        assertEquals("GET /api/gameDtos/{suppliedGameId} should result in HTTP status OK", HttpStatus.OK, statusCode);
        assertEquals("GET /api/gameDtos/{suppliedGameId} should result in HTTP status value 200", 200, statusCodeValue);
        
        //assertEquals("GET /api/gamesFixture/{suppliedGameId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/gamesFixture/{suppliedGameId} should result in a gameFixture with suppliedGameId {suppliedGameId}", "gameFixture", body);
        
    }

    @Test
    public void call_getGames_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getGames();
        assertEquals("GET /Games should result in HTTP status 200", 200, result.getStatusCodeValue());
    }
	
	@Test
	public void call_getGamesWhere_OK() throws Exception {
		final ResponseEntity result = this.resourceTest.getGamesWhere("HIGHLOW");
		assertEquals("GET /Games should result in HTTP status 200", 200, result.getStatusCodeValue());
	}
	
    @Test
    public void call_createGame_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.createGame(new GameDto());
        assertEquals("POST /Games should result in HTTP status CREATED(201)", 201, result.getStatusCodeValue());
    }
	
    // createGameWithDeck
	// updateGame
	@Test
	public void call_updateGameWithWinner_OK() throws Exception {
		final ResponseEntity result = this.resourceTest.updateGame(1,null,2);
		assertEquals("POST /Games should result in HTTP status OK(200)", 200, result.getStatusCodeValue());
	}
	
	@Test
	public void call_updateGameNoWinner_OK() throws Exception {
    	GameDto gameDto = new GameDto();
    	gameDto.setGameId(1);
		final ResponseEntity result = this.resourceTest.updateGame(1,gameDto,null);
		assertEquals("POST /Games should result in HTTP status OK(200)", 200, result.getStatusCodeValue());
	}
	
    @Test
    public void test_getMethodAnnotations() throws Exception {
        final Method method = this.resourceTest.getClass()
                                            .getMethod("getGames");
        assertThat("The method has the GET MAPPING annotation", method.isAnnotationPresent(GetMapping.class));
        //assertThat("The method produces JSon", method.isAnnotationPresent(Produces.class));

        final Produces produces = method.getDeclaredAnnotation(Produces.class);
        //assertThat("The produced mediatype is application/json", produces.value()[0], is(MediaType.APPLICATION_JSON));
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
