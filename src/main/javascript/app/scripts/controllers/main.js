'use strict';

/**
 * @ngdoc function
 * @name javascriptApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the javascriptApp
 */
angular.module('javascriptApp')
  .controller('MainCtrl', function ($scope, $log, GMCrypto) {
    
  	function encryptResult(data){
  		$log.info('encBit data ', data);
  	}
  	function encryptFault(error){
  		$log.error('encBit error ', error);
  	}


  	$scope.doEncrypt = function(){
  		if(null === $scope.bit){
  			$log.info('no bit ', $scope.bit);
  			return;
  		}
  		$log.info('encrypting ', $scope.bit);
  		GMCrypto.encBit($scope.bit).then(encryptResult, encryptFault);
  		
  	};


  });
