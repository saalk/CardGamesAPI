angular.module('myApp')
        .directive('myHomeDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('HomeController', ['$scope', 'playerService', 'toastr',
function ($scope, playerService, toastr){

    // viewmodel for this controller
    var vm = this;
    // flags for ng-if and check if player details are ok
    vm.showListForDebug = true;
    vm.gotocasino = false;
    // init the players collection
    $scope.players = [];
    // put a human player at index 0 in the collection
    initHome();
    // init due to local testing without backend
//    if ($scope.players.length === 0) { 
//        $scope.players = {'id': null, 'avatar': 'ELF',
//                    'alias':'No backend', 'isHuman' : true, 
//                    'aiLevel': 'HUMAN',  cubits: 0, 
//                    securedLoan: 0};
//    };
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    //vm.minimum = 0;
    vm.setHumanName = function () {
        //toastr.clear();
        $scope.players[0].alias = 'Script Doe';
        toastr.info('Your name is set', 'Information');
        checkIfNameAndSecuredLoanAreSet();
        playerService.updatePlayer( $scope.players[0] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pawnHumanShipForCubits = function (minimum) {
        minimum;
        if ($scope.players[0].securedLoan === 0 ) {
            $scope.players[0].securedLoan = (Math.ceil(Math.random() * (1000 - minimum))+ minimum);
            $scope.players[0].cubits = $scope.players[0].cubits + $scope.players[0].securedLoan;
            // toastr.info('<body>Your ship is pawned for at least {{ vm.minimum }}<body>', 'InformationL',{allowHtml: true});
            toastr.info('Your ship is pawned', 'Information');
        } else if ($scope.players[0].cubits < $scope.players[0].securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if ($scope.players[0].cubits >= $scope.players[0].securedLoan) {
            $scope.players[0].cubits = $scope.players[0].cubits - $scope.players[0].securedLoan;
            $scope.players[0].securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.gotocasino = false;
        };
        checkIfNameAndSecuredLoanAreSet();
        playerService.updatePlayer( $scope.players[0] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function checkIfNameAndSecuredLoanAreSet() {
        if ($scope.players[0].securedLoan !== 0 && $scope.players[0].alias !== 'stranger') {
            vm.gotocasino = true;
        };
        if ($scope.players[0].alias === 'stranger' && $scope.players[0].cubits !== 0) {
            setTimeout(function() {
            toastr.warning('Get your name for the casino, stranger', 'Warning');},1000);
        } else if ($scope.players[0].cubits === 0 && $scope.players[0].alias !== 'stranger') {
            setTimeout(function() {
            toastr.warning('Pawn your ship for the casino', 'Warning');},1000);
        };
    };
    // ---
    // PUBLIC API METHODS
    // ---
    // add the player
    $scope.addPlayer = function( player ) {
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
    // update the given player supplied
    $scope.updatePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.updatePlayer( player )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given player supplied
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
    // PRIVATE METHODS USED IN PUBLIC API METHODS OR INIT SCREEN
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
                function( response ) {
                    applyRemoteData( response );
                 }
            )
        ;
    }
    function initHome() {
        loadRemoteData(); // do a get all
        // TODO: read remote game from cookie > load related player
        // For now if remote players found -> delete them all
        if ($scope.players[1].id !== null) {
            for (i = 0, len = $scope.players.length; i < len; i++) {
                playerService.removePlayer( $scope.players[i].id )
                    .then( loadRemoteData, function( errorMessage ) {
                        toastr.error('An error has occurred:' + errorMessage, 'Error');
                        }
                    )
                ;
            }
        };
        // Add a default human player
        playerService.addPlayer( {'id':null, 'playerId': '', 'avatar': 'ELF', 
                'alias':'stranger', 'isHuman' : true, 'aiLevel': 'HUMAN',  
                cubits: 0, securedLoan: 0} )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('An error has occurred:' + errorMessage, 'Error');
                }
            )
        ;
        toastr.success('A new spaceship has landed...', 'Alpha landing bay');

    };
}]);
