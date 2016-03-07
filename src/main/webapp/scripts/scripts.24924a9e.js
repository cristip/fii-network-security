"use strict";angular.module("javascriptApp",["ngAnimate","ngCookies","ngResource","ngRoute","ngSanitize","ngTouch"]).config(["$routeProvider",function(a){a.when("/",{templateUrl:"views/main.html",controller:"MainCtrl",controllerAs:"main"}).when("/about",{templateUrl:"views/about.html",controller:"AboutCtrl",controllerAs:"about"}).otherwise({redirectTo:"/"})}]),angular.module("javascriptApp").controller("MainCtrl",["$scope","$log","GMCrypto",function(a,b,c){function d(a){b.info("encBit data ",a)}function e(a){b.error("encBit error ",a)}a.doEncrypt=function(){return null===a.bit?void b.info("no bit ",a.bit):(b.info("encrypting ",a.bit),void c.encBit(a.bit).then(d,e))}}]),angular.module("javascriptApp").controller("AboutCtrl",function(){this.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}),angular.module("javascriptApp").service("GMCrypto",["$http",function(a){var b={};return b.encBit=function(b){var c={bit:b};return a({method:"POST",url:"http://localhost:8080/fii-net-sec/GM",params:c})},b}]),angular.module("javascriptApp").run(["$templateCache",function(a){a.put("views/about.html","<p>This is the about view.</p>"),a.put("views/main.html",'<div class="container-fluid"> <div class="row"> <h4>Encrypt Single Bit</h4> <p> Alegeti valoarea bitului m si apoi apasati Criptare: </p> </div> <div class="row"> <div class="col-sm-12"> <div class="form-group"> <label for="valM">valoarea lui m:</label> <div class="radio"> <label> <input type="radio" ng-model="bit" name="optionsRadios" id="bit0" value="0" checked> Valoarea 0 </label> </div> <div class="radio"> <label> <input type="radio" ng-model="bit" name="optionsRadios" id="bit1" value="1"> Valoarea 1 </label> </div> </div> </div> </div> <div class="row"> <button ng-disabled="!bit" ng-click="doEncrypt()" type="button" role="button" class="btn btn-primary btn-active">Criptare</button> </div> <div class="row"> <h4>Rezultatul criptarii:</h4> <textarea rows="5" cols="140">{{$enctext}}</textarea> </div> </div>')}]);