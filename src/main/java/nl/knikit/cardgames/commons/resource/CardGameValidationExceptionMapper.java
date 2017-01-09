package nl.knikit.cardgames.commons.resource;

import nl.knikit.cardgames.commons.dao.InvalidCardGameException;
import nl.knikit.cardgames.response.CardGameResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by cl94wq on 22-02-16.
 */
public class CardGameValidationExceptionMapper implements ExceptionMapper<InvalidCardGameException> {

	@Override
	public Response toResponse(InvalidCardGameException e) {
		final CardGameResponse.CardGameResponseBuilder responseBuilder = CardGameResponse.builder();
		responseBuilder.build();
		responseBuilder.errorCode(ResourceConstants.ERROR_CODE_INVALID_CARDGAME);
		return Response.status(Response.Status.FORBIDDEN).entity(responseBuilder).build();
	}
}
