/* jasmine specs for controllers go here */

describe('HomeController tests', function () {

    /* Controllers are not available on the global scope: use angular.mock.inject first.
    1 - The first step is to use the module function, which is provided by angular-mocks.
        This loads in the module it's given, so it is available in your tests.
        We pass this into beforeEach, which is a function Jasmine provides that lets us run code
        before each test.
    2 - Then we can use inject to access $controller, the service that is responsible
        for instantiating controllers. */

    beforeEach(module('cardGamesApp'));
    var $controller;

    beforeEach(inject(function(_$controller_){
        // The injector unwraps the underscores (_) from around the parameter names when matching
        $controller = _$controller_; }));

    // Jasmine provides beforeEach, which lets us run a function before each individual test.
    var $scope, controller;
    beforeEach(function() {
      $scope = {};
      controller = $controller('HomeController', { $scope: $scope });
    });

    it('humanplayer.name should contain "stranger" at the start', function () {
        expect($scope.humanplayer.name).toBe('stranger');
    });
    it('bail should not contain 0 after "BAIL: 0000" clicked', function () {
        $scope.getBail();
        expect($scope.bail).not.toBe(0);
    });
});

