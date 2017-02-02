angular.module('myApp')
        .directive('myGameDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('GameController', ['$scope', 'playerService', 'gameService','toastr',
function ($scope, playerService, gameService, toastr){

    // viewmodel for this controller
    var vm = this;
    
    $scope.cardGame;
    $scope.players = [];
    $scope.visitor;

    // make sure there are only 2 aliens and 1 game
    initAliens(2);
    initGames(1);
    
    // flags + checks for ng-if
    vm.showListForDebug = false;
    vm.showalien1 = true;
    vm.showalien2 = true;
    vm.tothecasino = false;
    
    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    vm.start = function() {
           // to do add game and game relations
           toastr.warning('Work in progress', 'Warning');
    };
    vm.changeAlien = function (index) {

        initAliens(2);
        loopAiLevel(index);
        if ($scope.players[index].visitor.aiLevel === 'NONE') {
            $scope.players[index].visitor.cubits = 0;
            $scope.players[index].visitor.securedLoan = $scope.players[index].visitor.cubits;
        } else {
            $scope.players[index].visitor.cubits = (Math.ceil(Math.random() * 500)+ 500);
            $scope.players[index].visitor.securedLoan = $scope.players[index].visitor.cubits;
        };
        playerService.changeVisitorDetailsForGame( $scope.players[index] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---

    // flags for show/hide the buttons alien1, alien2 and tothecasino
    function checkIfAliensAreSet() {
        vm.tothecasino = true;
        vm.showalien1 = true;
        vm.showalien2 = true;
        for (i=0, len = $scope.players.length; i < len -1; i++) {
            if ($scope.players[i].visitor.aiLevel === 'NONE') {
                vm.tothecasino = false;
            };
            if (i === 1 && $scope.players[1].visitor.aiLevel === 'NONE') {
                vm.showalien1 = false;
            };
            if (i === 2 && $scope.players[2].visitor.aiLevel === 'NONE') {
                vm.showalien2 = false;
            };
        }
    };
    // proceed to the next aiLevel for the player at the index
    function loopAiLevel(index) {
        if ($scope.players[index].visitor.aiLevel === 'NONE') {
            if ($scope.players[1].visitor.aiLevel === 'NONE' && index === 2) {
                $scope.players[index].visitor.aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            } else {
                $scope.players[index].visitor.aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            };
        } else if ($scope.players[index].visitor.aiLevel === 'LOW') {
            $scope.players[index].visitor.aiLevel = 'MEDIUM';
            //vm.players[index].label = vm.average;
        } else if ($scope.players[index].visitor.aiLevel === 'MEDIUM') {
            $scope.players[index].visitor.aiLevel = 'HIGH';
            //vm.players[index].label = vm.smart;
        } else if ($scope.players[index].visitor.aiLevel === 'HIGH') {
            if ($scope.players[2].visitor.aiLevel !== 'NONE' && index === 1) {
                $scope.players[index].visitor.aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            } else {
                $scope.players[index].visitor.aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            };
        };
    };
    // ---
    // PUBLIC API METHODS
    // ---
    // process the add-player
    $scope.setupAiPlayerForGame = function(player) {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.setupAiPlayerForGame( player )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // update the given player from the current collection.
    $scope.changeVisitorDetailsForGame = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.changeVisitorDetailsForGame( player.playerId, player )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given friend from the current collection.
    $scope.deleteAiPlayerForGame = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.deleteAiPlayerForGame( player.playerId )
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
        // The playerService returns a promise.
        playerService.getPlayers()
            .then(
                function( players ) {
                    applyRemoteData( players );
                 }
            );
    }
    function initAliens( needed ) {
        // first get all human and alien players
        playerService.getPlayers()
        .then( function( response ) {
            $scope.players = response;
            // count the aliens  
            count = 0;
            for (i=0; i < $scope.players.length; i++) {
                 if ($scope.players[i].visitor.human === "false") {
                     count++; 
                }
            };
            toastr.info('There are ' + count + ' alien player(s) and ' + needed + ' is/are needed.', 'Info');

            if (needed < count ) {
                // more humans than needed -> delete all Aliens
                // TODO delete only the extra/specific aliens when less/one is/are needed
                 for (i=0; i < $scope.players.length; i++) {
                    if ($scope.players[i].visitor.human === "false") {
                        playerService.deleteAiPlayerForGame( $scope.players[i] )
                        .then( loadRemoteData, function( errorMessage ) {
                            toastr.error('Removing one alien failed: ' + errorMessage, 'Error');
                            }
                        );
                    }
                };
                for (i = 0 ; i < needed; i++) {
                    // add one or more aliens until needed
                    playerService.initGameForVisitor( "false" )
                           .then( loadRemoteData, function( errorMessage ) {
                               toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            } else if (needed > count) {
                // no aliens or too few? -> keep adding one until ok
                extra = needed - count;
                for (i = 0 ; i < extra; i++) {
                    // add one or more aliens 
                    playerService.initGameForVisitor( "false" )
                           .then( loadRemoteData, function( errorMessage ) {
                               toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            }
        }
        );
    }
    function applyRemoteGameData( newGames ) {
        $scope.games = newGames;
        // set or hide pictures and buttons
     }
    // load the remote data from the server.
    function loadRemoteGameData() {
        // The playerService returns a promise.
        gameService.getGameDetails()
            .then(
                function( games ) {
                    applyRemoteGameData( games );
                 }
            );
    }
    function initGames( needed ) {
        // first get all games
        gameService.getGameDetails()
        .then( function( response ) {
            $scope.games = response;
            // count the games  
            count = 0;
            count = $scope.games.length;
            
            toastr.info('There are ' + count + ' games and ' + needed + ' is/are needed.', 'Info');

            if (needed < count ) {
                // more games than needed -> delete all games
                // TODO delete only the extra/specific games when less/one is/are needed
                 for (i=0; i < $scope.games.length; i++) {

                    gameService.removeGame( $scope.games[i] )
                    .then( loadRemoteGameData, function( errorMessage ) {
                        toastr.error('Removing one game failed: ' + errorMessage, 'Error');
                        }
                    );

                };
                for (i = 0 ; i < needed; i++) {
                    // add one or more aliens until needed
                    gameService.initGameForExistingVisitor( "HIGHLOW" )
                           .then( loadRemoteGameData, function( errorMessage ) {
                               toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            } else if (needed > count) {
                // no aliens or too few? -> keep adding one until ok
                extra = needed - count;
                for (i = 0 ; i < extra; i++) {
                    // add one or more aliens 
                    gameService.initGameForExistingVisitor( "HIGHLOW" )
                           .then( loadRemoteGameData, function( errorMessage ) {
                               toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            }
        }
        );
    }
}]);