angular.module('myApp')
        // games in controller is an instance: get(), query(), save()
        // here
       .service('gameService', ['$http', '$q', 'toastr', '$httpParamSerializerJQLike' ,
function ($http, $q, toastr, $httpParamSerializerJQLike){           

    //var baseUrl =  "http://localhost:8383";
    var baseUrl = "http://knikit.nl";
    //$http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w';
    // Return public API
    return({
            initGameForType: initGameForType,
            addGame: addGame,
            getGames: getGames,
            getGamesWhere: getGamesWhere,
            updateGame: updateGame,
            removeGame: removeGame,
            removeGameById: removeGameById,
            removeGamesById: removeGamesById
            
        });
    // ---
    // PUBLIC METHODS.
    // ---

    // init a human game using the server' api
    function initGameForType( type ) {
 
        // TODO new game options:
        // 
        //    HILOW_1ROUND(Type.HIGHLOW, "One round double or nothing "),
        //    HILOW_3_IN_A_ROW_1SUIT(Type.HIGHLOW, "'3-in-a-row' one suit"),
        //    HILOW_5_IN_A_ROW(Type.HIGHLOW, "'5-in-a-row'");
    
        if (type === "HIGHLOW") {
           newGame = { "type": "HIGHLOW", "state": "IS_CONFIGURED", "maxRounds": 9, "minRounds": 1,
            "currentRound": 0, "maxTurns": 3, "minTurns": 1, "currentTurn": 0,
            "turnsToWin": 3, "ante": 50, "playerId": null };
        } else {
            // default HILOW_1ROUND rules
            //TODO deck: null needed?
           newGame = { "type": "HIGHLOW", "state": "IS_CONFIGURED", "maxRounds": 1, "minRounds": 1,
            "currentRound": 0, "maxTurns": 9, "minTurns": 1, "currentTurn": 0,
            "turnsToWin": 0, "ante": 50, "playerId": null };
        }
        
        var request = $http({
            method: "post",
            crossDomain: true,
            url: baseUrl + "/api/games",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: newGame
        });
        return( request.then( handleSuccess, handleError ) );
        
    }
    
    // get all games using the server' api
    function getGames() {
        // then() returns a new promise. We return that new promise.
        // that new promise is resolved via response.data, 
        // i.e. the games in the private method handleSuccess

        var request = $http({
                method: "get",
                crossDomain: true,
                url: baseUrl + "/api/games",
                headers: {'Content-Type': 'application/json'}
//                params: {
//                    action: "get"
//                }
            });
        return( request.then( handleSuccess, handleError ) );
    }
     
    // get all games for a condition using the server' api
    function getGamesWhere( type ) {

        var request = $http({
            method: "get",
            crossDomain: true,
            url: baseUrl + "/api/games?type=" + type,
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "get"
//            },
        });
        return( request.then( handleSuccess, handleError ) );

    }   
    // add a game with the given details using the server' api
    function addGame( game ) {
        var request = $http({
            method: "post",
            crossDomain: true,
            url: baseUrl + "/api/games",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: game
       });
       return( request.then( handleSuccess, handleError ) );
    }
    // update the game with the given \id using the server' api
    function updateGame( game ) {
        var request = $http({
            method: "put",
            crossDomain: true,
            url: baseUrl + "/api/games/" + game.gameId,
            headers: {'Content-Type': 'application/json'},   
            //
            //            params: {
//                action: "update"
//            },
           data: game
        });
        return( request.then( handleSuccess, handleError ) );
    }
    // remove the game with the given \id using the server' api
    function removeGame( game ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/games/" + game.gameId,
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
        });
        return( request.then( handleSuccess, handleError ) );
    }
    // remove a game passed in the body using the server' api
    // TODO never tested
    function removeGameById( game ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/games",
            params: game.gameId,
            paramSerializer: '$httpParamSerializerJQLike',
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
        });
        return( request.then( handleSuccess, handleError ) );
    }

    // remove all games passed in the body using the server' api
    // TODO never tested
    function removeGamesById( games ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/games?id=",
            params: games.gameId,
            paramSerializer: '$httpParamSerializerJQLike',
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    // ---
    // PRIVATE METHODS.
    // ---
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
        return( response.data );
    }
}]);
