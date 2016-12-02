package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IPlayerService;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
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
public class PlayerResourceTest {

    @InjectMocks
    private PlayerResource resourceTest = new PlayerResource();

    //@Mock
    //private AbcEvent setAbcEventMock;

    @Mock
    private IPlayerService playerService;
    @Mock
    private Player player = new Player();
    @Mock
    private List<Player> players = new ArrayList<>();

    private TestFlowDto flowDto;
    final int playerId = 1;

    @Before
    public void setUp() {
        flowDto = new TestFlowDto();
        player.setPlayerId(playerId);
        
        players = new ArrayList<>();
        players.add(player);

        // when(AbcEventMock.fireEvent(flowDto)).thenReturn(EventOutput.success());

        when(playerService.findOne(playerId)).thenReturn(player);
        when(playerService.findAll(anyString(), anyString())).thenReturn(players);
    }

    @Test
    public void call_getPlayer_OK() throws Exception {
        final ResponseEntity result = this.resourceTest.getPlayer(playerId);
    
        
        String body = result.getBody().toString();
	    org.springframework.http.MediaType contentType = result.getHeaders().getContentType();
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
