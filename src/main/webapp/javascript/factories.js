// Define all the factories on the `cardGamesApp` module
var factories = {};
factories.GenericFactory = function ($scope, $filter) {
   $scope.getNumber = function() {
     $scope.bail = (Math.ceil(Math.random() * 3));};
};
cardGamesApp.factory(factories);