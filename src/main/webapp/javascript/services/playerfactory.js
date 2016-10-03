angular.module('myApp')
        .service('PlayerService', PlayerService); 
PlayerService.$inject = ['toastr', 'Restangular'];
function PlayerService(toastr, Restangular){

   //var players = [];
   var players = Restangular.all('players').getList();
   return {
        initOrListPlayers: function() {
            //Restangular.all('players').getList().then(function(result) {
            //    players = result;
            //});
            if (players[0] == null) {
            players = [
                {id: 0,  'playerId': null, 'avatar': 'ELF', 'alias':
                'stranger', 'isHuman' : true, 'aiLevel': 'HUMAN', cubits: 0000, securedLoan: 0000}];
                toastr.success('A new spaceship has landed...', 'Alpha landing bay');
            };
            return players;
            //  players = Restangular.all('players');
            //  $scope.Players = Restangular.all('players').getList().$object;

        },
        pushAverageAiPlayerToList: function() {
            players.push({id: 0,  'playerId': '', 'avatar': 'ELF', 'alias':
                'alien', 'aiLevel': 'Average', cubits: 0000, securedLoan: 0000});
         },
        popLastPlayerFromList: function() {
            players.pop(players.length-1);
        },
        // Restangular services
        listPlayers: function() {
            Restangular.all('players').getList().then(function(players) {
            return players;
          });
        },
        getPlayer: function(player) {
            Restangular.get(player);
        },
        savePlayer: function(player) {
            Restangular.one('players').customPOST(player);
        },
        updatePlayer: function(player) {
            player.put; 
        }
    };
};
