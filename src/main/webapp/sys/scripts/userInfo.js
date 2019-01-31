'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:userloginInfoCtrl
 * @description # busUnitCtrl Controller of the psspAdminApp
 */
angular.module('psspAdminApp').controller('userInfoCtrl',
	function($scope, $http, $state,$stateParams, $controller, cacheService,localStorageService, alertService, $location,ngDialog) {
	$controller('BaseCtrl', {$scope:$scope});
	
	$scope.userInfoVo = {};
	$scope.disable = true;
	
	$scope.init = function(){
		$http.post('./userManager/currentUser').success(
		function(data) {
			$scope.userInfoVo = data;
			alertService.cleanAlert();
        });
	};
	
	
	
	$scope.save = function() {
		$scope.isSaveCk=true;
		$http.post('./userManager/saveCurrentUser', $scope.userInfoVo).success(
		function(data) {
			console.log($scope.userInfoVo);
			 if (data['errorType'] == "success") {
            	   alertService.saveSuccess();
            	   alertService.showSuccessMsg();
            	   $scope.isSaveCk=false;
               }else if(data['errorType']!=null){
                 alertService.add(data["errorType"], data["errorMessage"]);
              }     
		});	
	}
	$scope.init();
    
	
});