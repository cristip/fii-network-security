'use strict';

describe('Controller: TextCtrl', function () {

  // load the controller's module
  beforeEach(module('javascriptApp'));

  var TextCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TextCtrl = $controller('TextCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(TextCtrl.awesomeThings.length).toBe(3);
  });
});
