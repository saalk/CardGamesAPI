angular.module('myApp')
        .directive('myPlayerDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        })
        .controller('PlayerController', ['$scope', 'playerService', 'gameService', 'toastr',
function ($scope, playerService, gameService, toastr){

    // viewmodel for this controller
    var vm = this;
    
    // flags for ng-if and check if player details are ok
    vm.showListForDebug = false;
    vm.gotogame = false;
    
    $scope.cardGame = {};
    $scope.players = [];
    $scope.visitor = {};
    
    $scope.ai;

    // init a game and player
    init();

    // ---
    // PUBLIC VIEW BEHAVIOUR METHODS 
    // ---

    vm.setHumanName = function () {
        //toastr.clear();
        $scope.visitor.alias = 'Script Doe';
        toastr.info('Your name is set', 'Information');
        playerService.changeVisitorDetailsForGame( $scope.players[0] )
        // TODO applyRemoteData or loadRemoteData ?
               .then( applyRemoteData, function( errorMessage ) {
                toastr.error('Setting name failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };
    vm.pawnHumanShipForCubits = function (extraCubits) {
        minimum = 0 + extraCubits;
        if ($scope.visitor.securedLoan === 0 ) {
            $scope.visitor.securedLoan = (Math.ceil(Math.random() * (1000 - minimum))+ minimum);
            $scope.visitor.cubits = $scope.visitor.cubits + $scope.visitor.securedLoan;
            // toastr.info('<body>Your ship is pawned for at least {{ vm.minimum }}<body>', 'InformationL',{allowHtml: true});
            toastr.info('Your ship is pawned', 'Information');
        } else if ($scope.visitor.cubits < $scope.visitor.securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if ($scope.visitor.cubits >= $scope.visitor.securedLoan) {
            $scope.visitor.cubits = $scope.visitor.cubits - $scope.visitor.securedLoan;
            $scope.visitor.securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.gotogame = false;
        };
        playerService.changeVisitorDetailsForGame( $scope.players[0] )
        // TODO applyRemoteData or loadRemoteData ?
               .then( applyRemoteData, function( errorMessage ) {
                toastr.error('Setting pawn failed: ' + errorMessage, 'Error');
                }
            );
    };
   
    // ---
    // PRIVATE METHODS USED IN PUBLIC BEHAVIOUR METHODS + INIT
    // ---

    function init() {

        // add a new game and visitor and relate the visitor to the game
        playerService.initGameForVisitor( "true" )
               .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('Initializing new player failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };

    function checkIfNameAndSecuredLoanAreSet() {
        if ($scope.visitor.securedLoan !== 0 && $scope.visitor.alias !== 'stranger') {
            vm.gotogame = true;
        };
        if ($scope.visitor.alias === 'stranger' && $scope.visitor.cubits !== 0) {
            setTimeout(function() {
            toastr.warning('Get your name for the casino, stranger', 'Warning');},1000);
        } else if ($scope.visitor.cubits === 0 && $scope.visitor.alias !== 'stranger') {
            setTimeout(function() {
            toastr.warning('Pawn your ship for the casino', 'Warning');},1000);
        };
    };
    // ---
    // PUBLIC API METHODS
    // ---

    // add the player
    $scope.setupAiPlayerForGame = function( cardGame, ai ) {
        // If the data we provide is invalid, the promise will be rejected,
        // at which point we can tell the user that something went wrong. In
        // this case, toastr is used
        playerService.setupAiPlayerForGame( cardGame, ai )
        // TODO applyRemoteData or loadRemoteData ?
               .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('Adding a ai failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };
    // update the given player supplied
    $scope.changeVisitorDetailsForGame = function( cardGame, player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.changeVisitorDetailsForGame( cardGame, player )
        // TODO applyRemoteData or loadRemoteData ?
               .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('Updating player failed: ' + errorMessage, 'Error');
                }
            )
        ;
    };  
    // remove the given player supplied
    $scope.deleteAiPlayerForGame = function( cardGame, player ) {
        // Rather than doing anything clever on the client-side, I'm just
        // going to reload the remote data.
        playerService.deleteAiPlayerForGame( cardGame, player )
        // TODO applyRemoteData or loadRemoteData ?
               .then( applyRemoteData, function( errorMessage ) {
                    toastr.error('Removing one player failed: ' + errorMessage, 'Error');
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
        $scope.players = responseCardGame.players;
        $scope.visitor = responseCardGame.players[0].visitor;

        // set show / hide flags for pictures and buttons
        checkIfNameAndSecuredLoanAreSet();
    };

}]);
