'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:RoleManagerCtrl
 * @description
 * # RoleManagerCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
    .controller('RoleManagerCtrl', 
        function ($scope, $state, cacheService,$http, alertService, ngDialog, $location,$stateParams) {
		 $scope.RoleVo = {};
		 $scope.tableData = [];
         $scope.userName = "";
         $scope.activate = "Y";
         $scope.page = {};
         $scope.searchModel = cacheService.getCache();
         cacheService.setCache($scope.searchModel);
         $scope.pageModel = $scope.searchModel;
         $scope.init = function(){
 			$scope.getPowers();
 			alertService.showSuccessMsg();
 			};
         
    	 /**
    	  * id 
    	  */
    	 
         $scope.selected = [];
         $scope.selectAll = function (collection) {
            if ($scope.selected.length === 0) {
                 angular.forEach(collection, function (val) {
                     $scope.selected.push(val);

                 });
            } else if ($scope.selected.length > 0 && $scope.selected.length != $scope.tableData.length) {
                 angular.forEach(collection, function (val) {
                     var found = $scope.selected.indexOf(val);
                     if (found == -1) $scope.selected.push(val);
                 });
            } else {
                 $scope.selected = [];

             }

         };
        $scope.select = function (id) {
             var found = $scope.selected.indexOf(id);
             if (found == -1) {
            	 $scope.selected.push(id);
             }else {
            	 $scope.selected.splice(found, 1);
            	 };
         };
         
         $scope.select1 = function(role) {
        	 $state.go('main.roleManager_add',{'roleId': role.id});
 			
 		};
         
   		$scope.search = function(flag) {
   			console.log($scope.searchModel);
   			$http.post('./roleManager/searchRole', $scope.searchModel).success(			
					function(data) {
					$scope.tableData = data.list;
					$scope.page = data;
                    $scope.selected = [];
                    if(flag||false) alertService.cleanAlert();
                }
			);
        };
           
            
        $scope.addRole = function(){
                $state.go('main.roleManager_add');
            };
            
    		
    	$scope.resert= function () {
    		$scope.searchModel.roleName=undefined;
    		cacheService.clearCache();
    	};
            
        $scope.editRole = function(){
        		if($scope.selected.length != 1){
	        		 
	        		 alertService.add('danger', 'Please select one record.');
	    			 return;
	             }
        		 var roleId=$scope.selected[0]["id"];
        		 $state.go('main.roleManager_add',{'roleId': roleId});
        	};
        	
        	
        $scope.view = function(){
        		 if($scope.selected.length != 1){
	        		 
	        		 alertService.add('danger', 'Please select one record.');
	    			 return;
	             }
        		 var roleId=$scope.selected[0]["id"];
        		 var state=true;
        		 $state.go('main.roleManager_add',{'roleId': roleId,'state':state});
        	};
        	
        $scope.deletes = function(isDelete){
        	if( $scope.selected.length < 1){
            		
	        		alertService.add('danger', 'Please select one record.');
	    			return;
                 }
        	if (!isDelete) {
			    $('#confirmModal').modal();
			    return;
			      }
	    	var roleIds = "";
	        for (var i = 0; i < $scope.selected.length; i++) {
	        	roleIds = roleIds+$scope.selected[i]["id"] + ","; 	
	        }
	         $http({
	              method: 'POST',
	              params: {
	                roleIds: roleIds
	              },
	              url: "./roleManager/deleteRole"
	   	         }).success(function (data) {
	   	             if (data['errorType'] != "success") {
	   	            	$scope.selected = [];
	   	            	
	   	                alertService.add(data["errorType"], data["errorMessage"]);
	   	             } else {
	   	                
	   	                alertService.add("success", "Deleted successfully.");
	   	                $scope.searchModel.roleName=undefined;
	   	                $scope.search();
	   	               }
	   	          });
        	};
        	
        	// LY
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
       	
        $scope.getPowers = function(){
			   $http.post(
			    './login/getCurrentPowers',
			    $location.url().replace('/main/', '')
			   ).success(function(data) {
				console.log(data.returnData);
			    $scope.powers = data.returnData;
			   });
			  };
        });
