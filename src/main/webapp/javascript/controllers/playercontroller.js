angular.module('myApp')
        .directive('myPlayerDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('PlayerController', ['$scope', 'playerService', 'toastr',
function ($scope, playerService, toastr){

    // viewmodel for this controller
    var vm = this;
    
    // flags for ng-if and check if player details are ok
    vm.showListForDebug = false;
    vm.gotogame = false;
    
    // put a human player at index 0 in the collection, delete the rest
    $scope.players = [];
    initHumans(1);
  
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---

    vm.setHumanName = function () {
        //toastr.clear();
        $scope.players[0].alias = 'Script Doe';
        toastr.info('Your name is set', 'Information');
        playerService.updatePlayer( $scope.players[0] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('Setting name failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pawnHumanShipForCubits = function (extraCubits) {
        minimum = 0 + extraCubits;
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
            vm.gotogame = false;
        };
        playerService.updatePlayer( $scope.players[0] )
            .then( loadRemoteData, function( errorMessage ) {
                toastr.error('Setting pawn failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };
   
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function checkIfNameAndSecuredLoanAreSet() {
        if ($scope.players[0].securedLoan !== 0 && $scope.players[0].alias !== 'stranger') {
            vm.gotogame = true;
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
                    toastr.error('Adding a player failed: ' + errorMessage, 'Error');
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
                    toastr.error('Updating player failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given player supplied
    $scope.removePlayer = function( player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.removePlayer( player.playerId )
            .then( loadRemoteData, function( errorMessage ) {
                    toastr.error('Removing one player failed: ' + errorMessage, 'Error');
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
        // set show / hide flags for pictures and buttons
        checkIfNameAndSecuredLoanAreSet();
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
    function initHumans( needed ) {
        // first get all human and alien players        
        playerService.getPlayers()
        .then( function( response ) {
            $scope.players = response;
            // count the humans  
            count = 0;
            for (i=0; i < $scope.players.length; i++) {
                 if ($scope.players[i].human) {
                     count++; 
                }
            };
            toastr.info('There are ' + count + ' human player(s) and ' + needed + ' is/are needed.', 'Info');

            if (needed < count ) {
                // more humans than needed -> delete all Humans
                // TODO delete only the extra/specific humans when less/one is/are needed
                 for (i=0; i < $scope.players.length; i++) {
                    if ($scope.players[i].human) {
                        playerService.removePlayer( $scope.players[i] )
                        .then( loadRemoteData, function( errorMessage ) {
                            toastr.error('Removing one player failed: ' + errorMessage, 'Error');
                            }
                        );
                    }
                };
                for (i = 0 ; i < needed; i++) {
                    // add one or more humans until needed
                    playerService.initPlayerForIsHuman( "true" )
                           .then( loadRemoteData, function( errorMessage ) {
                               toastr.error('Initializing new player failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            } else if (needed > count) {
                // no humans or too few? -> keep adding one until ok
                extra = needed - count;
                for (i = 0 ; i < extra; i++) {
                    // add one or more humans 
                    playerService.initPlayerForIsHuman( "true" )
                           .then( loadRemoteData, function( errorMessage ) {
                               toastr.error('Initializing new player failed: ' + errorMessage, 'Error');
                           }
                       );
                }
            }
        }
        );
    }
}]);
