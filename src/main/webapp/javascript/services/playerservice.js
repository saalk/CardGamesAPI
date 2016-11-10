angular.module('myApp')
        // players in controller is an instance: get(), query(), save()
        // here
       .service('playerService', ['$http', '$q', 'toastr', '$httpParamSerializerJQLike' ,
function ($http, $q, toastr, $httpParamSerializerJQLike){           

    //var baseUrl =  "http://localhost:8383";
    var baseUrl = "http://knikit.nl";
    //$http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w';
    // Return public API
    return({
            initPlayerForIsHuman: initPlayerForIsHuman,
            addPlayer: addPlayer,
            getPlayers: getPlayers,
            getPlayersWhere: getPlayersWhere,
            updatePlayer: updatePlayer,
            removePlayer: removePlayer,
            removePlayerById: removePlayerById,
            removePlayersById: removePlayersById
            
        });
    // ---
    // PUBLIC METHODS.
    // ---

    // init a human player using the server' api
    function initPlayerForIsHuman( isHuman ) {
        
        if (isHuman) {
           newPlayer = {"avatar": "ELF",
                "alias":"stranger", "isHuman" : true, "aiLevel": 'HUMAN',
                "cubits": 0, "securedLoan": 0};
        } else {
            newPlayer = {"avatar": "ELF",
                "alias":"alien", "isHuman" : false, "aiLevel": 'NONE',
                "cubits": 0, "securedLoan": 0};
        }
        
        var request = $http({
            method: "post",
            crossDomain: true,
            url: baseUrl + "/api/players",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: newPlayer
        });
        return( request.then( handleSuccess, handleError ) );
        
    }
    
    // get all players using the server' api
    function getPlayers() {
        // then() returns a new promise. We return that new promise.
        // that new promise is resolved via response.data, 
        // i.e. the players in the private method handleSuccess

        var request = $http({
                method: "get",
                crossDomain: true,
                url: baseUrl + "/api/players",
                headers: {'Content-Type': 'application/json'}
//                params: {
//                    action: "get"
//                }
            });
        return( request.then( handleSuccess, handleError ) );
    }
     
    // get all players for a condition using the server' api
    function getPlayersWhere( isHuman ) {

        var request = $http({
            method: "get",
            crossDomain: true,
            url: baseUrl + "/api/players?isHuman=" + isHuman,
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "get"
//            },
        });
        return( request.then( handleSuccess, handleError ) );

    }   
    // add a player with the given details using the server' api
    function addPlayer( player ) {
        var request = $http({
            method: "post",
            crossDomain: true,
            url: baseUrl + "/api/players",
            headers: {'Content-Type': 'application/json'},            //           params: {
            //               action: "add"
            //           },
            data: player
       });
       return( request.then( handleSuccess, handleError ) );
    }
    // update the player with the given \id using the server' api
    function updatePlayer( player ) {
        var request = $http({
            method: "put",
            crossDomain: true,
            url: baseUrl + "/api/players/" + player.playerId,
            headers: {'Content-Type': 'application/json'},   
            //
            //            params: {
//                action: "update"
//            },
           data: player
        });
        return( request.then( handleSuccess, handleError ) );
    }
    // remove the player with the given \id using the server' api
    function removePlayer( player ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/players/" + player.playerId,
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
        });
        return( request.then( handleSuccess, handleError ) );
    }
    // remove a player passed in the body using the server' api
    // TODO never tested
    function removePlayerById( player ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/players",
            params: player.playerId,
            paramSerializer: '$httpParamSerializerJQLike',
              headers: {'Content-Type': 'application/json'} 
              //            params: {
//                action: "delete"
//            },
        });
        return( request.then( handleSuccess, handleError ) );
    }

    // remove all players passed in the body using the server' api
    // TODO never tested
    function removePlayersById( players ) {
        var request = $http({
            method: "delete",
            crossDomain: true,
            url: baseUrl + "/api/players?id=",
            params: players.playerId,
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
