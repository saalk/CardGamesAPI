'use strict';

/* jasmine specs for controllers go here */

describe('Controller tests', function () {

   describe('homeController', function () {
      var scope, ctrl;

      beforeEach(module('app'));
      beforeEach(inject(function ($rootScope, $controller) {
         scope = $rootScope.$new();
         ctrl = $controller('homeController', {$scope: scope});
         /* for testing */
         scope.title = 'Start '
      }));

       it('should contain start', function () {
         expect(scope.title).toBe('Start ');
      });
   });
});
