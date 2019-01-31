'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:ProductViewCtrl
 * @description
 * # ProductViewCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
    .controller('SystemParaCtrl', ['$scope', '$state', '$http', '$interval', 'alertService','localStorageService','$stateParams',
        function ($scope, $state, $http, $interval, alertService,localStorageService,$stateParams) {
    	$scope.userVo = {};
    	$scope.ckeckFlag = false;
    	$scope.state = false;
    	$scope.isSaveCk = false;
    	
    	if($stateParams.id != null){
			$http({method:'GET', params:{id:$stateParams.id},
	        	url:"./systemPara/sysDetail"
			}).success(function(data) {
				$scope.state = $stateParams.state;
				$scope.userVo = data;
				
			});
    	}else{
    		$scope.userVo.activeInd = 'Y';
    	}

     	$scope.save= function() {
        	$scope.ckeckFlag = true;
			if($scope.userVo == ''){
				$scope.userVo = {};
				$scope.userVo.id = "";
			}
			$scope.flag = false;
			if($scope.userVo.dispSeq == null){
				$scope.flag = ture;
			}
			
          $http.post('./systemPara/saveUser', $scope.userVo).success(function(data) {
        	  if (data['errorType'] == "success") {
        		  $scope.isSaveCk = true;
//        		  $scope.userVo = data.returnData;
//        		  alertService.cleanAlert();
//        		  if(id == null){
//	        		alertService.add(data["errorType"], "Saved successfully.");
//	              } else {
//	              	alertService.add(data["errorType"], "Saved successfully.");
//	              }
        		  alertService.saveSuccess();
        		  $scope.cancel();
        	  }else if(data['errorType']!=null){
                 
                 alertService.add(data["errorType"], data["errorMessage"]);
              }  
        	  
  	      });
        };
	
	 $scope.cancel =  function() {
		 $state.go('main.menu_system_parameters');
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
//         maxDate: new Date(2020, 5, 22),
         //minDate: new Date(),
         startingDay: 1
     };
     
     $scope.setdate=function(){
  	 	$scope.userVo.inactiveDate="";
	 };
	 
	 $scope.autoCheck = function(model, key){
		model[key] = model[key].replace(/[^\d.]/g,"");
		model[key] = model[key].replace(/^\./g,"");
		model[key] = model[key].replace(/\.{2,}/g,".");
		model[key] = model[key].replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	 };
     
//     $scope.checkValue(obj,index){
//    	 var state = obj.value == ''?'block':'none';
//    	 document.getElementById('errMsg'+index).style.display = state;
//    	 return state == 'none'?true:false;
//     };
	 
//	 $scope.checkValue = (value){
//		 alert("xp");
//    	 if(value == ''){
//    		 return true;
//    	 }else{
//    		 return false;
//    	 }
//     };
	 
}]);
