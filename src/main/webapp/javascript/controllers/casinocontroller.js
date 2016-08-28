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

CasinoController.$inject = ['PlayerService', 'toastr'];
function CasinoController(PlayerService, toastr){

    var vm = this;
    vm.players = PlayerService.initPlayers();

    // flags + checks for ng-if
    vm.showList = false;
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
    vm.changeAlien = function (id) {
        loopAiLevel(id);
        if (vm.players[id].aiLevel == 'None') {
            vm.players[id].cubits = 0; 
            vm.players[id].pawn = vm.players[id].cubits;
        } else {
            vm.players[id].cubits = (Math.ceil(Math.random() * 500)+ 500); 
            vm.players[id].pawn = vm.players[id].cubits;
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
    function loopAiLevel(id) {
        if (vm.players[id].aiLevel == 'None') {
            if (vm.players[1].aiLevel == 'None' && id == 2) {
                vm.players[id].aiLevel = 'None';    
                vm.players[id].label = vm.none;
            } else {
                vm.players[id].aiLevel = 'Dumb';    
                vm.players[id].label = vm.dumb;
            };
        } else if (vm.players[id].aiLevel == 'Dumb') {
            vm.players[id].aiLevel = 'Average';
            vm.players[id].label = vm.average;
        } else if (vm.players[id].aiLevel == 'Average') {
            vm.players[id].aiLevel = 'Smart';
            vm.players[id].label = vm.smart;
        } else if (vm.players[id].aiLevel == 'Smart') {
            if (vm.players[2].aiLevel != 'None' && id == 1) {
                vm.players[id].aiLevel = 'Dumb';    
                vm.players[id].label = vm.dumb;
            } else {
                vm.players[id].aiLevel = 'None';    
                vm.players[id].label = vm.none;
            };
        };
    };
        
 };
