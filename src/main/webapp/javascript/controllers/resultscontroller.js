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
    vm.cardGame;
    vm.players;
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.showalien1 = true;
    vm.showalien2 = true;
    vm.cardGame.ante = 0;
    vm.loopplayer = 0;
    vm.tothecasino = false;

    // fixed text
    vm.smart = 'Most evolved alien species, this fellow starts with ';
    vm.average = 'A nice competitor, he has a budget of ';
    vm.dumb = 'Quick to beat, starting with ';
    vm.none = 'This alien has left the game with ';
    
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
        if (vm.players[index].visitor.securedLoan === 0) {
             vm.players[index].visitor.cubits = (Math.ceil(Math.random() * 500)+ 500);
            vm.players[index].visitor.securedLoan = vm.players[index].visitor.cubits;
        };
        playerService.changeVisitorDetailsForGame( vm.players[index] )
            .then( applyRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pawnHumanShipForCubits = function () {
        if (vm.activePlayer.securedLoan === 0 ) {
            vm.activePlayer.securedLoan = (Math.ceil(Math.random() * 750)+250);
            vm.activePlayer.cubits = vm.activePlayer.cubits + vm.activePlayer.securedLoan;
            toastr.info('Your ship is pawned', 'Information');
        } else if (vm.activePlayer.cubits < vm.activePlayer.securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if (vm.activePlayer.cubits >= vm.activePlayer.securedLoan) {
            vm.activePlayer.cubits = vm.activePlayer.cubits - vm.activePlayer.securedLoan;
            vm.activePlayer.securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.tothecasino = false;
        };
        playerService.changeVisitorDetailsForGame( vm.players[0] );
    }; 
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function setAnte() {
        vm.cardGame = playerService.getGameStoredInService();
        vm.players = vm.cardGame.players;

        if (vm.cardGame.ante === 0) {
            vm.cardGame.ante = 50;
        };
    };

    // proceed to the next aiLevel for the player at the index
    // TODO a copy of the gamecontroller
    function loopAiLevel(index) {
        if (vm.players[index].visitor.aiLevel.toUpperCase() == 'NONE') {
            if (vm.players[1].visitor.aiLevel.toUpperCase() == 'NONE' && index === 2) {
                vm.players[index].visitor.aiLevel = 'None';
                vm.players[index].label = vm.none;
            } else {
                vm.players[index].visitor.aiLevel = 'Dumb';
                vm.players[index].label = vm.dumb;
            };
        } else if (vm.players[index].visitor.aiLevel.toUpperCase() == 'DUMB') {
            vm.players[index].visitor.aiLevel = 'Medium';
            vm.players[index].label = vm.average;
        } else if (vm.players[index].visitor.aiLevel.toUpperCase() == 'MEDIUM') {
            vm.players[index].visitor.aiLevel = 'Smart';
            vm.players[index].label = vm.smart;
        } else if (vm.players[index].visitor.aiLevel.toUpperCase() == 'SMART') {
            if (vm.players[2].visitor.aiLevel.toUpperCase() !== 'NONE' && index === 1) {
                vm.players[index].visitor.aiLevel = 'Dumb';
                vm.players[index].label = vm.dumb;
            } else {
                vm.players[index].visitor.aiLevel = 'None';
                vm.players[index].label = vm.none;
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
            .then( applyRemoteData, function( errorMessage ) {
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
            .then( applyRemoteData, function( errorMessage ) {
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
            .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // apply the remote data to the local scope.
    function applyRemoteData( responseCardGame ) {

        vm.cardGame = responseCardGame;
        vm.players = responseCardGame.players;
        vm.tothecasino = true;
        vm.showalien1 = true;
        vm.showalien2 = true;
        for (i=0, len = vm.players.length; i < len -1; i++) {
            if (vm.players[i].visitor.aiLevel.toUpperCase() == 'NONE') {
                vm.tothecasino = false;
            };
            if (i === 1 && vm.players[1].visitor.aiLevel.toUpperCase() == 'NONE') {
                vm.showalien1 = false;
            };
            if (i === 2 && vm.players[2].visitor.aiLevel.toUpperCase() == 'NONE') {
                vm.showalien2 = false;
            };
        }
    };
}]);
