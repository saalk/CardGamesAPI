angular.module('myApp')
        .directive('myCasinoDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('CasinoController', ['$scope', 'playerService', 'toastr',
function ($scope, playerService, toastr){

    // viewmodel for this controller
    var vm = this;
    // init the players collection
    $scope.players = [];
    // make sure there is only 1 alien
    initAliens(2);
    
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.tothetable = false;
    checkIfAliensAreSet();
    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    vm.start = function() {
           // to do add game and casino relations
           toastr.warning('Work in progress', 'Warning') 
    };
    vm.changeAlien = function (index) {

        initAliens(2);
        loopAiLevel(index);
        if ($scope.players[index].aiLevel === 'NONE') {
            $scope.players[index].cubits = 0;
            $scope.players[index].securedLoan = $scope.players[index].cubits;
        } else {
            $scope.players[index].cubits = (Math.ceil(Math.random() * 500)+ 500);
            $scope.players[index].securedLoan = $scope.players[index].cubits;
        };
        checkIfAliensAreSet();
        playerService.updatePlayer( $scope.players[index] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---

    // flags for show/hide the buttons alien1, alien2 and tothetable
    function checkIfAliensAreSet() {
        vm.tothetable = true;
        vm.showalien1 = true;
        vm.showalien2 = true;
        for (i=0, len = $scope.players.length; i < len -1; i++) {
            if ($scope.players[i].aiLevel === 'NONE') {
                vm.tothetable = false;
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
    function loopAiLevel(index) {
        if ($scope.players[index].aiLevel === 'NONE') {
            if ($scope.players[1].aiLevel === 'NONE' && index === 2) {
                $scope.players[index].aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            } else {
                $scope.players[index].aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            };
        } else if ($scope.players[index].aiLevel === 'LOW') {
            $scope.players[index].aiLevel = 'MEDIUM';
            //vm.players[index].label = vm.average;
        } else if ($scope.players[index].aiLevel === 'MEDIUM') {
            $scope.players[index].aiLevel = 'HIGH';
            //vm.players[index].label = vm.smart;
        } else if ($scope.players[index].aiLevel === 'HIGH') {
            if ($scope.players[2].aiLevel !== 'NONE' && index === 1) {
                $scope.players[index].aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            } else {
                $scope.players[index].aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
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
    // I update the given player from the current collection.
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
    // I remove the given friend from the current collection.
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
    }
    // load the remote data from the server.
    function loadRemoteData() {
        // The playerService returns a promise.
        playerService.getPlayers()
            .then(
                function( players ) {
                    applyRemoteData( players );
                 }
            );
    }
    function initAliens( needed ) {
        playerService.getPlayers()
        .then( function( response ) {
            $scope.players = response;
            // count the aliens  
            count = 0;
            for (var i=0; i < $scope.players.length; i++) {
                 if ($scope.players[i].isHuman === false) {
                     count++; 
                }
            };
            toastr.info('There are ' + count + ' alien player(s) and ' + needed + ' is/are needed.', 'Info');

            if (needed < count ) {
                // more aliens than needed delete them all
                // TODO delete only the extra aliens or add needed ones
                playerService.removePlayersById( $scope.players )
                .then( loadRemoteData, function( errorMessage ) { 
                        toastr.error('Removing all aliens failed: ' + errorMessage, 'Error');
                    }
                );
            } else if (needed > count) {
                // no aliens or too few
                extra = needed - count;
                for (i = 0 ; i < extra; i++) {
                    // add one or more aliens 
                    playerService.initPlayerForIsHuman( false )
                           .then( loadRemoteData, function( errorMessage ) {
                               toastr.error('Initializing alien failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            }
        }
        );
    }
}]);