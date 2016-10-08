angular.module('myApp')
        // players in controller is an instance: get(), query(), save()
        // here
       .service('playerService', ['$http', '$q', 'toastr',
function ($http, $q, toastr){           

    var baseUrl =  "" ; // "localhost:8181"
    //$http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w';
    // Return public API
    return({
            addPlayer: addPlayer,
            getPlayers: getPlayers,
            updatePlayer: updatePlayer,
            removePlayer: removePlayer
        });
    // ---
    // PUBLIC METHODS.
    // ---
    // get all players using the server' api
    function getPlayers() {
        // then() returns a new promise. We return that new promise.
        // that new promise is resolved via response.data, 
        // i.e. the players in the private method handleSuccess

        var request = $http({
                method: "get",
                url: baseUrl + "/api/players",
                headers: {
                    'Content-Type': 'application/json'
                },
//                params: {
//                    action: "get"
//                }
            });
        return( request.then( handleSuccess, handleError ) );
    }
    // add a player with the given details using the server' api
    function addPlayer( player ) {
       var request = $http({
           method: "post",
           url: baseUrl + "/api/players",
           headers: {
                    'Content-Type': 'application/json'
           },
//           params: {
//               action: "add"
//           },
           data: player
       });
       return( request.then( handleSuccess, handleError ) );
    }
    // update the player with the given ID using the server' api
    function updatePlayer( player ) {
        var request = $http({
            method: "put",
            url: baseUrl + "/api/players/" + player.id,
//            params: {
//                action: "update"
//            },
           data: player
        });
        return( request.then( handleSuccess, handleError ) );
    }
    // remove the player with the given ID using the server' api
    function removePlayer( player ) {
        var request = $http({
            method: "delete",
            url: baseUrl + "/api/players/" + player.id
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
            toastr.error('An API error has occurred, please try again (later)', 'Error');
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
