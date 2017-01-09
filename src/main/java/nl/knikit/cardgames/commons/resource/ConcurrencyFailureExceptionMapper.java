package nl.knikit.cardgames.commons.resource;

import nl.knikit.cardgames.response.CardGameResponse;

import org.springframework.dao.ConcurrencyFailureException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Custom exception handler to handle ConcurrencyFailureException , created by cl94wq on 26-01-16.
 */
@Provider
public class ConcurrencyFailureExceptionMapper implements ExceptionMapper<ConcurrencyFailureException> {

  @Override
  public Response toResponse(ConcurrencyFailureException e) {
    final CardGameResponse.CardGameResponseBuilder responseBuilder = CardGameResponse.builder();
    responseBuilder.build();
    responseBuilder.errorCode(ResourceConstants.ERROR_CODE_CONCURRENT_CARDGAME);
    return Response.status(Response.Status.OK).entity(responseBuilder).build();
  }

}
