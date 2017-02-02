angular.module('myApp')
        // games in controller is an instance: get(), query(), save()
        // here
       .service('gameService', ['$http', '$q', 'toastr', '$httpParamSerializerJQLike' ,
function ($http, $q, toastr, $httpParamSerializerJQLike){           

          // interface
          var service = {
            cardGame: {},
            initGameForExistingVisitor: initGameForExistingVisitor,
            getGameDetails: getGameDetails,
            updateGame: updateGame,
            shuffleGame: shuffleGame,
            turnGame: turnGame
          };
          return service;


    // implementations

    function initGameForExistingVisitor( cardGame, visitor ) {

        var request = $http({
            method: "post",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/init/human/" + visitor.visitorId + "?gameType=" + cardGame.gameType + "&ante=" + cardGame.ante,
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: "{}"
        });
        return( request.then( handleSuccess, handleError ) );
        
    }
    
    function getGameDetails( cardGame ) {
        // then() returns a new promise. We return that new promise.
        // that new promise is resolved via response.data, 
        // i.e. the game in the private method handleSuccess

        var request = $http({
                method: "get",
                crossDomain: true,
                url: "http://knikit.nl/api/cardgames/" + cardGame.gameId ,
                headers: {'Content-Type': 'application/json'}
//                params: {
//                    action: "get"
//                }
            });
        return( request.then( handleSuccess, handleError ) );
    }
     
    function shuffleGame( cardGame, jokers ) {
        var request = $http({
            method: "post",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/" + cardGame.gameId + "/shuffle/cards?jokers=" + jokers ,
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: cardGame
       });
       return( request.then( handleSuccess, handleError ) );
    }

    function turnGame( cardGame, action ) {
        var request = $http({
            method: "post",
            crossDomain: true,
            url:"http://knikit.nl/api/cardgames/" + cardGame.gameId + "/turn/players/" + cardGame.currentPlayerId + "?action=" + action + "&total=1&cardLocation=Hand" ,
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: cardGame
       });
       return( request.then( handleSuccess, handleError ) );
    }

    function updateGame( cardGame ) {
        var request = $http({
            method: "put",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/" + cardGame.gameId + "/init?gameType=" + cardGame.gameType + "&ante=" + cardGame.ante,
            headers: {'Content-Type': 'application/json'},   
            //
            //            params: {
//                action: "update"
//            },
           data: cardGame
        });
        return( request.then( handleSuccess, handleError ) );
    }

    // PRIVATE METHODS.
    // transform the error response, unwrapping the application dta from
    // the API response payload.

    function handleError( response ) {
        // The API response from the server should be returned in a
        // nomralized format. However, if the request was not handled by the
        // server (or what not handles properly - ex. server error), then we
        // may have to normalize it on our end, as best we can.
        if (
            ! angular.isObject( response.data ) ||
            ! response.data.message
            ) {
            return( $q.reject( "An unknown error occurred." ) );
        }
        // Otherwise, use expected error message.
        return( $q.reject( response.data.message ) );
    }

    // transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {
        return( service.cardGame = response.data );
    }
}]);
