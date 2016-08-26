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
    vm.showList = true;
 
    vm.list = function() {
       vm.players = PlayerService.initPlayers(false);
     };
    vm.list();
;
    
    vm.alien1 = function () {
        vm.players = PlayerService.nextAiLevel('1');
        vm.players[1].fiches = (Math.ceil(Math.random() * 500)+ 500); 
        toastr.info('Alien 1 changed', 'Information');
     };
    vm.alien2 = function () {
        vm.players[2].fiches = (Math.ceil(Math.random() * 500)+ 500);
        toastr.info('Alien 2 changed', 'Information');
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
                {id: '0', 'name': 'stranger', 'aiLevel': 'Human', fiches: '0000'},
                {id: '1', 'name': 'alien 1', 'aiLevel': 'Average', fiches: '0000'},
                {id: '2', 'name': 'alien 2', 'aiLevel': 'Dumb', fiches: '0000'}
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
        nextAiLevel: function(id) {
            //var human = {id: 0, 'name': 'not found', 'aiLevel': 'Human', fiches: '0000'};
            //toastr.success('Configure aiLevel for alien', 'Success');
            for (var i in players) {
                if (players[i].id == id) {
                    // found
                    if (players[i].aiLevel == 'Smart') {
                        players[i].aiLevel = 'Dumb';
                    } else if (players[i].aiLevel == 'Dumb') {
                        players[i].aiLevel = 'Average';
                    } else if (players[i].aiLevel == 'Dumb') {
                        players[i].aiLevel = 'Average';
                    } else if (players[i].aiLevel == 'Average') {
                        players[i].aiLevel = 'Smart';
                    };
                    toastr.success('Alien aiLevel changed', 'Success');
                    return players;
                }
                // not found so add average alien    
                toastr.error('Alien not found', 'Error');
                //savePlayers({id: id, 'name': 'Alien ' + id, 'aiLevel': 'Average', fiches: '0000'});
                return players;
            };
            toastr.error('Alien not searched', 'Error');
            return players;
        },
        deletePlayers: function(id) {
            // existing player
            for (var i in players) {
                if (players[i].id == id)
                players.splice(i, 1);}

        }
    }
};
