angular.module('myApp')
        .controller('TableController', TableController)
        .directive('myTableDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        });

TableController.$inject = ['PlayerService', 'toastr'];
function TableController(PlayerService, toastr){

    var vm = this;
    vm.players = PlayerService.initPlayers();

    // flags + checks for ng-if
    vm.showListForDebug = false;
    vm.higher = true;
    vm.lower = true;
    vm.pass = true;
    vm.generateguess = 0; 
    vm.loopplayer = 0;
    vm.ante = 0; 
    vm.round = 1; 
    vm.showalien1 = false;
    vm.showalien2 = false;
    
    // behaviour
    vm.doguess = function() { 
        vm.generateguess = (Math.random() >= 0.5); // (Math.ceil(Math.random() * 1)); 
        if (vm.generateguess == 0) {
            toastr.success('A good guess', 'Success');
            vm.players[vm.loopplayer].cubits = vm.players[vm.loopplayer].cubits + vm.ante;
        } else {
            toastr.error('Next card differs from you guess', 'Bad luck');
            vm.players[vm.loopplayer].cubits = vm.players[vm.loopplayer].cubits - vm.ante;
        };    
    };
    vm.pass = function() { 
        if (vm.loopplayer < vm.players.length -1 ) {
            if (vm.players[vm.loopplayer + 1].aiLevel == 'None') {
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
    
    init();
    checkIfAliensAreSet();
    // checks and functions
    function init() {
        if (vm.ante == 0) {
            vm.ante = 50;
        };
        for (var i in vm.players) {
            if (vm.players[i].securedLoan == 0 && vm.players[i].aiLevel != 'None') {
                vm.players[i].securedLoan = vm.players[i].cubits;
            };
        };       
        
    };
    function checkIfAliensAreSet() {
        if (vm.players[1].aiLevel == 'None') {
            vm.showalien1 = false; 
        } else {
            vm.showalien1 = true; 
        };
        if (vm.players[2].aiLevel == 'None') {
            vm.showalien2 = false;    
        } else {
            vm.showalien2 = true; 
        }
    };
 };
