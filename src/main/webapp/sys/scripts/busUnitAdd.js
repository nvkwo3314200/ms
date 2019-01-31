'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:busUnitAddCtrl
 * @description
 * # busUnitAddCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
    .controller('busUnitAddCtrl', ['$scope', '$state', '$http', '$interval', 'alertService','localStorageService','$stateParams',
        function ($scope, $state, $http, $interval, alertService, localStorageService, $stateParams) {
    	$scope.busUnitModel = {};
    	$scope.isSaveCk = false;
    	$scope.state=false;
    	init();
    	function init(){
    		search();
    	}
    	
    	function search(){
    		if($stateParams.busUnitCode != null){
    			$http.post('./busUnit/search', {'busUnitCode':$stateParams.busUnitCode}).
    			success(function(data) {
    				if (data['errorType'] == "success") {
    					$scope.state = $stateParams.state;
    					$scope.busUnitModel = data.returnDataList[0];
    					alertService.cleanAlert();
    				}
    			});
    		}    		
    	}
 		
     	$scope.save= function() {
     		$scope.isSaveCk = true;
     		$scope.busUnitModel.falg=true;
     		if($scope.busUnitModel.busUnitCode!=null&&$scope.busUnitModel.busUnitName!=null){
     			$http.post('./busUnit/save', $scope.busUnitModel).
     			success(function(data) {
     				if (data['errorType'] == "success") {
//     					$scope.busUnitModel = data.returnData;
//     					
//     					if(createdBy == null){
//     						alertService.add(data["errorType"], "Saved successfully. ");
//     					} else {
//     						alertService.add(data["errorType"], "Saved successfully. ");
//     					}
     					alertService.saveSuccess();
		        		$scope.cancel();
     				}else if(data['errorType']!=null) {
     					 
     					alertService.add(data["errorType"], data["errorMessage"]);
     				}     
     			});    			
     		}
     	};
              
     	$scope.cancel =  function() {
     		$state.go('main.bus_unit'); 							
     	};
	 
}]);
