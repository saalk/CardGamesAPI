// Define all the controller on the `cardGamesApp` module

cardGamesApp.controller('HomeController', function ($scope, GameService, PlayerService, $filter) {

    $scope.players = PlayerService.listPlayers();
    $scope.humanplayer = PlayerService.getPlayer(0);
    $scope.bail = humanplayer.fiches;

    $scope.newplayer = {};

    // save is add or update based on id
    $scope.savePlayer = function(){
        PlayerService.savePlayer($scope.newplayer)
    };

    $scope.deletePlayer = function(id){
            PlayerService.deletePlayer(id);
            if ($scope.newplayer.id == id ) $scope.newplayer = {};
    };

    $scope.editPlayer = function(id){
            $scope.newplayer = angular.copy(PlayerService.getPlayer(id));
    };

    $scope.getBail = function() {
        newplayer = angular.copy(PlayerService.getPlayer(0));
        newplayer.fiches = (Math.ceil(Math.random() * 1000));
        PlayerService.savePlayer(newplayer);
        $scope.bail = newplayer.fiches;
    }
});

cardGamesApp.controller('CasinoController', function ($scope, $filter) {


    $scope.fiches1 = 0;
    $scope.fiches2 = 0;

    $scope.players = PlayerService.listPlayers();
    $scope.humanplayer = PlayerService.getPlayer(0);
    $scope.bail = humanplayer.fiches;

    $scope.newplayer = {};

    // save is add or update based on id
    $scope.savePlayer = function(){
        PlayerService.savePlayer($scope.newplayer)
    };

    $scope.deletePlayer = function(id){
            PlayerService.deletePlayer(id);
            if ($scope.newplayer.id == id ) $scope.newplayer = {};
    };

    $scope.editPlayer = function(id){
            $scope.newplayer = angular.copy(PlayerService.getPlayer(id));
    };

    $scope.getFiches1 = function(){
        newplayer = angular.copy(PlayerService.getPlayer(1));
        newplayer.fiches = (Math.ceil(Math.random() * 500)) + 500;
         PlayerService.savePlayer(newplayer);
        $scope.fiches1 = newplayer.fiches;
    }

    $scope.getFiches2 = function(){
        newplayer = angular.copy(PlayerService.getPlayer(2));
        newplayer.fiches = (Math.ceil(Math.random() * 500)) + 500;
        PlayerService.savePlayer(newplayer);
        $scope.fiches2 = newplayer.fiches;
    };
});