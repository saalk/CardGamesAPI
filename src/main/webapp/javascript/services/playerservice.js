angular.module('myApp')
        // players in controller is an instance: get(), query(), save()
        // here
       .service('playerService', ['$http', '$q', 'toastr', '$httpParamSerializerJQLike' ,
function ($http, $q, toastr, $httpParamSerializerJQLike){

         // interface
         var service = {
            cardGame: {},
            initGameForVisitor: initGameForVisitor,
            getGameStoredInService: getGameStoredInService,
            setupAiPlayerForGame: setupAiPlayerForGame,
            changeVisitorDetailsForGame: changeVisitorDetailsForGame,
            deleteAiPlayerForGame: deleteAiPlayerForGame
         };
         return service;

    // implementations

    function initGameForVisitor( human ) {
        
        if (human==="true") {
           humanOrAi = "human";
        } else {
           humanOrAi = "ai";

        }
        var request = $http({
            method: "post",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/init/" + humanOrAi + "?alias=Js Stranger&avatar=Elf&aiLevel=Human&securedLoan=0",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: "{}"
        });
        return( request.then( handleSuccess, handleError ) );
        
    }
    function setupAiPlayerForGame( suppliedCardGame, ai ) {

        var request = $http({
            method: "post",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/" + suppliedCardGame.gameId + "/setup/ai?alias=Js Ai&avatar=Goblin&aiLevel=Medium&securedLoan=0",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: "{}"
       });
       return( request.then( handleSuccess, handleError ) );
    }
    function changeVisitorDetailsForGame( suppliedCardGame, player ) {
        var request = $http({
            method: "put",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/" + suppliedCardGame.gameId + "/setup/players/" + player.playerId,
            headers: {'Content-Type': 'application/json'},   
            //
            //            params: {
//                action: "update"
//            },
           data: player.visitor
        });
        return( request.then( handleSuccess, handleError ) );
    }
    function deleteAiPlayerForGame( suppliedCardGame, player ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: "http://knikit.nl/api/cardgames/" + suppliedCardGame.gameId + "/setup/ai/" + player.playerId,
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
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
        cardGame.push(response.data);
        return cardGame;
    }

    // retrieve the cardgame that is pushed in this service
    function getGameStoredInService() {
        return cardGame;
    }

}]);
