angular.module('myApp')
        .directive('myTableDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('TableController', ['$scope', 'playerService', 'toastr',
function ($scope, playerService, toastr){

    var vm = this;
    $scope.players = [];
    // load the Players
    loadRemoteData();
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.higher = true;
    vm.lower = true;
    vm.pass = true;
    vm.generateguess = 0; 
    vm.loopplayer = 0;
    vm.ante = 0; 
    vm.round = 1; 
    vm.showalien1 = false;
    vm.showalien2 = false;
    initTable();
    toastr.info('There are ' + $scope.players.length + ' player(s).', 'Info');

    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
     vm.doguess = function() { 
        vm.generateguess = (Math.random() >= 0.5); // (Math.ceil(Math.random() * 1)); 
        if (vm.generateguess === 0) {
            toastr.success('A good guess', 'Success');
            $scope.players[vm.loopplayer].cubits = $scope.players[vm.loopplayer].cubits + vm.ante;
        } else {
            toastr.error('Next card differs from your guess', 'Bad luck');
            $scope.players[vm.loopplayer].cubits = $scope.players[vm.loopplayer].cubits - vm.ante;
        }; 
        $scope.updatePlayer($scope.players[vm.loopplayer]);
    };
    vm.pass = function() { 
        if (vm.loopplayer < $scope.players.length -1 ) {
            if ($scope.players[vm.loopplayer + 1].aiLevel === 'NONE') {
                vm.loopplayer = 0; 
                vm.round = vm.round + 1;
            } else {
                vm.loopplayer = vm.loopplayer + 1; 
            };    
        } else {
            vm.loopplayer = 0; 
            vm.round = vm.round + 1;
        };
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function initTable() {
        if (vm.ante === 0) {
            vm.ante = 50;
        };
        for (var i in $scope.players) {
            if ($scope.players[i].securedLoan === 0 && $scope.players[i].aiLevel !== 'NONE') {
                $scope.players[i].securedLoan = $scope.players[i].cubits;
            };
        };       
    };
    function checkIfAliensAreSet() {
        if ($scope.players[1].aiLevel === 'NONE') {
            vm.showalien1 = false; 
        } else {
            vm.showalien1 = true; 
        };
        if ($scope.players[2].aiLevel === 'NONE') {
            vm.showalien2 = false;    
        } else {
            vm.showalien2 = true; 
        }
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
            .then(
                loadRemoteData,
                function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
        // Reset the form once values have been consumed.
        $scope.form.name = "";
    };
    // I update the given player from the current collection.
    $scope.updatePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.updatePlayer( player.id, player )
            .then(
                loadRemoteData,
                function( errorMessage ) {
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
            .then(
                loadRemoteData,
                function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // ---
    // PRIVATE METHODS USED IN PUBLIC API METHODS AND INIT
    // ---
    // apply the remote data to the local scope.
    function applyRemoteData( newPlayers ) {
        $scope.players = newPlayers;
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
