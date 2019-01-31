'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:ProductViewCtrl
 * @description
 * # ProductViewCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
    .controller('UserManagerAddCtrl', ['$scope', '$state', '$http', '$interval', 'alertService','localStorageService','$stateParams',
                                       'ngDialog','$window', '$confirm', "IntegralUITreeGridService", "$timeout","$filter","dateUtilsService",
        function ($scope, $state, $http, $interval, alertService,localStorageService,$stateParams
        			, ngDialog, $window, $confirm, $gridService, $timeout, $filter, dateUtilsService) {
    	 
    	$scope.isSaveCk=false;
		$scope.userInfoVo = {};
		
		//lijun
		$scope.falg=false;
		$scope.productline=[];
		$scope.ProdLnList=[];
		$scope.PlantList=[];
		$scope.brandSelect=[];
		$scope.brandList=[];
		$scope.prodSelect=[];
		$scope.prodList=[];
		$scope.prod=[];
		$scope.prodln=[];
		$scope.plant=[];
		$scope.plantcode=[];
		//LY new mothoed Pop--------------------------
	 	$scope.bufferRole = {};
	 	$scope.page = 10;
	    $scope.bufferRole.selected = [];
	    $scope.bufferRole.rowCollection = [];
	    $scope.bufferRole.data = [];
	    $scope.readDate = true;
		$scope.viewStatu =$stateParams.viewStatu;
		init();
		
		function init(){
	    	userDetail(); 
	    	$scope.falg=true;
 		};
	    
    	$scope.open1 = function () {
             $scope.popup1.opened = true;
         };
         $scope.popup1 = {
             opened: false
         };
        
         $scope.dateOptions = {
             dateDisabled: false,
             formatYear: 'yy',
             format:'yyyy/MM/dd',
//             maxDate: new Date(2020, 5, 22),
             //minDate: new Date(),
             startingDay: 1
         };
         
         var date = dateUtilsService.getStr2Date("09/12/2017", "dd/MM/yyyy");
         console.log(date);
         
         
         /*var count = 0;
         $scope.$watch('userInfoVo.userActiveDate',function(newValue,oldValue, scope){
        	 var date = $filter('date')(newValue, "yyyy/MM/dd");
        	 var newTime = $filter('date')(newValue, "hh:mm:ss");
        	 var time = $filter('date')(new Date(), 'hh:mm:ss');
        	 console.log(newTime);
        	 var dateTime = new Date(date + ' '+ time);
        	 if("12:00:00" == newTime && count > 1) {
        		 console.log(dateTime);
	        	 $scope.userInfoVo.userActiveDate = dateTime;
        	 }
        	 console.log(count);
        	 count ++;
         });*/
         
         function userDetail(){
			$http({method:'GET',params:{ userId:$stateParams.userId},  
				url:"./userManager/userDetail"}).
				success(function(data) {
					$scope.userInfoVo = data;   
					$scope.getPlantModels();						
					if($scope.userInfoVo.plantCode!=null){
						$scope.plantcode=$scope.userInfoVo.plantCode.split(",");						
					}
					if($scope.userInfoVo.prodLn!=null){
						$scope.prodln=$scope.userInfoVo.prodLn.split(",");						
					}
					$scope.bufferRole.rowCollection = $scope.userInfoVo.subRoleModelList;
					$scope.bufferRole.data = [].concat($scope.bufferRole.rowCollection);
					$scope.bufferRole.selected = [];
					 
				}); 			
		};
		
		$scope.getPlantModels=function(){
			$http.post('./userManager/getPlantModels').
			success(function(data) { 								
					$scope.PlantList=data; 
					for(var j=0;j<$scope.plantcode.length;j++){
						for(var i=0;i<$scope.PlantList.length;i++){				
							if($scope.PlantList[i].plantCode==$scope.plantcode[j]){	
								$scope.brandList.push($scope.PlantList[i]);
								$scope.PlantList.splice(i,1);
							}			
						}
					}
					$scope.setProdLn();
			});		
		};
         
         
		
    	//save user
     	$scope.save= function() { 
     		if($scope.userInfoVo == ''){
     			$scope.userInfoVo ={};
     			$scope.userInfoVo.id= "";
     		}
     	  $scope.isSaveCk=true;
     	  $scope.userInfoVo.subRoleModelList = $scope.bufferRole.rowCollection;
     	  $scope.userInfoVo.plantList=$scope.brandList;
     	  $scope.userInfoVo.prodLnList=$scope.prodList;
          $http.post('./userManager/saveUser', $scope.userInfoVo).
              success(function(data) {
                   if (data['errorType'] == "success") {
                	 // console.log(data.returnData);
//                  	 $scope.userInfoVo = data.returnData;
//                     alertService.cleanAlert();
//                 	 if(id == null){
//                         alertService.add(data["errorType"], "Saved successfully.");
//                         $scope.userInfoVo.userPwd = null;
//                 	 }else{
//                  	     alertService.add(data["errorType"], "Saved successfully.");
//                  	     $scope.userInfoVo.userPwd = null;
//                 	 }
                	   alertService.saveSuccess();
                	   $scope.cancel();
                   }else if(data['errorType']!=null){
                 	  
                     alertService.add(data["errorType"], data["errorMessage"]);
                  }     
  	        });
       };
     //lijun stare
        $scope.setProdLn=function() {     	  
    	   $http.post('./userManager/pordLnlist', $scope.brandList).
           success(function(data) {
        	   $scope.prodList=[];
        	   $scope.ProdLnList = data.returnDataList;
        	 
        	   if($scope.falg){
        		 for(var j=0;j<$scope.prodln.length;j++){
        			   for(var i=0;i<$scope.ProdLnList.length;i++){				
        				   if($scope.ProdLnList[i].prodLn==$scope.prodln[j]){	
        					   $scope.prodList.push($scope.ProdLnList[i]);
        					   $scope.ProdLnList.splice(i,1);
        				   }			
        			   }
        		   }
        		   $scope.falg=false;  
        	   }
           });
       };
    	   
    	$scope.setprod = function(){
    		 $http.post('./userManager/pordLnlist', $scope.brandList).
             success(function(data) {
            	$scope.productline = data.returnDataList;
            	for(var j=0;j<$scope.prodList.length;j++){
            		for(var i=0;i<$scope.productline.length;i++){
            			 if($scope.productline[i].prodLn==$scope.prodList[j].prodLn){
            				 $scope.productline.splice(i,1);
            			 }
            		 }
            	 }
            	$scope.ProdLnList = $scope.productline;
            	
             });
    	};
    	
		$scope.appendTo = function() {	
			if($scope.PlantList.length>0){
				for(var i=0;i<$scope.PlantList.length;i++){				
					if($scope.PlantList[i].plantCode==$scope.brandSelect[0].plantCode){				
						$scope.PlantList.splice(i,1);
					}			
				}
				$scope.brandList.push($scope.brandSelect[0]);
				$scope.setprod();				
			}
		};
		
		$scope.appendAll = function() {	
			Array.prototype.push.apply($scope.brandList, $scope.PlantList);
			$scope.PlantList.splice(0,$scope.PlantList.length);
			$scope.setprod();
		};
		
		$scope.appendFrom = function(){
			if($scope.brandList.length>0){
				if($scope.plant[0].plantCode!=null){
					for(var i=0;i<$scope.brandList.length;i++){	
						if($scope.brandList[i].plantCode==$scope.plant[0].plantCode){				
							$scope.brandList.splice(i,1);
						}
					}
					$scope.PlantList.push($scope.plant[0]);						
				}
				$scope.setProdLn();
			}
		};
		
		$scope.appendFromAll = function() {	
			Array.prototype.push.apply( $scope.PlantList,$scope.brandList);
			$scope.brandList.splice(0,$scope.brandList.length);
			$scope.setProdLn();
			
		};
		
		
		$scope.prodTo = function() {
			if($scope.ProdLnList.length>0){
				for(var i=0;i<$scope.ProdLnList.length;i++){				
					if($scope.ProdLnList[i].prodLn==$scope.prodSelect[0].prodLn){				
						$scope.ProdLnList.splice(i,1);
					}			
				}
				$scope.prodList.push($scope.prodSelect[0]);				
			}
		};
		
		$scope.prodAll = function() {	
			Array.prototype.push.apply($scope.prodList, $scope.ProdLnList);
			$scope.ProdLnList.splice(0,$scope.ProdLnList.length);
		};
		
		$scope.prodFrom = function(){
			if($scope.prodList.length>0){
				for(var i=0;i<$scope.prodList.length;i++){	
					if($scope.prodList[i].prodLn==$scope.prod[0].prodLn){				
						$scope.prodList.splice(i,1);
					}
				}
				$scope.ProdLnList.push($scope.prod[0]);				
			}
		};
		
		$scope.prodFromAll = function() {	
			Array.prototype.push.apply( $scope.ProdLnList,$scope.prodList);
			$scope.prodList.splice(0,$scope.prodList.length);
		};
     //end  
	 $scope.cancel =  function() {
		 $http.post('./userManager/cancel')
   		.success(function (data) {
   			$scope.userInfo=data;
   			$state.go('main.userManager',{'userInfo':$scope.userInfo});
   		});
	 };
	 
	 $scope.setdate=function(){
   	   $scope.userInfoVo.userActiveDate="";
	 };
	 
    $scope.bufferRole.selectAll = function (collection) {
         if ($scope.bufferRole.selected.length === 0) {
             angular.forEach(collection, function (val) {
                 $scope.bufferRole.selected.push(val);

             });
         } else if ($scope.bufferRole.selected.length > 0 && $scope.bufferRole.selected.length != $scope.bufferRole.data.length) {
             angular.forEach(collection, function (val) {
                 var found = $scope.bufferRole.selected.indexOf(val);
                 if (found == -1) $scope.bufferRole.selected.push(val);
             });
         } else {
             $scope.bufferRole.selected = [];
         }
     };
     
     $scope.bufferRole.select = function (id) {
         var found = $scope.bufferRole.selected.indexOf(id);
         if (found == -1) $scope.bufferRole.selected.push(id);
         else $scope.bufferRole.selected.splice(found, 1);
     };

     $scope.roleManagerCodeList = [];
     $scope.roleModel = {};
	 $http.post('./roleManager/roleList',  $scope.roleModel).success(
		 function(data) {
			 $scope.roleManagerCodeList = data;
		 }
	 );
	 
	  $scope.addUserRole = function() {
	   	  $scope.userInfoVo.bufferRoleModel = {};
	   	  $scope.userInfoVo.selectedRoleModel = {};
	   	  $scope.userInfoVo.selectedRoleModel.roleActive = "Y";
	   	  $scope.userInfoVo.subRoleModelList = $scope.bufferRole.rowCollection;
	   	  $scope.userRoleDialog();
      };
      
     $scope.editUserRole = function(){
     	 $scope.userInfoVo.selectedRoleModel = {};
     	 $scope.userInfoVo.bufferRoleModel = {};
     	 $scope.userInfoVo.subRoleModelList = $scope.bufferRole.rowCollection;
    		 if($scope.bufferRole.selected.length != 1){
        		 
        		 alertService.add('danger', 'Please select one record.');
    			 return;
          }
		 $scope.userInfoVo.bufferRoleModel = angular.copy($scope.bufferRole.selected[0]);
		 $scope.userInfoVo.selectedRoleModel = angular.copy($scope.bufferRole.selected[0]);
		 $scope.userRoleDialog();
     };
     
     $scope.editUserRole2 = function(row){
     	 $scope.userInfoVo.selectedRoleModel = {};
     	 $scope.userInfoVo.bufferRoleModel = {};
     	 $scope.userInfoVo.subRoleModelList = $scope.bufferRole.rowCollection;
    		
		 $scope.userInfoVo.bufferRoleModel = angular.copy(row);
		 $scope.userInfoVo.selectedRoleModel = angular.copy(row);
		 $scope.userRoleDialog();
     };
     

 	$scope.deleteUserRole = function(isDelete){
 		 $scope.userInfoVo.subRoleModelList = $scope.bufferRole.rowCollection;
 		 if( $scope.bufferRole.selected.length < 1){
     		 
     		 alertService.add('danger', 'Please select one record.');
 			 return;
          }
 		 if (!isDelete) {
		    $('#confirmModal').modal({keyboard: false});
		    return;
		 }
 		 var roleCodes = "";
         for (var i = 0; i < $scope.bufferRole.selected.length; i++) {
        	 roleCodes = roleCodes + $scope.bufferRole.selected[i]["roleCode"] + ",";
         }
         $scope.userInfoVo.bufferRoleCodes = roleCodes;
         console.log($scope.userInfoVo.subRoleModelList);
      	 $http.post('./userManager/deleteRolePop', $scope.userInfoVo)
      		.success(function (data) {
 	              if (data != null && data != '') {
 	                  if (data['errorType'] != "success") {
 	                      
 	                      alertService.add(data["errorType"], data["errorMessage"]);
 	                  } else {
 	                	  $scope.bufferRole.rowCollection = data.returnData.subRoleModelList;
  				          $scope.bufferRole.data = [].concat($scope.bufferRole.rowCollection);
  		                  $scope.bufferRole.selected = [];
 	                      
// 	                      alertService.add("success", "Deleted successfully.");
 	                  }
 	              }   
 	          });
 	};
 	
 	$scope.userRoleAuditTrail = function(){
		 if($scope.bufferRole.selected.length != 1){
    		 
    		 alertService.add('danger', 'Please select one record.');
			 return;
         }
		 $scope.auditTrailData = $scope.bufferRole.selected[0];
		 console.log( $scope.auditTrailData);
		 ngDialog.open({
	  		    className: 'ngdialog-theme-default custom-width-800',
	  		    template: 'sys/layout/auditTrailDialog.html',
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
     
     
     
 	$scope.userRoleDialog = function (){
  		alertService.cleanAlert();
  		ngDialog.open({
  		    className: 'ngdialog-theme-default custom-width-800',
  		    template: 'sys/pages/userRoleDialog.html',
  		    scope: $scope,
  		    overlay: false,
  		    controller: ['$scope', '$http', 'alertService', '$stateParams','ngDialog',
						function($scope, $http, alertService,  $stateParams, ngDialog) {
		    	$scope.saveRolePop = function (){
					alertService.cleanAlert();
					$http.post('./userManager/saveAddRole', $scope.userInfoVo)
	      				.success(function(data) {
							if (data != null && data != '') {
								if (data['errorType'] != "success") {
									alertService.add(data["errorType"],data["errorMessage"]);
								} else {
									$scope.bufferRole.rowCollection = data.returnData.subRoleModelList;
									$scope.bufferRole.data = [].concat($scope.bufferRole.rowCollection);
									$scope.bufferRole.selected = [];
								    $scope.closeThisDialog(0);
						            ngDialog.closePromise;
//						            alertService.add(data["errorType"],"Saved successfully.");
								}
							}
						});
				};
				console.log($scope.$parent);
				$scope.cancelRolePop = function (){
					alertService.cleanAlert();
					$scope.closeThisDialog(0);
				};
  		    }],
  		    onOpenCallback: function() {
  		    	var scope = $scope.$$childTail;
  		    	console.log(scope);
			}, 
  		    preCloseCallback: function(value) {
  		    	alertService.cleanAlert();
  		    },
  		});
		return;
	};
	
	 $scope.openPop = function () {
         $scope.popupPop.opened = true;
     }; 
     $scope.popupPop = {
         opened: false
     };
     $scope.dateOptionsPop = {
         dateDisabled: false,
         formatYear: 'yy',
         startingDay: 1
     };
     
     $scope.setPopActiveDate = function(){
    	 $scope.userInfoVo.selectedRoleModel.inactiveDate = "";
	 };
	 
	 $scope.roleValueChange = function(){
		if ($scope.roleManagerCodeList != null && $scope.roleManagerCodeList != '') {
             for(var i=0; i< $scope.roleManagerCodeList.length; i++) {
            	 if($scope.userInfoVo.selectedRoleModel.roleCode  ==  $scope.roleManagerCodeList[i].roleCode) {
            		 $scope.selectedRole = $scope.roleManagerCodeList[i];
            		 console.log("---------------------value-change---------------------");
            		 console.log( $scope.selectedRole);
            		 $scope.userInfoVo.selectedRoleModel.roleName = $scope.selectedRole.roleName;
            		 $scope.userInfoVo.selectedRoleModel.roleId =  $scope.selectedRole.id;
            		 break;
            	 }
             }
		}
  	};
 	
}]);