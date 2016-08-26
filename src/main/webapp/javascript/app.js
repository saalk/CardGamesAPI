// Set an angular module for our cardGamesApp
// Define each module with the angular.module(name, [requires]) syntax only once
// Retrieve it for subsequent use with angular.module(name)
//
// 1. 'ngRoute' is for angular-route
// 2. 'ngAnimate', 'toastr' is for angular-toastr (
angular.module('myApp', ['ngRoute', 'ngAnimate', 'toastr']);
angular.module('myApp')
        .config(function($routeProvider) {

    $routeProvider.when('/home', {
       templateUrl: 'partials/home.html',
       controller: 'HomeController'
    }).when('/casino', {
       templateUrl: 'partials/casino.html',
       controller: 'CasinoController'
    }).otherwise({
        redirectTo: '/home'
    });
 });


angular.module('myApp')
        .controller('HomeController', HomeController);
HomeController.$inject = ['PlayerService','toastr'];
function HomeController(PlayerService, toastr){

    var vm = this;
    vm.welcome = 'Welcome';
    vm.showList = false;

    vm.init = function() {
       vm.players = PlayerService.initPlayers(true);
     };
    vm.init();

    vm.setHumanName = function () {
        toastr.info('Your name is set', 'Information');
        vm.players[0].name = 'John Doe';
        //PlayerService.savePlayer(vm.players[0]);
    };
    
    vm.bailHumanShipForFiches = function () {
        toastr.info('Your ship is bailed', 'Information');
        vm.players[0].fiches = (Math.ceil(Math.random() * 1000));
        //PlayerService.savePlayer(vm.players[0]);
    }; 
    vm.bailCheatHumanShipForFiches = function () {
        if (vm.players[0].fiches == 0 ) {
        toastr.success('Your ship is bailed above 900', 'Cheat applied');
        vm.players[0].fiches = (Math.ceil(Math.random() * 100)+900);
        }
    };
    vm.checkIfNameAndBailAreSet = function () {
          if (vm.players[0].fiches == 0) {
              toastr.error('No bail is set', 'Error');
          }
          if (vm.players[0].name === 'stranger') {
              toastr.error('No name is set', 'Error');
          }
    };

 };


angular.module('myApp')
        .controller('CasinoController', CasinoController);
CasinoController.$inject = ['PlayerService', 'toastr'];
function CasinoController(PlayerService, toastr){

    var vm = this;
    vm.showList = false;
    
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.startthegame = false;
    
    vm.smart = "Most evolved alien species, this fellow start with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";
    
    vm.list = function() {
       vm.players = PlayerService.initPlayers(false);
     };
    vm.list();
    vm.start = function() {
       toastr.warning('Work in progress', 'Warning');
     };    
    vm.alien1 = function () {
        nextAiLevel(1);
        if (vm.players[1].aiLevel == 'None') {
            vm.showalien1 = false;
            toastr.info('Alien 1 left', 'Information');
        } else {
            vm.players[1].fiches = (Math.ceil(Math.random() * 500)+ 500); 
            if (vm.players[0].fiches != 0)  {
                vm.startthegame = true;
            }
            if (vm.players[0].fiches != 0) {
                vm.tothetable = true;
            }
            vm.showalien1 = true;
            toastr.info('Alien 1 changed', 'Information');
        }
    };
    vm.alien2 = function () {
        nextAiLevel(2);
        if (vm.players[2].aiLevel == 'None') {
            vm.showalien2 = false;
            toastr.info('Alien 2 left', 'Information');
        } else {
            vm.players[2].fiches = (Math.ceil(Math.random() * 500)+ 500); 
            vm.showalien2 = true;
            toastr.info('Alien 1 changed', 'Information');
        }
    };
    function nextAiLevel(id) {
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
 
angular.module('myApp')
        .service('PlayerService', PlayerService); 
PlayerService.$inject = ['toastr'];
function PlayerService(toastr){

   var players = [];
   uid = 0;
   return {
        initPlayers: function(init) {
            if (init && (players[0] == null)) {
            players = [
                {id: 0, 'name': 'stranger', 'aiLevel': 'Human', 'label': 'Bailing your ship gained you ', fiches: '0000'},
                {id: 1, 'name': 'alien 1', 'aiLevel': 'None', 'label': 'This alien has left the game with ', fiches: '0000'},
                {id: 2, 'name': 'alien 2', 'aiLevel': 'None', 'label': 'This alien has left the game with ', fiches: '0000'}
                ];
                toastr.success('Players initializing...', 'Success');
            };
            return players;
        },
        listPlayers: function() {
            return players;
        },
        savePlayers: function(player) {
            if (player.id == null) {
            // new player
               player.id = uid++;
               players.push(player);
            } else {
             // existing player
              for (var i in players) {
                  if (players[i].id == player.id)
                     players[i] = player;}
            }
        },
        getPlayer: function(id) {
            // existing player
            for (var i in players) {
                if (players[i].id == id)
                return players[i];
            }
        },
        deletePlayers: function(id) {
            // existing player
            players.forEach(function () {
                if (players[i].id == id)
                players.splice(i, 1);
            });
        }
    }
};
