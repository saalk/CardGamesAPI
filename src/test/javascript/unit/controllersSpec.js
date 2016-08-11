'use strict';

/* jasmine specs for controllers go here */

describe('Controller tests', function () {

   describe('HomeController', function () {
      var scope, ctrl;

      beforeEach(module('cardGamesApp'));
      beforeEach(inject(function ($rootScope, $controller) {
         scope = $rootScope.$new();
         ctrl = $controller('HomeController', {$scope: scope});

      }));

       it('human player name should contain stranger', function () {
         expect(scope.humanplayer.name).toBe('stranger');
      });
   });
});
