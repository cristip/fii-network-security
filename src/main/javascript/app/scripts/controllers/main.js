'use strict';

/**
 * @ngdoc function
 * @name javascriptApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the javascriptApp
 */
angular.module('javascriptApp')
  .controller('MainCtrl', function ($scope, $log, $timeout, GMCrypto) {
    
  	function hex2Bin(hexStr){
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
  	}

  	function encryptResult(result){
  		$log.info('encBit data ', result);
  		$timeout(function(){
  			$scope.encryptedData = result.data;	
  			$scope.encryptedDataBin = hex2Bin(result.data);
  		});
  		
  	}
  	function encryptFault(error){
  		$log.error('encBit error ', error);
  	}
  	function decryptResult(result){
  		$timeout(function(){
  			$scope.decBit = result.data;
  		});
  	}
  	function decryptFault(error){
  		$log.error('decrypt error ', error);
  	}


  	$scope.doEncrypt = function(){
  		$scope.decBit = null;
  		if(null === $scope.bit){
  			$log.info('no bit ', $scope.bit);
  			return;
  		}
  		$log.info('encrypting ', $scope.bit);
  		GMCrypto.encBit($scope.bit).then(encryptResult, encryptFault);
  		
  	};

  	$scope.doDecrypt = function(){
  		$scope.decBit = null;
  		if(null === $scope.encryptedData){
  			$log.info('no encryptedData ', $scope.encryptedData);
  			return;	
  		}
  		$log.info('decrypting ', $scope.encryptedData);
  		GMCrypto.decodeText($scope.encryptedData).then(decryptResult, decryptFault);
  	};


  });
