// Set all the controller on the `cardGamesApp` module
//
// The controller's responsibility is for the presentation and gathering of information for the view.
// It should not care how it gets the data, just that it knows who to ask for it.

angular
    .module('myApp')
    .controller('HomeController2', function ($scope, PlayerService, $location) {

    $scope.players = PlayerService.listPlayers();
    $scope.humanplayer = PlayerService.getPlayer(0);
    $scope.bail = $scope.humanplayer.fiches;
    $scope.newplayer = $scope.humanplayer;

    if ($location.path() === '') {
        $location.path('/');
    }

    $scope.location = $location;

    // save is add or update based on id
    $scope.savePlayer = function () {
        PlayerService.savePlayer($scope.newplayer)
    };

    $scope.deletePlayer = function (id) {
        PlayerService.deletePlayer(id);
        if ($scope.newplayer.id == id) {
        $scope.newplayer = {};
    }
    };

    $scope.editPlayer = function (id) {
        $scope.newplayer = angular.copy(PlayerService.getPlayer(id));
    };

    $scope.getBail = function () {
        $scope.newplayer = angular.copy(PlayerService.getPlayer(0));
        $scope.newplayer.fiches = (Math.ceil(Math.random() * 1000));
        // (Math.ceil(Math.random() * 500)) + 500;
        PlayerService.savePlayer($scope.newplayer);
        $scope.bail = $scope.newplayer.fiches;
    }
})