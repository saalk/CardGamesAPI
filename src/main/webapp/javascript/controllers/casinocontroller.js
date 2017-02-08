angular.module('myApp')
        .directive('myCasinoDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('CasinoController', ['$scope', 'playerService', 'gameService','toastr',
function ($scope, playerService, gameService, toastr){

    // viewmodel for this controller
    var vm = this;
    vm.cardGame;
    vm.players;
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.higher = true;
    vm.lower = true;
    //vm.pass = true;
    vm.generateguess = 0; 
    vm.loopplayer = 0;
    vm.showalien1 = true;
    vm.showalien2 = true;

    initCasino();
 
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
     vm.doguess = function() { 
        vm.generateguess = 0;
        vm.generateguess = Math.round(Math.random() + 0.1 ); 
        if (vm.generateguess === 1) {
            toastr.success('A good guess', 'Success');
            vm.players[vm.loopplayer].visitor.cubits = vm.players[vm.loopplayer].visitor.cubits + vm.cardGame.ante;
        } else {
            toastr.error('Next card differs from your guess', 'Bad luck');
            vm.players[vm.loopplayer].visitor.cubits = vm.players[vm.loopplayer].visitor.cubits - vm.cardGame.ante;
        }; 
        playerService.changeVisitorDetailsForGame( vm.cardGame, vm.players[vm.loopplayer] )
            .then( applyRemoteData, function( errorMessage ) {
                toastr.error('Setting cubits failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pass = function() { 
        if (vm.loopplayer < vm.players.length -1 ) {
            if (vm.players[vm.loopplayer + 1].visitor.aiLevel.toUpperCase() == 'NONE') {
                vm.loopplayer = 0; 
                vm.cardGame.currentRound = vm.cardGame.currentRound + 1;
            } else {
                vm.loopplayer = vm.loopplayer + 1; 
            };    
        } else {
            vm.loopplayer = 0; 
            vm.cardGame.currentRound = vm.cardGame.currentRound + 1;
        };
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function initCasino() {
        vm.cardGame = playerService.getGameStoredInService();
        vm.players = vm.cardGame.players;

        if (vm.cardGame.ante === 0) {
            vm.cardGame.ante = 50;
        };
    };

    // ---
    // PUBLIC API METHODS
    // ---
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

    // ---
    // PRIVATE METHODS USED IN PUBLIC API METHODS AND INIT
    // ---
    // apply the remote data to the local scope.
    function applyRemoteData( responseCardGame ) {

        vm.cardGame = responseCardGame;
        vm.players = responseCardGame.players;
        if (vm.players[1].visitor.aiLevel.toUpperCase() == 'NONE') {
            vm.showalien1 = false;
        } else {
            vm.showalien1 = true;
        };
        if (vm.players[2].visitor.aiLevel.toUpperCase() == 'NONE') {
            vm.showalien2 = false;
        } else {
            vm.showalien2 = true;
        }
    }

}]);
