angular.module('myApp')
        .service('PlayerService', PlayerService); 
PlayerService.$inject = ['toastr'];
function PlayerService(toastr){

   var players = [];
   uid = 0;
   return {
        initPlayers: function() {
            if (players[0] == null) {
            players = [
                {id: 0, 'alias': 'stranger', 'aiLevel': 'Human', cubits: 0000, securedLoan: 0000},
                {id: 1, 'alias': 'alien 1', 'aiLevel': 'None', cubits: 0000, securedLoan: 0000},
                {id: 2, 'alias': 'alien 2', 'aiLevel': 'None', cubits: 0000, securedLoan: 0000}
                ];
                toastr.success('A new spaceship has landed...', 'Alpha landing bay');
            };
            return players;
        },
        listPlayers: function() {
            return players;
        },
        savePlayers: function(player) {
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
        },
        getPlayer: function(id) {
            // existing player
            for (var i in players) {
                if (players[i].id == id)
                return players[i];
            }
        },
        deletePlayers: function(id) {
            // existing player
            players.forEach(function () {
                if (players[i].id == id)
                players.splice(i, 1);
            });
        }
    }
};
