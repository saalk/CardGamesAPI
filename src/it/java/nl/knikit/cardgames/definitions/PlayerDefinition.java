package nl.knikit.cardgames.definitions;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import nl.knikit.cardgames.model.Player;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import response.ErrorResponse;
import response.SelectResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Represents a container for general step definitions
 */

@Slf4j
public class PlayerDefinition {

    @Autowired
    @Getter
    private HttpClient httpClient;

    @Autowired
    private Player cukesPlayer;


    protected final String CONTEXT_ROOT;

    protected PlayerDefinition(){
        Properties properties = new Properties();
        try {
            properties.load(
                    (InputStream) ClassLoader.getSystemClassLoader().getResource("cukestest.properties").getContent());
        } catch (IOException e) {
            throw new RuntimeException("Please provide the file: cukestest.properties in your resources context and set test env correctly" ,e);
        }
        this.CONTEXT_ROOT = properties.getProperty("api-url-endpoint");
    }

    public void clearState() throws Throwable {
        //cukesPlayer.clearState();
    }

    private void logOn(String typeOfVisitor, Map<String, String> headers) {

        String cookie = "visitorIdIsOne";
        validateValueForProperty("cookie", cookie);
        log.info("using cookie {}", cookie);

       // logOnProvider.logOn(TestUsers.player(cookie));

        if(headers == null) {
            headers = generateRequestHeaders(cookie);
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            //apiVisitor.addPersistentHeader(entry.getKey(), entry.getValue());
        }
    }

    public HttpResponse iPostToWithBodyWithReturn(String url, String body) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String newUrl = replaceWithPlayerRelatedData(url);
        final HttpPost player = new HttpPost(getUrlWithContextRoot(newUrl));

        StringEntity entity = new StringEntity(replaceWithPlayerRelatedData(body), ContentType.APPLICATION_JSON);
        player.setEntity(entity);

        return httpClient.execute(player);
    }

    @When("GET ([^\"\\s]*)$")
    public void iGet(String url) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        String newUrl = replaceWithPlayerRelatedData(url);
        httpClient.execute(new HttpGet(getUrlWithContextRoot(newUrl)));
    }

    @When("GET absolute url ([^\"\\s]*)$")
    public void iGetWithAbsoluteUrl(String url) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        try {
            URL theURL = new URL(url);
            httpClient.execute(new HttpHost( theURL.getHost(), theURL.getPort() ), new HttpGet(theURL.getPath()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Can not parse URL: " + url ,e);
        }
    }

    @When("GET ([^\"\\s]*) with header name ([^\"\\s]*) and value ([^\"\\s]*)$")
    public void iGetWithHeader(String url, String headerName, String headerValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        String newUrl = replaceWithPlayerRelatedData(url);
        HttpGet get = new HttpGet(getUrlWithContextRoot(newUrl));
        get.setHeader(headerName, replaceWithPlayerRelatedData(headerValue));
        httpClient.execute(get);
    }

    private String replaceWithPlayerRelatedData(String httpData) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> toBeReplacedValues = getPropertiesToBeReplaced(httpData);
        return replace(httpData, toBeReplacedValues);
    }

    private String replace(String url, List<String> toBeReplacedValues) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (String toBeReplaceValue : toBeReplacedValues ) {
            //Method method = Player.class.getDeclaredMethod(toBeReplaceValue, null);
            //String value = (String) method.invoke(mingplayerDataStorage.getMingplayerData(), null);

            //validateValueForProperty(toBeReplaceValue, value);

            //url = url.replaceAll("\\("+toBeReplaceValue+"\\)", value);
        }
        return url;
    }

    private List<String> getPropertiesToBeReplaced(String url) {
        Matcher matcher = Pattern.compile("\\(([^\\)]+)").matcher(url);
        List<String> toBeReplacedValues = new ArrayList<String>();

        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            toBeReplacedValues.add(matcher.group(1));
        }
        return toBeReplacedValues;
    }

    private void validateValueForProperty(String property, String value) {
        if (StringUtils.isEmpty(value)) {
            String  message = String.format("No value for %s", property);
            throw new IllegalArgumentException(message);
        }
    }

    private String getUrlWithContextRoot(String url) {
        return CONTEXT_ROOT.concat(url);
    }

    @When("^POST to ([^\"\\s]*) with no body$")
    public void iPostTo(String url) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        iPostToWithBody(url, "");
    }

    @When("POST to ([^\"\\s]*) with body:$")
    public void iPostToWithBody(String url, String body) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        iPostToWithBodyWithReturn(url, body);
    }

    @When("POST (\\d+) times to ([^\"\\s]*) with body:$")
    public void iPostToWithBodyMultipleTimes(int amountOfTimes, String url, String body) throws IOException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < amountOfTimes; i++) {
            iPostToWithBodyWithReturn(url, body);
        }
    }

    @And("^response should be like:$")
    public void responseShouldBeLike(String response) {
        //assertThat(httpClient.mostRecentResponse().getEntityString(), is(equalTo(response)));
    }

    @And("^json should be exactly like:$")
    public void jsonShouldBeExactlyLike(String form) {
        // remove any unwanted characters

        //JsonElement expectedForm = HttpResponse.getGson().fromJson(form, JsonElement.class);
        //assertThat(cukesPlayer.mostRecentResponse().getAsJsonElement().toString(), equalTo(expectedForm.toString()));
    }


    private String getAttributeAsString(JsonObject obj, String attr) {
        Set<Map.Entry<String, JsonElement>> set = obj.entrySet();
        for(Map.Entry<String, JsonElement> e : set) {
            if (e.getKey().equals(attr)) {
                return e.getValue().getAsString();
            }
        }
        return null;
    }

    private JsonObject getAttributeAsJsonObject(JsonObject obj, String attr) {
        Set<Map.Entry<String, JsonElement>> set = obj.entrySet();
        for(Map.Entry<String, JsonElement> e : set) {
            if (e.getKey().equals(attr)) {
                return e.getValue().getAsJsonObject();
            }
        }
        return null;
    }

    @And("^player id should have been created:$")
    public void playerIdShouldHaveBeenCreated(String expected) {
        HttpResponse httpResponse = (HttpResponse) httpClient;
        //JsonObject responseObj = httpResponse.getAsJsonElement().getAsJsonObject();

        //String gameId = getAttributeAsString(responseObj, "gameId");
        //assertThat(gameId, is(notNullValue()));

    }


    @And("^json string should contain:$")
    public void jsonStringShouldContain(String expected) {
        //MatcherAssert.assertThat(this.httpClient.mostRecentResponse().getEntityString(), Matchers.containsString(expected.trim()));
    }

    @And("^creditcard json should be:$")
    public void creditcardJsonShouldBe(String expected) {
        HttpResponse httpResponse = (HttpResponse) httpClient;
        //JsonObject responseObj = ingHttpResponse.getAsJsonElement().getAsJsonObject();

        //JsonObject creditCardObj = getAttributeAsJsonObject(responseObj, "creditCard");
        // remove any unwanted characters
        //JsonElement expectedCreditcardObj = httpResponse.getGson().fromJson(expected, JsonElement.class);
        //assertThat(String.valueOf(creditCardObj), equalTo(expectedCreditcardObj.toString()));
    }

    @And("^agreement json should be:$")
    public void agreementJsonShouldBe(String expected) {
        HttpResponse httpResponse = (HttpResponse) httpClient;
        //JsonObject responseObj = httpResponse.getAsJsonElement().getAsJsonObject();

        //JsonObject agreementObj = getAttributeAsJsonObject(responseObj, "agreement");
        // remove any unwanted characters
        //JsonElement expectedAgreementObj = httpResponse.getGson().fromJson(expected, JsonElement.class);
        //assertThat(expectedAgreementObj.toString(), equalTo(expectedAgreementObj.toString()));
    }

    @Then("^json should contain cramId$")
    public void json_should_contain_cramId() throws Throwable {
        HttpResponse httpResponse = (HttpResponse) httpClient;
        //JsonObject responseObj = httpResponse.getAsJsonElement().getAsJsonObject();

        //String cramId = getAttributeAsString(responseObj, "cramId");
        //assertThat(cramId, notNullValue());
        //assertThat(cramId, not(""));
    }


    private void addPlayerHeaders(final String playerId) {
        for (Map.Entry<String, String> entry : generateRequestHeaders(playerId).entrySet()) {
            //httpClient.addPersistentHeader(entry.getKey(), entry.getValue());
        }
    }

    private Map<String, String> generateRequestHeaders(String rgb) {

        // using apache HttpClient native API 4.3
        Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        // Lists from google
        List<Header> headers = Lists.newArrayList(header);
        // HttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();
        // HttpUriRequest request = RequestBuilder.get().setUri(SAMPLE_URL).build();
        // client.execute(request);

        return (Map<String, String>) header;
    }

    @Then("^an active session for ([^\"\\s]*) with a playerId which is not for this player$")
    public void an_active_session_with_the_previous_playerId(String typeOfVisitor) throws Throwable {
        //final String previousPlayerId = playerDataStorage.getMingplayerData().playerId();
        //cukesPlayer.clearState();
        logOn(typeOfVisitor, null);
        //playerDataStorage.getMingplayerData().playerId(previousPlayerId);
    }

    @Then("^we set the retrieved playerId to be used in other calls$")
    public void we_set_the_retrieved_playerId_to_be_used_in_other_calls() throws Throwable {
        //SelectResponse selectResponse = (SelectResponse) httpClient.getAs(new TypeToken<SelectResponse>() {
        //        }.getType());
        //assertThat(selectResponse, Is.is(IsNull.notNullValue()));

        //final playerData mingplayerData = mingplayerDataStorage.getMingplayerData();

        //log.debug("setting {}", selectResponse.getPlayerId());
        //mingplayerData.playerId(selectResponse.getPlayerId());
    }

    /**
     * Asserting with json exact is not feasible to use (playerId is always different). Therefore this explicit check is needed.
     *
     * @param errorCode
     * @throws Throwable
     */
    @Then("^the errorCode should be (\\d+)$")
    public void the_errorCode_should_be(int errorCode) throws Throwable {
    /*    SelectResponse selectResponse = cukesPlayer.mostRecentResponse()
                .getAs(new TypeToken<SelectResponse>() {
                }.getType());


        assertThat(selectResponse.getErrorCode(),Is.is(errorCode));
    */
    }

    @Then("^the errorId should be (\\d+)$")
    public void the_errorId_should_be(int errorId) throws Throwable {
        /*ErrorResponse errorResponse = cukesPlayer;
                .getAs(new TypeToken<ErrorResponse>() {
                }.getType());


        assertThat(errorResponse.getErrorId(),Is.is(errorId));*/
    }
}
