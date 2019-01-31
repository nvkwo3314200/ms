'use strict';
angular.module('psspAdminApp')
    .controller('BaseCtrl',
        function ($scope, $state, $http, $interval,$location, alertService,localStorageService,$stateParams) {
    	
    // 权限处理
	$scope.getPowers = function(){
		$http.post(
			'./login/getCurrentPowers',
			$location.url().replace('/main/', '')
		).success(function(data) {
			$scope.powers = data.returnData;
		});
	};	
    	
	// table全选择处理
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
	
	// table 单选处理
	$scope.select = function(id) {
		var found = $scope.selected.indexOf(id);
		if (found == -1){				
			$scope.selected.push(id);
		}
		else{				
			$scope.selected.splice(found, 1);
		}
	};
	
	//  日历控件，有特殊需求可重写或补充
	$scope.dateOptions = {
        dateDisabled: false,
        formatYear: 'yy',
        format:'yyyy/MM/dd',
//	    maxDate: new Date(2020, 5, 22),
//      minDate: new Date(),
        startingDay: 1
    };
	
	// 日历控件初始化,预先初始化6个
	$scope.open1 = function () {
        $scope.popup1.opened = true;
    };
    
    $scope.popup1 = {
        opened: false
    };
    
    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };
    
    $scope.popup2 = {
        opened: false
    };	
    
    $scope.open3 = function () {
        $scope.popup3.opened = true;
    };
    
    $scope.popup3 = {
        opened: false
    };	
    
    $scope.open4 = function () {
        $scope.popup4.opened = true;
    };
    
    $scope.popup4 = {
        opened: false
    };	
    
    $scope.open5 = function () {
        $scope.popup5.opened = true;
    };
    
    $scope.popup5 = {
        opened: false
    };	
    
    $scope.open6 = function () {
        $scope.popup6.opened = true;
    };
    
    $scope.popup6 = {
        opened: false
    };	
    	
});