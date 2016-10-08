angular.module('myApp')
        .controller('CasinoController', CasinoController)
        .directive('myCasinoDirective', function() {
  return {
      restrict: 'AE',
      templateUrl: function(elem, attrs) {
          return attrs.templatePath;
      }
  };
})
;

CasinoController.$inject = ['$scope', 'playerService', 'toastr'];
function CasinoController($scope, playerService, toastr){

    var vm = this;
    // list of players to be rendered, init due to local testing without backend
    $scope.players = [{'id': null, 'avatar': 'ELF',
                    'alias':'Stranger', 'isHuman' : true, 
                    'aiLevel': 'LOW',  cubits: 2, 
                    securedLoan: 0}];  
    // load the Human created in on the home screen
    loadRemoteData();
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
        if ($scope.players[1] != null) {
           // add aliens
           for (i = 1, len = $scope.players.length; i < len; i++) {
                addPlayer($scope.players[i]);
           };
           // to do add game and casino relations
           toastr.warning('Work in progress', 'Warning') 
        };    
    };
    vm.changeAlien = function (index) {
        loopAiLevel(index);
        if ($scope.players[index].aiLevel == 'NONE') {
            $scope.players[index].cubits = 0;
            $scope.players[index].securedLoan = $scope.players[index].cubits;
        } else {
            $scope.players[index].cubits = (Math.ceil(Math.random() * 500)+ 500);
            $scope.players[index].securedLoan = $scope.players[index].cubits;
        };
        checkIfAliensAreSet();    
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function checkIfAliensAreSet() {
        if ($scope.players[0].cubits != 0 && $scope.players[1].cubits != 0 && $scope.players[1].aiLevel != 'NONE')  {
            vm.tothetable = true; 
        }
        if ($scope.players[1].aiLevel == 'NONE') {
            vm.showalien1 = false; 
        } else {
            vm.showalien1 = true; 
        }
        if ($scope.players[2].aiLevel == 'NONE') {
            vm.showalien2 = false;    
        } else {
            vm.showalien2 = true; 
        }
    };
    function loopAiLevel(index) {
        if ($scope.players[index].aiLevel == 'NONE') {
            if ($scope.players[1].aiLevel == 'NONE' && index == 2) {
                $scope.players[index].aiLevel = 'NONE';
                //vm.players[index].label = vm.none;
            } else {
                $scope.players[index].aiLevel = 'LOW';
                //vm.players[index].label = vm.dumb;
            };
        } else if ($scope.players[index].aiLevel == 'LOW') {
            $scope.players[index].aiLevel = 'MEDIUM';
            //vm.players[index].label = vm.average;
        } else if ($scope.players[index].aiLevel == 'MEDIUM') {
            $scope.players[index].aiLevel = 'HIGH';
            //vm.players[index].label = vm.smart;
        } else if ($scope.players[index].aiLevel == 'HIGH') {
            if ($scope.players[2].aiLevel != 'NONE' && index == 1) {
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
            .then( loadRemoteData )
        ;
    };  
    // I remove the given friend from the current collection.
    $scope.removePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.removePlayer( player.id )
            .then( loadRemoteData )
        ;
    };  
    // ---
    // PRIVATE METHODS USED IN PUBLIC API METHODS
    // ---
    // apply the remote data to the local scope.
    function applyRemoteData( newPlayers ) {
        $scope.players = newPlayers;
    }
    // load the remote data from the server.
    function loadRemoteData() {
        // The friendService returns a promise.
        playerService.getPlayers()
            .then(
                function( players ) {
                    applyRemoteData( players );
                 }
            );
    }
};