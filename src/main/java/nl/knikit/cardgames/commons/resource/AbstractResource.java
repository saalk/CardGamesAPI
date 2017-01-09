package nl.knikit.cardgames.commons.resource;

import nl.knikit.cardgames.service.IGameService;

import org.springframework.validation.Validator;

import javax.annotation.Resource;


public abstract class AbstractResource {

	@Resource
	private IGameService gameService;
	
    @Resource
    private Validator validator;

	public final IGameService getRequestService() {
		return gameService;
	}

	/**
	 * Checks if the game is initiated by the current player
	 *
	 * @param channelContext User context as received from Logon
	 * @param gameId requestId from the GET/POST http request
	 */
//	protected final boolean verifyRequest(final String channelContext, final String gameId) {
//
//		/**
//		 * Verifies if the customer is requesting action on his own request. Fetches the requestorId of the request saved in the
//		 * database, and compares it with customerId coming in from channelContext.
//		 *
//		 * @param requestId      id of the incoming request
//		 * @param channelContext context of the current request
//		 * @return true when verification succeeded; false when not
//		 */
//		Boolean verifyRequest(final String requestId, final ChannelContext channelContext);
//
//
//
//		if (!gameService.verifyRequest(gameId, channelContext)) {
//			auditDelegate.fireSecurityEvent("The user does not match with the request.");
//			return false;
//		}
//		return true;
//	}

	
    /**
     * Validates the input
     * @param field - input field
     */
	// use of Validator

}
