'use strict';

/**
 * @ngdoc service
 * @name javascriptApp.GMCrypto
 * @description
 * # GMCrypto
 * Service in the javascriptApp.
 */
angular.module('javascriptApp')
  .service('GMCrypto', function ($http) {
    var crypto = {};

    crypto.encBit = function(bit){
    	var params = {
    		bit:bit
    	};
    	return $http({
    		method:'POST',
    		url:'http://localhost:8080/fii-net-sec/GM',
    		params:params
    	});
    };

    return crypto;
  });
