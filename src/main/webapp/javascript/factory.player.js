// Set all the services on the `cardGamesApp` module using factories and not services
//
// Separating the data services moves the logic on how to get it to the data service, and lets the
// controller be simpler and more focused on the view.
//
// Strict mode changes previously accepted "bad syntax" into real errors.
// mistyping a variable name creates a new global variable. In strict mode, this will throw an
// error, making it impossible to accidentally create a global variable.

angular
    .module('cardGamesApp')
    .factory('playerservice', playerservice);

playerservice.$inject = ['$http', 'logger'];

function playerservice($http, logger) {

    uid = 0;

    var service = {
        listPlayers: listPlayers,
        savePlayer: savePlayer,
        getPlayer: getPlayer,
        deletePlayer: deletePlayer
    };

    return service;

    //var player[1] = {id: 0, name: 'first', aiLevel: 'Average', fiches: '0000'};
    //var player[2] = {id: 0, name: 'second', aiLevel: 'Dumb', fiches: '0000'};

    // initial call
    function listPlayers() {
        var players = [
            {id: 0, name: 'stranger', aiLevel: 'Human', fiches: '0000'},
            {id: 1, name: 'first', aiLevel: 'Average', fiches: '0000'},
            {id: 2, name: 'second', aiLevel: 'Dumb', fiches: '0000'}
            ];
        return players;
    /*
        return $http.get('/api/players')
            .then(getPlayersComplete)
            .catch(getPlayersFailed);

        function getPlayersComplete(response) {
            return response.data.results; // then() returns a promise object, not resp.data
        }

        function getPlayersFailed(error) {
            logger.error('XHR Failed for getPlayers.' + error.data);
        }
        or do return $http.post(urlBase + '/' + player.id, player)
     */
    }

    function savePlayer(player) {
        if (player.id == null) {
        // new player
           player.id = uid++;
           players.push(player);
        } else {
         // existing player
          for (var i in players) {
              if (players[i].id == player.id)
                 players[i] = player;}
        }
    };

    function getPlayer(id) {
        // existing player
        for (var i in players) {
            if (players[i].id == id)
            return players[i];}
    };

    function deletePlayer(id) {
        // existing player
        for (var i in players) {
            if (players[i].id == id)
            players.splice(i, 1);}

    }

};

