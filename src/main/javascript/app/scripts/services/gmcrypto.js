'use strict';

/**
 * @ngdoc service
 * @name javascriptApp.GMCrypto
 * @description
 * # GMCrypto
 * Service in the javascriptApp.
 */
angular.module('javascriptApp')
  .service('GMCrypto', function ($http, $log) {
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
    crypto.encText = function(plain){
    	var params = {
    		plain:plain
    	};
    	return $http({
    		method:'POST',
    		url:'http://localhost:8080/fii-net-sec/GM',
    		params:params
    	});
    };
    crypto.decypher = function(cryptoText){
    	var params = {
    		crypto:cryptoText
    	};
    	return $http({
    		method:'POST',
    		headers:{'Content-Type': 'application/json'},
    		url:'http://localhost:8080/fii-net-sec/GM',
    		data:JSON.stringify(params)
    	});
    };
    crypto.decodeSingleBit = function(ctext){
    	var params = {
    		ctext:ctext
    	};
    	return $http({
    		method:'POST',
    		headers:{'Content-Type': 'application/json'},
    		url:'http://localhost:8080/fii-net-sec/GM',
    		data:JSON.stringify(params)
    	});
    };
    crypto.hex2Bin = function(hexStr){
  		var fixLeading = function(binStr){
  			var diff = 8 - binStr.length;
  			for(var i  = 0; i < diff; i++){
  				binStr = '0'+binStr;
  			}
  			return binStr;
  		};
  		var hexBytes = hexStr.split(' ');
  		var result = [];
  		for(var i = 0; i < hexBytes.length; i++){
  			result.push(fixLeading(parseInt(hexBytes[i],16).toString(2)));
  		}
  		return result.join(' ');
  	};
  	crypto.bin2Hex = function(binStr){
  		var result = [];
  		for(var i = 0; i < binStr.length; i+=8){
  			var segment = binStr.substr(i, 8);
  			$log.info('segment ', segment);
  			var integerValue = parseInt(segment, 2);
  			$log.info('integerValue ', integerValue.toString(16));
  			result.push(integerValue.toString(16));
  		}
  		return result.join(' ');
  	};

    return crypto;
  });
