package nl.knikit.cardgames.definitions.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.commons.util.StackableResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = SpringWebAppInitializer.class)
@WebAppConfiguration
//@IntegrationTest
@Controller
@Scope("request")
public class SpringIntegrationTest {
	
	
	protected static ResponseResults latestResponse = null;
	
	protected static String latestCardGamesID = "";
	
	protected static String latestPlayersID = "";
	protected static String latestPlayersID2 = "";
	protected static String latestGamesID = "";
	protected static String latestCasinosID = "";
	protected static String latestHandsID = "";
	protected static String latestDecksID = "";
	
	protected static List<String> latestCardGamesIDs = new ArrayList<>();
	
	protected static List<String> latestPlayersIDs = new ArrayList<>();
	protected static List<String> latestGamesIDs = new ArrayList<>();
	protected static List<String> latestCasinosIDs = new ArrayList<>();
	protected static List<String> latestHandsIDs = new ArrayList<>();
	protected static List<String> latestDecksIDs = new ArrayList<>();
	
	private static String api = "http://localhost:8383/api";
	
	protected static String baseCardGamesUrl = api + "/cardgames";
	protected static String cardGamesUrlWithId = baseCardGamesUrl + "/{id}";
	
	protected static String allPlayersUrl = api + "/players";
	protected static String allGamesUrl = api + "/games";
	protected static String allCasinosUrl = api + "/casinos";
	protected static String allHandsUrl = api + "/hands";
	protected static String allDecksUrl = api + "/decks";
	
	protected static String playersUrl = api + "/players/";
	protected static String gamesUrl = api + "/games/";
	protected static String casinosUrl = api + "/casinos/";
	protected static String handsUrl = api + "/hands/";
	protected static String decksUrl = api + "/decks/";
	
	protected static String playersUrlWithId = api + "/players/{id}";
	protected static String gamesUrlWithId = api + "/games/{id}";
	protected static String casinosUrlWithId = api + "/casinos/{id}";
	protected static String handsUrlWithId = api + "/hands/{id}";
	protected static String decksUrlWithId = api + "/decks/{id}";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	@Autowired
	protected RestTemplate restTemplate;

	protected StackableResponses stackableResponses = new StackableResponses();
	
	protected void executeGet(String url) throws IOException {
		final Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		
		restTemplate.setErrorHandler(errorHandler);
		latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
	}
	
	protected void executePost(String url, String body) throws IOException {
		
		final Map<String, String> headers = new HashMap<>();
		//headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		requestCallback.setBody(body);
		
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		
		restTemplate.setErrorHandler(errorHandler);
		latestResponse = restTemplate.execute(url, HttpMethod.POST, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
		
	}
	
	protected void executePostWithUriAndQueryParam(String urlWithUriParams, Map<String, String> uriParams, String body, Map<String, String> queryParams) throws IOException {
		
		final Map<String, String> headers = new HashMap<>();
		//headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		
		// Make a uri and fill in the base Uri
		// resource/{uriParams}?queryParams=queryParams[0],queryParams[2], etc
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlWithUriParams);
		
		// iterate over a hashmap with a for loop passing the key and value into the builder.queryParam
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			// Add query parameters to the builder
			builder.queryParam(entry.getKey(), entry.getValue());
		}
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		requestCallback.setBody(body);
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		restTemplate.setErrorHandler(errorHandler);
		
		// Build the uri and fill in the uriParams eg. resource1/{uriParams for key}/resource2{uriParams for key}
		latestResponse = restTemplate.execute(builder.buildAndExpand(uriParams).toUri(), HttpMethod.POST, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
	
	}
	
	protected void executePut(String url, String body) throws IOException {
		
		final Map<String, String> headers = new HashMap<>();
		//headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		requestCallback.setBody(body);
		
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		
		restTemplate.setErrorHandler(errorHandler);
		latestResponse = restTemplate.execute(url, HttpMethod.PUT, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
		
	}
	
	protected void executePutWithUriAndQueryParam(String urlWithUriParams, Map<String, String> uriParams, String body, Map<String, String> queryParams) throws IOException {
		
		final Map<String, String> headers = new HashMap<>();
		//headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		
		// Make a uri and fill in the base Uri
		// resource/{uriParams}?queryParams=queryParams[0],queryParams[2], etc
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlWithUriParams);
		
		// iterate over a hashmap with a for loop passing the key and value into the builder.queryParam
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			// Add query parameters to the builder
			builder.queryParam(entry.getKey(), entry.getValue());
		}
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		requestCallback.setBody(body);
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		restTemplate.setErrorHandler(errorHandler);
		
		// Build the uri and fill in the uriParams eg. resource1/{uriParams for key}/resource2{uriParams for key}
		latestResponse = restTemplate.execute(builder.buildAndExpand(uriParams).toUri(), HttpMethod.PUT, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
		
	}
	
	protected void executeDelete(String url, String body) throws IOException {
		
		final Map<String, String> headers = new HashMap<>();
		//headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		requestCallback.setBody(body);
		
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
		
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		
		restTemplate.setErrorHandler(errorHandler);
		latestResponse = restTemplate.execute(url, HttpMethod.DELETE, requestCallback, new ResponseExtractor<ResponseResults>() {
			@Override
			public ResponseResults extractData(ClientHttpResponse response) throws IOException {
				if (errorHandler.hadError) {
					return (errorHandler.getResults());
				} else {
					return (new ResponseResults(response));
				}
			}
		});
	}
	
	private class ResponseResultErrorHandler implements ResponseErrorHandler {
		private ResponseResults results = null;
		private Boolean hadError = false;
		
		private ResponseResults getResults() {
			return results;
		}
		
		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			hadError = response.getRawStatusCode() >= 400;
			return hadError;
		}
		
		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			results = new ResponseResults(response);
		}
	}
}