angular.module('myApp')
        .controller('ResultsController', ResultsController)
        .directive('myResultsDirective', function() {
            return {
                restrict: 'AE',
                templateUrl: function(elem, attrs) {
                    return attrs.templatePath;
                }
            };
        });

ResultsController.$inject = ['$scope', 'PlayerService', 'toastr', 'restangular'];
function ResultsController($scope, PlayerService, toastr, Restangular){

    var vm = this;
    vm.players = PlayerService.initPlayers();

    // flags + checks for ng-if
    vm.showListForDebug = false;
    vm.showalien1 = false;
    vm.showalien2 = false;
    vm.ante = 0; 
    vm.loopplayer = 0;
    vm.tothetable = false;

    // next page is under construction
    vm.start = function() {
       toastr.warning('Work in progress', 'Warning'); };    

    
    checkIfAliensAreSet();
    setAnte();
 
    // fixed text
    vm.smart = "Most evolved alien species, this fellow starts with ";
    vm.average = "A nice competitor, he has a budget of ";
    vm.dumb = "Quick to beat, starting with ";
    vm.none = "This alien has left the game with ";

    // behaviour 
    vm.changeAlien = function (sequence) {
        loopAiLevel(sequence);
        checkIfAliensAreSet();    
    };

    // checks and functions
    function setAnte() {
        if (vm.ante == 0) {
            vm.ante = 50;
        };
    };
    vm.pawnHumanShipForCubits = function () {
        if (vm.players[0].securedLoan == 0 ) {
            vm.players[0].securedLoan = (Math.ceil(Math.random() * 750)+250);
            vm.players[0].cubits = vm.players[0].cubits + vm.players[0].securedLoan;
            toastr.info('Your ship is pawned', 'Information');
        } else if (vm.players[0].cubits < vm.players[0].securedLoan) {
            toastr.error('Your don\'t have not enough credits', 'Error');
        } else if (vm.players[0].cubits >= vm.players[0].securedLoan) {
            vm.players[0].cubits = vm.players[0].cubits - vm.players[0].securedLoan;
            vm.players[0].securedLoan = 0;
            toastr.info('Your loan is repayed', 'Information');
            vm.tothetable = false;
        };
        checkIfAliensAreSet();
    }; 
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
                vm.players[sequence].label = vm.none;
            } else {
                vm.players[sequence].aiLevel = 'Dumb';
                vm.players[sequence].label = vm.dumb;
            };
        } else if (vm.players[sequence].aiLevel == 'Dumb') {
            vm.players[sequence].aiLevel = 'Average';
            vm.players[sequence].label = vm.average;
        } else if (vm.players[sequence].aiLevel == 'Average') {
            vm.players[sequence].aiLevel = 'Smart';
            vm.players[sequence].label = vm.smart;
        } else if (vm.players[sequence].aiLevel == 'Smart') {
            if (vm.players[2].aiLevel != 'None' && sequence == 1) {
                vm.players[sequence].aiLevel = 'Dumb';
                vm.players[sequence].label = vm.dumb;
            } else {
                vm.players[sequence].aiLevel = 'None';
                vm.players[sequence].label = vm.none;
            };
        };
    };
 };
