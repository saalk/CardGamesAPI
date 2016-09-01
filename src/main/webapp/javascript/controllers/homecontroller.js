angular.module('myApp')
        .controller('HomeController', HomeController)
        .directive('myHomeDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        });

HomeController.$inject = ['PlayerService','toastr'];
function HomeController(PlayerService, toastr){

    var vm = this;
    vm.players = PlayerService.initPlayers();
        
    // flags for ng-if
    vm.showList = false;
    vm.gotocasino = false;
    checkIfNameAndSecuredLoanAreSet();

    // behaviour 
    vm.setHumanName = function () {
        vm.players[0].alias = 'John Doe';
        toastr.info('Your name is set', 'Information');
        checkIfNameAndSecuredLoanAreSet();
    };
    vm.pawnHumanShipForCubits = function () {
        if (vm.players[0].securedLoan == 0 ) {
            vm.players[0].cubits = (Math.ceil(Math.random() * 750)+250);
            vm.players[0].securedLoan = vm.players[0].cubits;
            toastr.info('Your ship is pawned', 'Information');
        } else if (vm.players[0].cubits < vm.players[0].securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if (vm.players[0].cubits >= vm.players[0].securedLoan) {
            vm.players[0].cubits = vm.players[0].cubits - vm.players[0].securedLoan;
            vm.players[0].securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.gotocasino = false;
        };
        checkIfNameAndSecuredLoanAreSet();
    }; 
    vm.pawnCheatHumanShipForCubits = function () {
        if (vm.players[0].securedLoan == 0 ) {
            vm.players[0].cubits = (Math.ceil(Math.random() * 250)+750);
            vm.players[0].securedLoan = vm.players[0].cubits;
            toastr.success('Your ship is pawned above 750!', 'Cheat applied');
        };
        checkIfNameAndSecuredLoanAreSet();
    };
    
    // checks and functions
    function checkIfNameAndSecuredLoanAreSet() {
        if (vm.players[0].securedLoan != 0 && vm.players[0].alias != 'stranger') {
            vm.gotocasino = true;
        };
        if (vm.players[0].alias === 'stranger' && vm.players[0].cubits != 0) {
            setTimeout(function() {
            toastr.warning('Get your name, stranger', 'Warning')},1000);
        } else if (vm.players[0].cubits == 0 && vm.players[0].alias != 'stranger') {
            setTimeout(function() {
            toastr.warning('Pawn your ship for the casino', 'Warning')},1000);
        };
    };
 };
