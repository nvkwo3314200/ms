'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:busUnitCtrl
 * @description # busUnitCtrl Controller of the psspAdminApp
 */
angular.module('psspAdminApp').controller('busUnitCtrl',
	function($scope, $http,$stateParams, $state, $controller, localStorageService, alertService, $location,ngDialog, cacheService) {
		
		$controller('BaseCtrl', {$scope:$scope});
		
		$scope.tableData = [];
		$scope.selected = [];
		$scope.page = {};
		$scope.model = cacheService.getCache();
		cacheService.setCache($scope.model);
		$scope.pageModel = $scope.model;
		$scope.init = function(){
			$scope.getPowers();
			alertService.showSuccessMsg();
		};

		$scope.selectAll = function(collection) {
			if ($scope.selected.length === 0) {
				angular.forEach(collection, function(val) {			
					$scope.selected.push(val);			
				});
			} else if ($scope.selected.length > 0 && $scope.selected.length != $scope.tableData.length) {
				angular.forEach(collection, function(val) {
					var found = $scope.selected.indexOf(val);
					if (found == -1)
						$scope.selected.push(val);					
				});
			} else {
				$scope.selected = [];
			}
		};

		$scope.select = function(id) {
			var found = $scope.selected.indexOf(id);
			if (found == -1)
				$scope.selected.push(id);
			else
				$scope.selected.splice(found, 1);
		};

		$scope.create = function() {
			$state.go('main.bus_add');
		};
		
		$scope.select1 = function(id) {
			$state.go('main.bus_add', {'id' : id});
		};
		
		$scope.search = function(flag) {
		$http.post('./busUnit/select', $scope.model).success(
			function(data) {
				if (data['errorType'] == "success") {
					$scope.tableData = data.page.list;
					$scope.page = data.page;
					if(flag||false) alertService.cleanAlert();
				}
			}
		);
		};
		
         $scope.run = function(){
        	 alertService.cleanAlert();
        	  $http.post('./busUnit/run', {"sql":$scope.sql})
        	  .success(function(data) {
        		  alertService.add('success','exec success');
        	  });
         };
          
		$scope.edit = function() {
			if ($scope.selected.length != 1) {
				
				alertService.add('danger', 'Please select one record.');
				return;
			}
			var busUnitCode = $scope.selected[0]['busUnitCode'];
			$state.go('main.bus_add', {'busUnitCode' : busUnitCode});
		};
		
		$scope.view = function() {
			if ($scope.selected.length != 1) {
				
				alertService.add('danger', 'Please select one record.');
				return;
			}
			var busUnitCode = $scope.selected[0]['busUnitCode'];
			var state=true;
			$state.go('main.bus_add', {'busUnitCode' : busUnitCode,'state':state});
		};
		
		
		$scope.deletes = function(isDelete) {
			if ($scope.selected.length < 1) {
				
				alertService.add('danger', 'Please select one record.');
				return;
			}
			 if (!isDelete){
				    $('#confirmModal').modal({keyboard: false});
				    return;
				    }
			$http.post('./busUnit/delete', $scope.selected).success(
				function(data) {
					if (data['errorType'] == "success") {
						$scope.selected = [];
						$scope.tableData = data.returnDataList;
						alertService.cleanAlert();
						alertService.add(data["errorType"],
								"Deleted successfully.");
					} else {
						
						alertService.add(data["errorType"],
						data["errorMessage"]);
					}
				}
			);
		};
		
		$scope.auditTrail = function(){
   		 if($scope.selected.length != 1){
       		 
       		 alertService.add('danger', 'Please select one record.');
   			 return;
            }
   		 $scope.auditTrailData = $scope.selected[0];
   		 ngDialog.open({
   	  		    className: 'ngdialog-theme-default custom-width-800',
   	  		    template: 'sys/layout/auditTrailDialog.html',
	   	  		closeByEscape: false,
				closeByDocument: false,
   	  		    scope: $scope,
   	  		    controller: ['$scope', 
   							function($scope) {
       				$scope.cancel = function (){
       					$scope.closeThisDialog(0);
       				};
   	  		    }],
   	  		    preCloseCallback: function(value) {
   	  		    	  alertService.cleanAlert();
   	  		    }
   	  		});
   	};
   	
		  
		  $scope.selectedit = function(row) {
				 var found = $scope.selected.indexOf(row);
	             if($scope.selected.length==1){
	            	var editFlag = true;
	     			var timeType = $scope.selected[0]['timeType'];
	     			$state.go('main.timetype_add', {'timeType' : timeType,'editFlag' : editFlag});
	             }
	 			else if(found == -1){
	 				$scope.selected.push(row);
	 				$scope.edit();
	 			}else
	             	$scope.selected.splice(found, 1);
	 				
	 				alertService.add('danger', 'Please select one record.');
	 				return;
			};
		  
		  $scope.reset = function(){
			  $scope.model.busUnitName= undefined;
			  cacheService.clearCache();
			};
	}
);