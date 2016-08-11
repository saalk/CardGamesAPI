// Define all the factories on the `cardGamesApp` module
var factories = {};
factories.GenericFactory = function () {
    $scope.alien = 'stranger';

    $scope.bail = 0;
    $scope.fiches1 = 0;
    $scope.fiches2 = 0;
    $scope.calculateFiches = function() {
        $scope.bail = (Math.ceil(Math.random() * 1000));
        $scope.fiches1 = (Math.ceil(Math.random() * 500)) + 500;
        $scope.fiches2 = (Math.ceil(Math.random() * 500)) + 500 ; };
    return factory
};
cardGamesApp.factory(factories);