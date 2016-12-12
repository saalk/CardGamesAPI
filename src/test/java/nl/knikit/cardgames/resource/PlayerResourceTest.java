package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerResourceTest {

    @InjectMocks
    private PlayerResource resourceTest = new PlayerResource();

    //@Mock
    //private AbcEvent setAbcEventMock;

    @Mock
    private IPlayerService playerService;
	
	@Mock
	private ModelMapperUtil mapUtil;
	
    private Player playerFixture = new Player();
    private PlayerDto playerDtoFixture = new PlayerDto();
    private List<Player> playersFixture = new ArrayList<>();
	
	private TestFlowDto flowDto;

    @Before
    public void setUp() {
    	
    	// Given for GET
        flowDto = new TestFlowDto();
	    playerFixture.setPlayerId(1);
	    playerFixture.setCreated("1");
	    playerFixture.setAlias("John 'Test' Doe");
	    playerFixture.setHuman(true);
	    playerFixture.setAiLevel(AiLevel.HUMAN);
	    playerFixture.setAvatar(Avatar.ELF);
	    playerFixture.setCubits(1);
	    playerFixture.setSecuredLoan(1);
	    ArrayList<Game> games = new ArrayList<>();
	    games.add(new Game());
	    games.add(new Game());
	    games.add(new Game());
	    playerFixture.setGames(games);
	    when(playerService.findOne(anyInt())).thenReturn(playerFixture);
	    
	    // Given for GET, DELETE
		playersFixture = new ArrayList<>();
        playersFixture.add(playerFixture);
	    when(playerService.findAll(anyString(), anyString())).thenReturn(playersFixture);
	    when(playerService.findAllWhere(anyString(), anyString())).thenReturn(playersFixture);
	    
	    // Given for POST, PUT
	    playerDtoFixture.setPlayerId(2);
	    playerDtoFixture.setCreated("2");
	    playerDtoFixture.setAlias("John 'DtoTest' Doe");
	    playerDtoFixture.setHuman(false);
	    playerDtoFixture.setAiLevel(AiLevel.LOW);
	    playerDtoFixture.setAvatar(Avatar.GOBLIN);
	    playerDtoFixture.setName(); // extra field "Script Joe(Human|Smart) [Elf]"
	    playerDtoFixture.setCubits(2);
	    playerDtoFixture.setSecuredLoan(2);
	    ArrayList<GameDto> gamesDto = new ArrayList<>();
	    gamesDto.add(new GameDto());
	    gamesDto.add(new GameDto());
	    gamesDto.add(new GameDto());
	    gamesDto.add(new GameDto());
	    playerDtoFixture.setGameDtos(gamesDto);
	    playerDtoFixture.setWinCount();  // extra field
	
	    // TODO add when then for create, update in PlayerResourceTest
	
    }

    @Test
    public void call_getPlayer_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getPlayer(1);
        
        //String body = result.getBody().toString();
	    //org.springframework.http.MediaType contentType = result.getHeaders().getContentType();

        HttpStatus statusCode = result.getStatusCode();
        int statusCodeValue = result.getStatusCodeValue();
        
	    // message, expected, actual
        assertEquals("GET /api/players/{playerId} should result in HTTP status OK", HttpStatus.OK, statusCode);
        assertEquals("GET /api/players/{playerId} should result in HTTP status value 200", 200, statusCodeValue);
        
        //assertEquals("GET /api/players/{playerId} should result with MediaType " + MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, contentType);
        //assertEquals("GET /api/players/{playerId} should result in a player with playerId {playerId}", "player", body);
        
    }

    @Test
    public void call_getPlayers_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getPlayers();
        assertEquals("GET /Players should result in HTTP status 200", 200, result.getStatusCodeValue());
    }

    @Test
    public void test_getMethodAnnotations() throws Exception {
        final Method method = this.resourceTest.getClass()
                                            .getMethod("getPlayers");
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
        //assertThat("The path is /api/players", path.value(), is("/api/players"));
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
