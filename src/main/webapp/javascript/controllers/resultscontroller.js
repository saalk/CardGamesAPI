angular.module('myApp')
        .directive('myResultsDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('ResultsController', ['$scope', 'playerService', 'toastr',
function ($scope, playerService, toastr){

    // viewmodel for this controller
    var vm = this;
    
    // init the players collection
    $scope.players = [];
    
    // flags + checks for ng-if
    vm.showListForDebug = false;
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.ante = 0; 
    vm.loopplayer = 0;
    vm.tothecasino = false;

    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";
    
    // load players
    setAnte();
    
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    vm.start = function() {
       toastr.warning('Work in progress', 'Warning'); 
    };    
    vm.changeAlien = function (index) {
        loopAiLevel(index);
        if ($scope.players[index].securedLoan === 0) {
             $scope.players[index].cubits = (Math.ceil(Math.random() * 500)+ 500);
            $scope.players[index].securedLoan = $scope.players[index].cubits;
        };
        playerService.updatePlayer( $scope.players[index] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pawnHumanShipForCubits = function () {
        if ($scope.players[0].securedLoan === 0 ) {
            $scope.players[0].securedLoan = (Math.ceil(Math.random() * 750)+250);
            $scope.players[0].cubits = $scope.players[0].cubits + $scope.players[0].securedLoan;
            toastr.info('Your ship is pawned', 'Information');
        } else if ($scope.players[0].cubits < $scope.players[0].securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if ($scope.players[0].cubits >= $scope.players[0].securedLoan) {
            $scope.players[0].cubits = $scope.players[0].cubits - $scope.players[0].securedLoan;
            $scope.players[0].securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.tothecasino = false;
        };
        playerService.updatePlayer( $scope.players[0] );
    }; 
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function setAnte() {
        if (vm.ante === 0) {
            vm.ante = 50;
        };
        loadRemoteData();
    };
    // flags for show/hide the buttons alien1, alien2 and tothecasino
    // TODO a copy of the gamecontroller
    function checkIfAliensAreSet() {
        vm.tothecasino = true;
        vm.showalien1 = true;
        vm.showalien2 = true;
        for (i=0, len = $scope.players.length; i < len -1; i++) {
            if ($scope.players[i].aiLevel === 'NONE') {
                vm.tothecasino = false;
            };
            if (i === 1 && $scope.players[1].aiLevel === 'NONE') {
                vm.showalien1 = false;
            };
            if (i === 2 && $scope.players[2].aiLevel === 'NONE') {
                vm.showalien2 = false;
            };
        }
    };
    // proceed to the next aiLevel for the player at the index
    // TODO a copy of the gamecontroller
    function loopAiLevel(index) {
        if ($scope.players[index].aiLevel === 'NONE') {
            if ($scope.players[1].aiLevel === 'NONE' && index === 2) {
                $scope.players[index].aiLevel = 'NONE';
                $scope.players[index].label = vm.none;
            } else {
                $scope.players[index].aiLevel = 'LOW';
                $scope.players[index].label = vm.dumb;
            };
        } else if ($scope.players[index].aiLevel === 'LOW') {
            $scope.players[index].aiLevel = 'MEDIUM';
            $scope.players[index].label = vm.average;
        } else if ($scope.players[index].aiLevel === 'MEDIUM') {
            $scope.players[index].aiLevel = 'HIGH';
            $scope.players[index].label = vm.smart;
        } else if ($scope.players[index].aiLevel === 'HIGH') {
            if ($scope.players[2].aiLevel !== 'NONE' && index === 1) {
                $scope.players[index].aiLevel = 'LOW';
                $scope.players[index].label = vm.dumb;
            } else {
                $scope.players[index].aiLevel = 'NONE';
                $scope.players[index].label = vm.none;
            };
        };
    };
    // ---
    // PUBLIC API METHODS
    // ---
    // process the add-player
    $scope.addPlayer = function(player) {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.addPlayer( player )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // update the given player from the current collection.
    $scope.updatePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.updatePlayer( player.id, player )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given friend from the current collection.
    $scope.removePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.removePlayer( player.id )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // ---
    // PRIVATE METHODS USED IN PUBLIC API METHODS OR INIT
    // ---
    // apply the remote data to the local scope.
    function applyRemoteData( newPlayers ) {
        $scope.players = newPlayers;
        // set or hide pictures and buttons
        checkIfAliensAreSet();
    }
    // load the remote data from the server.
    function loadRemoteData() {
        // The friendService returns a promise.
        playerService.getPlayers()
            .then(
                function( players ) {
                    applyRemoteData( players );
                 }
            )
        ;
    }
}]);
