'use strict';

/**
 * @ngdoc overview
 * @name javascriptApp
 * @description
 * # javascriptApp
 *
 * Main module of the application.
 */
angular
  .module('javascriptApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/text', {
        templateUrl: 'views/text.html',
        controller: 'TextCtrl',
        controllerAs: 'text'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
