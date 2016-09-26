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
                {id: 123,  'created': '160913-01:28-11', sequence: 0, 'origin': 'ELF', 'alias':
                'stranger', 'aiLevel': 'Human', cubits: 0000, securedLoan: 0000},
                {id: 234,  'created': '160913-01:28-12', sequence: 1, 'origin': 'ELF', 'alias':
                'alien1', 'aiLevel': 'None', cubits: 0000, securedLoan: 0000},
                {id: 345,  'created': '160913-01:28-13', sequence: 2, 'origin': 'ELF', 'alias':
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
            if (player.sequence == null) {
            // new player
               player.sequence = uid++;
               players.push(player);
            } else {
             // existing player
              for (var i in players) {
                  if (players[i].sequence == player.sequence)
                     players[i] = player;}
            }
        },
        getPlayerBySequence: function(sequence) {
            // existing player
            for (var i in players) {
                if (players[i].sequence == sequence)
                return players[i];
            }
        },
        deletePlayerBySequence: function(sequence) {
            // existing player
            i = 0;
            players.forEach(function () {
                if (players[i].sequence == sequence) {
                players.splice(i, 1);
                i++;}
            });
        }
    }
};
