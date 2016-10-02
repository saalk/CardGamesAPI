angular.module('myApp')
        .service('PlayerService', PlayerService); 
PlayerService.$inject = ['toastr', 'Restangular'];
function PlayerService(toastr, Restangular){

   var players = [];
   uid = 0;
   return {
        initPlayers: function() {
            if (players[0] == null) {
            players = [
                {id: 123,  'playerId': '160913-01:28-11', playingOrder: 0, 'origin': 'ELF', 'alias':
                'stranger', 'aiLevel': 'Human', cubits: 0000, securedLoan: 0000},
                {id: 234,  'playerId': '160913-01:28-12', playingOrder: 1, 'origin': 'ELF', 'alias':
                'alien1', 'aiLevel': 'None', cubits: 0000, securedLoan: 0000},
                {id: 345,  'playerId': '160913-01:28-13', playingOrder: 2, 'origin': 'ELF', 'alias':
                'alien2', 'aiLevel': 'None', cubits: 0000, securedLoan: 0000}
                ];
                toastr.success('A new spaceship has landed...', 'Alpha landing bay');
            };
            return players;
        },
        listPlayers: function() {
            Restangular.all('players').getList().then(function(players) {
            return players;
          });
        },
        savePlayer: function(player) {
            Restangular.post(player);
        },

        savePlayerInList: function(player) {
            if (player.playingOrder == null) {
            // new player
               player.playingOrder = uid++;
               players.push(player);
            } else {
             // existing player
              for (var i in players) {
                  if (players[i].playingOrder == player.playingOrder)
                     players[i] = player;}
            }
        },
        getPlayerByplayingOrder: function(playingOrder) {
            // existing player
            for (var i in players) {
                if (players[i].playingOrder == playingOrder)
                return players[i];
            }
        },
        deletePlayerByplayingOrder: function(playingOrder) {
            // existing player
            i = 0;
            players.forEach(function () {
                if (players[i].playingOrder == playingOrder) {
                players.splice(i, 1);
                i++;}
            });
        }
    }
};
