'use strict';

/**
 * @ngdoc function
 * @name javascriptApp.controller:TextCtrl
 * @description
 * # TextCtrl
 * Controller of the javascriptApp
 */
angular.module('javascriptApp')
  .controller('TextCtrl', function ($scope, $log, $timeout, GMCrypto) {
  	$scope.inText = '';
    function encryptResult(result){
  		$log.info('encText data ', result);
  		$timeout(function(){
  			$scope.encryptedData = result.data;	
  			$scope.encryptedDataHex = GMCrypto.bin2Hex(result.data);
  		});
  		
  	}
  	function encryptFault(error){
  		$log.error('encText error ', error);
  	}
  	function decryptResult(result){
  		$log.info("got result ", result);
  		$timeout(function(){
  			$scope.decText = result.data;
  		});
  	}
  	function decryptFault(error){
  		$log.error('decrypt error ', error);
  	}


  	$scope.doEncrypt = function(){
  		$scope.decText = null;
  		$scope.encryptedData = "";
  		if(null === $scope.inText){
  			$log.info('no bit ', $scope.inText);
  			return;
  		}
  		$log.info('encrypting ', $scope.inText);
  		GMCrypto.encText($scope.inText).then(encryptResult, encryptFault);
  		
  	};

  	$scope.doDecrypt = function(){
  		$scope.decText = null;
  		if(null === $scope.encryptedData){
  			$log.info('no encryptedData ', $scope.encryptedData);
  			return;	
  		}
  		
  		GMCrypto.decypher($scope.encryptedData).then(decryptResult, decryptFault);
  	};
  });
