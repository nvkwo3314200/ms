'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:userloginInfoCtrl
 * @description # busUnitCtrl Controller of the psspAdminApp
 */
angular.module('psspAdminApp').controller('userloginInfoCtrl',
	function($scope, $http, $state,$stateParams, $controller, cacheService,localStorageService, alertService, $location,ngDialog) {
	$controller('BaseCtrl', {$scope:$scope});
	$scope.tableData = [];
	$scope.selected=[];
	$scope.searchModel = cacheService.getCache();
	$scope.page = {};
	cacheService.setCache($scope.searchModel);
	$scope.pageModel = $scope.searchModel;
	$scope.search=function(){
		$http.post('./userLoginInfo/search', $scope.searchModel).success(
				function(data) {
					$scope.tableData =data.list;
					$scope.page = data;
					$scope.selected = [];
					alertService.cleanAlert();
	            });
	};
	
	$scope.selectAll = function(collection) {
		if ($scope.selected.length === 0) {
			angular.forEach(collection, function(val) {
				$scope.selected.push(val);
			});
		} else if ($scope.selected.length > 0 && $scope.selected.length != $scope.tableData.length) {
			angular.forEach(collection, function(val) {
				var found = $scope.selected.indexOf(val);
				if (found == -1){						
					$scope.selected.push(val);
				}
			});
		} else {
			$scope.selected = [];
		}
	};
	
	$scope.open1 = function () {
        $scope.popup1.opened = true;
    };
    
    $scope.resert = function(){
    	$scope.searchModel.loginUserInfoId=undefined;
    	$scope.searchModel.userName=undefined;
    	$scope.searchModel.roleCode=undefined;
    	$scope.searchModel.plantCode=undefined;
    	$scope.searchModel.prodLn=undefined;
    	$scope.searchModel.operateDate=undefined;
    };
    
    $scope.popup1 = {
            opened: false
        };
       
        $scope.dateOptions = {
            dateDisabled: false,
            formatYear: 'yy',
            format:'yyyy/MM/dd',
//            maxDate: new Date(2020, 5, 22),
            //minDate: new Date(),
            startingDay: 1
        };
	$scope.select = function(id) {
		var found = $scope.selected.indexOf(id);
		if (found == -1){				
			$scope.selected.push(id);
		}
		else{				
			$scope.selected.splice(found, 1);
		}
	};
});