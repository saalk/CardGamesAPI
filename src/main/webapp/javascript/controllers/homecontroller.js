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

HomeController.$inject = ['PlayerService','toastr', 'Restangular'];
function HomeController(PlayerService, toastr, Restangular){

    var vm = this;
    vm.players = PlayerService.initPlayers();
        
    // flags for ng-if
    vm.showListForDebug = false;
    vm.gotocasino = false;
    checkIfNameAndSecuredLoanAreSet();

    // behaviour 
    //vm.minimum = 0;
    vm.setHumanName = function () {
        //toastr.clear();
        vm.players[0].alias = 'Script Doe';
        toastr.info('Your name is set', 'Information');
        checkIfNameAndSecuredLoanAreSet();
    };
    vm.pawnHumanShipForCubits = function (minimum) {
        minimum;
        if (vm.players[0].securedLoan == 0 ) {
            vm.players[0].securedLoan = (Math.ceil(Math.random() * (1000 - minimum))+ minimum);
            vm.players[0].cubits = vm.players[0].cubits + vm.players[0].securedLoan;
            // toastr.info('<body>Your ship is pawned for at least {{ vm.minimum }}<body>', 'InformationL',{allowHtml: true});
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
        PlayerService.savePlayer(vm.players[0]);
    };
    
    // checks and functions
    function checkIfNameAndSecuredLoanAreSet() {
        if (vm.players[0].securedLoan != 0 && vm.players[0].alias != 'stranger') {
            vm.gotocasino = true;
        };
        if (vm.players[0].alias === 'stranger' && vm.players[0].cubits != 0) {
            setTimeout(function() {
            toastr.warning('Get your name for the casino, stranger', 'Warning')},1000);
        } else if (vm.players[0].cubits == 0 && vm.players[0].alias != 'stranger') {
            setTimeout(function() {
            toastr.warning('Pawn your ship for the casino', 'Warning')},1000);
        };
    };
 };
