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
    // list of players to be rendered, init due to local testing without backend
    $scope.players = [{'id': null, 'avatar': 'ELF',
                    'alias':'Stranger', 'isHuman' : true, 
                    'aiLevel': 'NONE',  cubits: 1, 
                    securedLoan: 0}];  
    // make sure there is only on: a Human
    loadRemoteData();
    initHumanOnRemoteData();
    checkIfNameAndSecuredLoanAreSet();
    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---
    //vm.minimum = 0;
    vm.setHumanName = function () {
        //toastr.clear();
        $scope.players[0].alias = 'Script Doe';
        toastr.info('Your name is set', 'Information');
        checkIfNameAndSecuredLoanAreSet();
    };
    vm.pawnHumanShipForCubits = function (minimum) {
        minimum;
        if ($scope.players[0].securedLoan == 0 ) {
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
    };
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS
    // ---
    function initHumanOnRemoteData() {
        // if more then one remote player found -> delete the rest
        // TODO: read remote player from cookie
        if ($scope.players[1] != null) {
             for (i = 1, len = $scope.players.length; i < len; i++) {
                playerService.deletePlayer($scope.players[i]);
             };
        };
        // no remote player found -> spawn a human
        if ($scope.players[0].id == null) {
                playerService.addPlayer({'avatar': 'ELF', 
                    'alias':'stranger', 'isHuman' : true, 
                    'aiLevel': 'HUMAN',  cubits: 0, 
                    securedLoan: 0})
        };
        toastr.success('A new spaceship has landed...', 'Alpha landing bay');
        loadRemoteData();
    };
    function checkIfNameAndSecuredLoanAreSet() {
        if ($scope.players[0].securedLoan != 0 && $scope.players[0].alias != 'stranger') {
            vm.gotocasino = true;
        };
        if ($scope.players[0].alias === 'stranger' && $scope.players[0].cubits != 0) {
            setTimeout(function() {
            toastr.warning('Get your name for the casino, stranger', 'Warning')},1000);
        } else if ($scope.players[0].cubits == 0 && $scope.players[0].alias != 'stranger') {
            setTimeout(function() {
            toastr.warning('Pawn your ship for the casino', 'Warning')},1000);
        };
    };
    // ---
    // PUBLIC API METHODS
    // ---
    vm.gotothecasino = function() {
       savePlayer($scope.players[0]);
    };
    // process the add-player
    $scope.addPlayer = function() {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.addPlayer( $scope.players[0] )
            .then( loadRemoteData, function( errorMessage ) {
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
            )
        ;
    }
}]);
