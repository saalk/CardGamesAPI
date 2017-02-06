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

    /* jshint validthis: true */
    var vm = this;
    vm.cardGame = {};
    vm.players = [];
    vm.defaultAi = {alias: "Ai JS Doe", human: "false", aiLevel: "NONE", cubits: 0, securedLoan: 0};
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.showalien1 = true;
    vm.showalien2 = true;
    vm.tothecasino = false;
    // make sure there are only 2 aliens
    initAliens(2);
    // fixed text
    vm.smart = 'Most evolved alien species, this fellow starts with ';
    vm.average = 'A nice competitor, he has a budget of ';
    vm.dumb = 'Quick to beat, starting with ';
    vm.none = 'This alien has left the game with ';

    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    vm.changeAlien = function (index) {

        initAliens(2);
        loopAiLevel(index);
        if (vm.players[index].visitor.aiLevel === 'NONE') {
            vm.players[index].visitor.securedLoan = 0;
        } else {
            vm.players[index].visitor.securedLoan = (Math.ceil(Math.random() * 500)+ 500);
        };
        playerService.changeVisitorDetailsForGame( vm.players[index] )
            .then( applyRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // ---
    // PUBLIC API METHODS
    // ---
    // process the add-player
    vm.setupAiPlayerForGame = function( functionCardGame, ai) {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.setupAiPlayerForGame( functionCardGame, ai )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // update the given player from the current collection.
    vm.changeVisitorDetailsForGame = function( functionCardGame, player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.changeVisitorDetailsForGame( functionCardGame, player )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given friend from the current collection.
    vm.deleteAiPlayerForGame = function( functionCardGame, ai ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.deleteAiPlayerForGame( functionCardGame, ai )
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  

    // ---
    // PRIVATE METHODS USED IN PUBLIC METHODS OR TO INIT THE PAGE
    // ---

    // proceed to the next aiLevel for the player at the index
    function loopAiLevel(index) {
        if (vm.players[index].visitor.aiLevel === 'NONE') {
            if (vm.players[1].visitor.aiLevel === 'NONE' && index === 2) {
                vm.players[index].visitor.aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            } else {
                vm.players[index].visitor.aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            };
        } else if (vm.players[index].visitor.aiLevel === 'LOW') {
            vm.players[index].visitor.aiLevel = 'MEDIUM';
            //vm.players[index].label = vm.average;
        } else if (vm.players[index].visitor.aiLevel === 'MEDIUM') {
            vm.players[index].visitor.aiLevel = 'HIGH';
            //vm.players[index].label = vm.smart;
        } else if (vm.players[index].visitor.aiLevel === 'HIGH') {
            if (vm.players[2].visitor.aiLevel !== 'NONE' && index === 1) {
                vm.players[index].visitor.aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            } else {
                vm.players[index].visitor.aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            };
        };
    };

    function initAliens( needed ) {
        // always get the cardgame from the service
        vm.cardGame = playerService.getGameStoredInService();
        vm.players = vm.cardGame.players;

        // count the aliens
        count = 0;
        for (i=0; i < vm.players.length; i++) {
             if (vm.players[i].visitor.human === 'false') {
                 count++;
            }
        };
        toastr.info('There are ' + count + ' alien player(s) and ' + needed + ' is/are needed.', 'Info');

        if (needed < count ) {
            // more humans than needed -> delete all Aliens
            // TODO delete only the extra/specific aliens when less/one is/are needed
             for (i=0; i < vm.players.length; i++) {
                if (vm.players[i].visitor.human === 'false') {
                    playerService.deleteAiPlayerForGame( vm.cardGame, vm.players[i] )
                    .then( applyRemoteData, function( errorMessage ) {
                        toastr.error('Removing one alien failed: ' + errorMessage, 'Error');
                        }
                    );
                }
            };
            for (i = 0 ; i < needed; i++) {
                // add one or more aliens until needed
                playerService.setupAiPlayerForGame( vm.cardGame, vm.defaultAi )
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
                playerService.setupAiPlayerForGame( vm.cardGame, vm.defaultAi )
                       .then( applyRemoteData, function( errorMessage ) {
                           toastr.error('Initializing new alien failed: ' + errorMessage, 'Error');
                       }
                   );
            }
        }
    };
    // apply the remote data to the local scope.
    function applyRemoteData( responseCardGame ) {

        vm.cardGame = responseCardGame;
        vm.players = responseCardGame.players;

        vm.tothecasino = true;
        vm.showalien1 = true;
        vm.showalien2 = true;
        for (i=0, len = vm.players.length; i < len -1; i++) {
            if (vm.players[i].visitor.aiLevel === 'NONE') {
                vm.tothecasino = false;
            };
            if (i === 1 && vm.players[1].visitor.aiLevel === 'NONE') {
                vm.showalien1 = false;
            };
            if (i === 2 && vm.players[2].visitor.aiLevel === 'NONE') {
                vm.showalien2 = false;
            };
        };
    };
}]);