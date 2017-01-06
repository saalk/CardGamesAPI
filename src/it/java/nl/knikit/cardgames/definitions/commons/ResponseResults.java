package nl.knikit.cardgames.definitions.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseResults {
    private  final ClientHttpResponse theResponse;
    protected  final String body;
    
    ResponseResults(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();
        if (null == bodyInputStream) {
            this.body = "{}";
        } else {
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy(bodyInputStream, stringWriter, "UTF-8");
            this.body = stringWriter.toString();
        }
    }
    
    public ClientHttpResponse getTheResponse() {
        return theResponse;
    }
    
    public String getBody() {
        return body;
    }
}