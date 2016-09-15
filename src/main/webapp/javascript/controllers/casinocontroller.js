angular.module('myApp')
        .controller('CasinoController', CasinoController)
        .directive('myCasinoDirective', function() {
  return {
      restrict: 'AE',
      templateUrl: function(elem, attrs) {
          return attrs.templatePath;
      }
  };
});

CasinoController.$inject = ['$scope', 'PlayerService', 'toastr', 'restangular'];
function CasinoController($scope, PlayerService, toastr, Restangular){

    var vm = this;
    vm.players = PlayerService.initPlayers();

    // flags + checks for ng-if
    vm.showListForDebug = false;
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.tothetable = false;
    checkIfAliensAreSet();
 
    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";

    // next page is under construction
    vm.start = function() {
       toastr.warning('Work in progress', 'Warning') };    

    // behaviour 
    vm.changeAlien = function (sequence) {
        loopAiLevel(sequence);
        if (vm.players[sequence].aiLevel == 'None') {
            vm.players[sequence].cubits = 0;
            vm.players[sequence].securedLoan = vm.players[sequence].cubits;
        } else {
            vm.players[sequence].cubits = (Math.ceil(Math.random() * 500)+ 500);
            vm.players[sequence].securedLoan = vm.players[sequence].cubits;
        };
        checkIfAliensAreSet();    
    };

    // checks and functions
    function checkIfAliensAreSet() {
        if (vm.players[0].cubits != 0 && vm.players[1].cubits != 0 && vm.players[1].aiLevel != 'None')  {
            vm.tothetable = true; 
        }
        if (vm.players[1].aiLevel == 'None') {
            vm.showalien1 = false; 
        } else {
            vm.showalien1 = true; 
        }
        if (vm.players[2].aiLevel == 'None') {
            vm.showalien2 = false;    
        } else {
            vm.showalien2 = true; 
        }
    };
    function loopAiLevel(sequence) {
        if (vm.players[sequence].aiLevel == 'None') {
            if (vm.players[1].aiLevel == 'None' && sequence == 2) {
                vm.players[sequence].aiLevel = 'None';
                //vm.players[sequence].label = vm.none;
            } else {
                vm.players[sequence].aiLevel = 'Dumb';
                //vm.players[sequence].label = vm.dumb;
            };
        } else if (vm.players[sequence].aiLevel == 'Dumb') {
            vm.players[sequence].aiLevel = 'Average';
            //vm.players[sequence].label = vm.average;
        } else if (vm.players[sequence].aiLevel == 'Average') {
            vm.players[sequence].aiLevel = 'Smart';
            //vm.players[sequence].label = vm.smart;
        } else if (vm.players[sequence].aiLevel == 'Smart') {
            if (vm.players[2].aiLevel != 'None' && sequence == 1) {
                vm.players[sequence].aiLevel = 'Dumb';
                //vm.players[sequence].label = vm.dumb;
            } else {
                vm.players[sequence].aiLevel = 'None';
                //vm.players[sequence].label = vm.none;
            };
        };
    };
        
 };
