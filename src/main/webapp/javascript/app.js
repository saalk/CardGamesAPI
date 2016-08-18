//Set an angular module for our cardGamesApp
// Define each module with the angular.module(name, [requires]) syntax only once
// Retrieve it for subsequent use with angular.module(name)
//
// 1. 'ngRoute' is for angular-route
// 2. 'ngAnimate', 'toastr' is for angular-toastr (
var app = angular.module('myApp', ['ngRoute', 'ngAnimate', 'toastr']);

app.config(function($routeProvider) {

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

app.controller('HomeController', function ($scope, playerfactory, toastr) {

    var vm = this;
    $scope.message = '';
    $scope.welcome = 'Welcome';
    $scope.players = []; // add with .push() and splice(addafterpos,numitemdelete,'item')
    $scope.players = playerfactory.listPlayers();
    vm.getHumanPlayer;
    vm.getHumanPlayer = function() {
        $scope.human = angular.copy(playerfactory.getPlayer(0));
    }

    //$scope.bailHumanShipForFiches = function () {
    //    $scope.human = playerfactory.getHumanPlayer();
    //    $scope.human.fiches = (Math.ceil(Math.random() * 1000));
        // (Math.ceil(Math.random() * 500)) + 500;
    //   savePlayer($scope.human);
    // };

    // function savePlayer(player) {
    //    playerfactory.savePlayers(player);
    // };
 });

app.controller('CasinoController', function ($scope, playerfactory, toastr) {

    //toastr.success('Hello casino!', 'Toastr fun!', {iconClass: 'toast-pink'});
    toastr.success('Success');
    // toastr.info('We are open today from 10 to 22', 'Information');

    $scope.players = playerfactory.listPlayers();
    $scope.human = playerfactory.getHumanPlayer();
    $scope.alien1 = {id: 1, name: 'alien1', aiLevel: 'Smart', fiches: '0000'};
    $scope.alien2 = {id: 2, name: 'alien2', aiLevel: 'Dumb', fiches: '0000'};

    $scope.addAlien = function (id) {
        $scope.alien = playerfactory.getPlayer(id);
        $scope.alien.fiches = (Math.ceil(Math.random() * 500) + 500);
        playerfactory.savePlayer($scope.alien);
     };
 });

app.factory('playerfactory', function() {

    var factory = {};
    uid = 0;
    factory.listPlayers = function() {
        var players = [
            {id: 0, 'name': 'stranger', 'aiLevel': 'Human', fiches: '0000'},
            {id: 1, 'name': 'first', 'aiLevel': 'Average', fiches: '0000'},
            {id: 2, 'name': 'second', 'aiLevel': 'Dumb', fiches: '0000'}
            ];
        return players;
    /*  use $resource
            See more at:
            http://www.tothenew.com/blog/angulars-resource-for-crud-operations/#sthash.R87Vw97y.dpuf
            http://www.tothenew.com/blog/angulars-resource-for-crud-operations/
     */
    }
    factory.savePlayers = function() {
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
    }
    factory.getPlayer = function(id) {
        // existing player
        for (var i in players) {
            if (players[i].id == id)
            return players[i];}
    }
    factory.getHumanPlayer = function() {
        // existing player
        for (var i in players) {
            if (players[i].aiLevel == 'Human')
            return players[i];}
    }
    factory.deletePlayers = function(id) {
        // existing player
        for (var i in players) {
            if (players[i].id == id)
            players.splice(i, 1);}

    }
    return factory;
});
