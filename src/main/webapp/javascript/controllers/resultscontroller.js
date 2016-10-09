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
        $scope.players = [];
    // list of players to be rendered, init due to local testing without backend
//    $scope.players = [[{'id': null, 'avatar': 'ELF',
//                    'alias':'No Backend', 'isHuman' : true, 
//                    'aiLevel': 'HUMAN',  cubits: 750, 
//                    securedLoan: 750},
//                    {'id': null, 'avatar': 'ELF',
//                    'alias':'Alien1', 'isHuman' : false, 
//                    'aiLevel': 'MEDIUM',  cubits: 750, 
//                    securedLoan: 750},
//                    {'id': null, 'avatar': 'ELF',
//                    'alias':'Alien2', 'isHuman' : false, 
//                    'aiLevel': 'MEDIUM',  cubits: 750, 
//                    securedLoan: 750}]];  
    // load the Players
    loadRemoteData();
    // flags + checks for ng-if
    vm.showListForDebug = true;
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.ante = 0; 
    vm.loopplayer = 0;
    vm.tothetable = false;
    checkIfAliensAreSet();
    setAnte();
    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    vm.start = function() {
       toastr.warning('Work in progress', 'Warning'); 
    };    
    vm.changeAlien = function (index) {
        loopAiLevel(index);
        checkIfAliensAreSet();  
        playerService.savePlayer($scope.players[index]);
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
            vm.tothetable = false;
        };
        playerService.savePlayer($scope.players[0]);
        checkIfAliensAreSet();
    }; 
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function setAnte() {
        if (vm.ante == 0) {
            vm.ante = 50;
        };
    };
    function checkIfAliensAreSet() {
        if ($scope.players[0].cubits !== 0 && $scope.players[1].cubits !== 0 && $scope.players[1].aiLevel !== 'NONE')  {
            vm.tothetable = true; 
        }
        if ($scope.players[1].aiLevel === 'NONE') {
            vm.showalien1 = false; 
        } else {
            vm.showalien1 = true; 
        }
        if ($scope.players[2].aiLevel === 'NONE') {
            vm.showalien2 = false;    
        } else {
            vm.showalien2 = true; 
        }
    };
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
            .then(
                loadRemoteData,
                function( errorMessage ) {
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
    // PRIVATE METHODS USED IN PUBLIC API METHODS AND INIT
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
            )
        ;
    }
}]);
