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
    $scope.players;
    $scope.ai;
    $scope.visitor;

    // make sure there are only 2 aliens
    initAliens(2);

    // flags + checks for ng-if
    vm.showListForDebug = true;
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
            .then( applyRemoteData, function( errorMessage ) {
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
    $scope.setupAiPlayerForGame = function( cardGame, ai) {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.setupAiPlayerForGame( cardGame, ai )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // update the given player from the current collection.
    $scope.changeVisitorDetailsForGame = function( cardGame, player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.changeVisitorDetailsForGame( cardGame, player )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given friend from the current collection.
    $scope.deleteAiPlayerForGame = function( cardGame, ai ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.deleteAiPlayerForGame( cardGame, ai )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  

    // ---
    // PRIVATE METHODS USED IN PUBLIC API METHODS OR TO INIT THE PAGE
    // ---

    // apply the remote data to the local scope.
    function applyRemoteData( responseCardGame ) {
        $scope.cardGame = responseCardGame;
        $scope.players = $scope.cardGame.players;

        // set or hide pictures and buttons
        checkIfAliensAreSet();
    }

    function initAliens( needed ) {

        // always get the cardgame from the service
        $scope.cardGame = playerService.getGameStoredInService();
        $scope.players = $scope.cardGame.players

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
                    .then( applyRemoteData, function( errorMessage ) {
                        toastr.error('Removing one alien failed: ' + errorMessage, 'Error');
                        }
                    );
                }
            };
            for (i = 0 ; i < needed; i++) {
                // add one or more aliens until needed
                playerService.initGameForVisitor( "false" )
                       .then( applyRemoteData, function( errorMessage ) {
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
                       .then( applyRemoteData, function( errorMessage ) {
                           toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                       }
                   );
            }
        }
    }


}]);