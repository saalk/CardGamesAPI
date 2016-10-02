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

CasinoController.$inject = ['PlayerService', 'toastr', 'Restangular'];
function CasinoController(PlayerService, toastr, Restangular){

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
    vm.changeAlien = function (playingOrder) {
        loopAiLevel(playingOrder);
        if (vm.players[playingOrder].aiLevel == 'None') {
            vm.players[playingOrder].cubits = 0;
            vm.players[playingOrder].securedLoan = vm.players[playingOrder].cubits;
        } else {
            vm.players[playingOrder].cubits = (Math.ceil(Math.random() * 500)+ 500);
            vm.players[playingOrder].securedLoan = vm.players[playingOrder].cubits;
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
    function loopAiLevel(playingOrder) {
        if (vm.players[playingOrder].aiLevel == 'None') {
            if (vm.players[1].aiLevel == 'None' && playingOrder == 2) {
                vm.players[playingOrder].aiLevel = 'None';
                //vm.players[playingOrder].label = vm.none;
            } else {
                vm.players[playingOrder].aiLevel = 'Dumb';
                //vm.players[playingOrder].label = vm.dumb;
            };
        } else if (vm.players[playingOrder].aiLevel == 'Dumb') {
            vm.players[playingOrder].aiLevel = 'Average';
            //vm.players[playingOrder].label = vm.average;
        } else if (vm.players[playingOrder].aiLevel == 'Average') {
            vm.players[playingOrder].aiLevel = 'Smart';
            //vm.players[playingOrder].label = vm.smart;
        } else if (vm.players[playingOrder].aiLevel == 'Smart') {
            if (vm.players[2].aiLevel != 'None' && playingOrder == 1) {
                vm.players[playingOrder].aiLevel = 'Dumb';
                //vm.players[playingOrder].label = vm.dumb;
            } else {
                vm.players[playingOrder].aiLevel = 'None';
                //vm.players[playingOrder].label = vm.none;
            };
        };
    };
        
 };
