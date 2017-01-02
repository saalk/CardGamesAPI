package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;

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

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = SpringWebAppInitializer.class)
@WebAppConfiguration
//@IntegrationTest
@Controller
@Scope("prototype")
public class SpringIntegrationTest {
	
	
	static ResponseResults latestResponse = null;
	
	static String latestCardGamesID = "";
	
	static String latestPlayersID = "";
	static String latestPlayersID2 = "";
	static String latestGamesID = "";
	static String latestCasinosID = "";
	static String latestHandsID = "";
	static String latestDecksID = "";
	
	static List<String> latestCardGamesIDs = new ArrayList<>();
	
	static List<String> latestPlayersIDs = new ArrayList<>();
	static List<String> latestGamesIDs = new ArrayList<>();
	static List<String> latestCasinosIDs = new ArrayList<>();
	static List<String> latestHandsIDs = new ArrayList<>();
	static List<String> latestDecksIDs = new ArrayList<>();
		
	private static String api = "http://localhost:8383/api";
	
	static String baseCardGamesUrl = api + "/cardgames";
	static String cardGamesUrlWithId = baseCardGamesUrl + "/{id}";
	
	static String allPlayersUrl = api + "/players";
	static String allGamesUrl = api + "/games";
	static String allCasinosUrl = api + "/casinos";
	static String allHandsUrl = api + "/hands";
	static String allDecksUrl = api + "/decks";
	
	static String playersUrl = api + "/players/";
	static String gamesUrl = api + "/games/";
	static String casinosUrl = api + "/casinos/";
	static String handsUrl = api + "/hands/";
	static String decksUrl = api + "/decks/";
	
	static String playersUrlWithId = api + "/players/{id}";
	static String gamesUrlWithId = api + "/games/{id}";
	static String casinosUrlWithId = api + "/casinos/{id}";
	static String handsUrlWithId = api + "/hands/{id}";
	static String decksUrlWithId = api + "/decks/{id}";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	@Autowired
	protected RestTemplate restTemplate;
	
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
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback( headers);
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
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback( headers);
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
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback( headers);
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
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback( headers);
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
		
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback( headers);
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