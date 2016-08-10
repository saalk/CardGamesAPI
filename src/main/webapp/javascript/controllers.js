// Define all the controller on the `cardGamesApp` module
var controllers = {};
controllers.HomeController = function ($scope, $filter) {
    $scope.alien = 'stranger';

    $scope.bail = 0;
    $scope.getNumber = function() {
        $scope.bail = (Math.ceil(Math.random() * 1000));};
};
controllers.CasinoController = function ($scope, $filter) {
    $scope.alien = 'stranger';

    $scope.fiches1 = 0;
    $scope.getNumber = function() {
        $scope.fiches1 = (Math.ceil(Math.random() * 1000));};

    $scope.fiches2 = 0;
    $scope.getNumber = function() {
        $scope.fiches2 = (Math.ceil(Math.random() * 1000));};
};
cardGamesApp.controller(controllers);
