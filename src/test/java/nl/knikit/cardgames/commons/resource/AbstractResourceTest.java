package nl.knikit.cardgames.commons.resource;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.IGameService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractResourceTest {
	
	@InjectMocks
	private DummyResource fixture;
	
	@Mock
	private IGameService iGameService;

	
	@Test
	public void request_findOne_is_successful() throws Exception {
		final int requestId = 1234;

	}
	
}