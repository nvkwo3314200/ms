'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:MenuManagerCtrl
 * @description # MenuManagerCtrl Controller of the psspAdminApp
 */
angular.module('psspAdminApp').controller('PicManageCtrl',
	function($scope, $http,$stateParams,cacheService, $state, $controller, localStorageService, alertService, $location
			, ngDialog, $timeout) {
		$controller('BaseCtrl', {$scope:$scope});
		$scope.tableData = [];
		$scope.selected = [];
		$scope.picPage = {};
		$scope.floatImage = {};
		$scope.searchmodel = cacheService.getCache();
		cacheService.setCache($scope.searchmodel);
		$scope.pageModel = $scope.searchmodel;
		
		$scope.init=function(){
			$scope.getPowers();
			alertService.showSuccessMsg();
		};

			
		 $scope.getMenuName=function(){
			 $http.post('./menuManager/getMenuName').success(
					 function(data) {
						 if (data['errorType'] == "success") {	
							 $scope.menuNames = data.returnDataList;
						} else {
							 
							 alertService.add(data["errorType"],
									 data["errorMessage"]);
						 }
					 }
			 );
			 
		 };
		
		$scope.select1 = function(pic) {
			$state.go('main.pic_add', {'id' : pic.id});
		};
		
		$scope.create = function() {
			$state.go('main.pic_add');
		};
		
				
		$scope.search = function (searchModel){
			$http.post('./picture/search', searchModel).success(
					function(data) {
						if (data['errorType'] == "success") {
							$scope.selected = [];
							$scope.tableData = data.page.list;
							$scope.picPage = data.page; 
						} else if(data['errorType'] !=null )  {
							alertService.add(data["errorType"],
									data["errorMessage"]);
						}
					}
				);
		};
		
		$scope.edit = function() {
			if ($scope.selected.length != 1) {			
				alertService.add('danger', 'Please select one record.');
				return;
			}
			var id = $scope.selected[0]['id'];
			$state.go('main.pic_add', {'id' : id});
		};
		
		$scope.view= function(){
			if ($scope.selected.length != 1) {
				alertService.add('danger', 'Please select one record.');
				return;
			}
			var id = $scope.selected[0]['id'];
			var state=true;
			$state.go('main.pic_add', {'id' : id,'state':state});
		};
			
		$scope.resert= function () {
			$scope.searchmodel.lev1=undefined;
			$scope.searchmodel.lev2=undefined;
			cacheService.clearCache();
		};
		
		$scope.deletes = function(isDelete) {
			if ($scope.selected.length < 1) {
				alertService.add('danger', 'Please select one record.');
				return;
			}
			
			if (!isDelete) {
				    $('#confirmModal').modal({keyboard: false});
				    return;
			}
			
			$http.post('./menuManager/delete', $scope.selected).success(
				function(data) {
					if (data['errorType'] == "success") {
						$scope.selected = [];
						$scope.getMenuName();
						$scope.searchmodel.lev1=undefined;
						$scope.searchmodel.lev2=undefined;
						$http.post('./menuManager/searchMenu', $scope.searchmodel).success(
								function(data) {
									if (data['errorType'] == "success") {
										$scope.tableData = data.returnDataList;
									}
						});
						alertService.add("success","Deleted successfully.");
					}else {
						
						alertService.add(data["errorType"],data["errorMessage"]);
					}
				}
			);
		};
		
		$scope.funcNameChange = function(){	
			$scope.funcNamelist=[];
			if($scope.searchmodel.lev1!=null){
				$http.post('./menuManager/getfuncName', $scope.searchmodel).success(
						function(data) {
							if (data['errorType'] == "success") {	
								$scope.funcNamelist = data.returnDataList;
								alertService.cleanAlert();
							} else if(data['errorType'] != null){
								
								
							}
						}
				);												 				
			}
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
	        
	        // 获取当前dom元素
	        function getCrossBrowserElement(mouseEvent){
	          var result = {
	            x: 0,
	            y: 0,
	            relativeX: 0,
	            relativeY: 0,
	            currentDomId: ""
	          };
	   
	          if (!mouseEvent){
	            mouseEvent = window.event;
	          }
	   
	          if (mouseEvent.pageX || mouseEvent.pageY){
	            result.x = mouseEvent.pageX;
	            result.y = mouseEvent.pageY;
	          }
	          else if (mouseEvent.clientX || mouseEvent.clientY){
	            result.x = mouseEvent.clientX + document.body.scrollLeft +
	              document.documentElement.scrollLeft;
	            result.y = mouseEvent.clientY + document.body.scrollTop +
	              document.documentElement.scrollTop;
	          }
	   
	          result.relativeX = result.x;
	          result.relativeY = result.y;
	   
	          if (mouseEvent.target){
	            var offEl = mouseEvent.target;
	            var offX = 0;
	            var offY = 0;
	            if (typeof(offEl.offsetParent) != "undefined"){
	              while (offEl){
	                offX += offEl.offsetLeft;
	                offY += offEl.offsetTop;
	                offEl = offEl.offsetParent;
	              }
	            }
	            else{
	              offX = offEl.x;
	              offY = offEl.y;
	            }
	   
	            result.relativeX -= offX;
	            result.relativeY -= offY;
	          }
	          result.currentDomId = mouseEvent.target.id;
	   
	          return result;
	        };
	        
	       $scope.showPicture = function(mouseEvent, src) {
	    	   $timeout(function() {
	    		   var element = getCrossBrowserElement(mouseEvent);
		    	   $scope.floatImage.src = mouseEvent.target.getAttribute("look");
		    	   if(!$scope.floatImage.src) {
		    		   $scope.floatImage.src = mouseEvent.target.parentElement.getAttribute("look");
		    	   }
		    	   $scope.floatImage.left = (element.x + 5) + "px";
		    	   $scope.floatImage.top = (element.y + 5) + "px";
		    	   $scope.floatImage.show = true;
	    	   }, 0);
	       }
	       
	       $scope.hidePicture = function(mouseEvent) {
	    	   $timeout(function() {
	    		   $scope.floatImage.show = false;
	    	   }, 0);
	    	   
	    	   console.log("hide");
	       }
	       
	       $scope.movePicture = function(mouseEvent) {
	    	   var element = getCrossBrowserElement(mouseEvent);
	    	   $scope.floatImage.left = (element.x + 5) + "px";
	    	   $scope.floatImage.top = (element.y + 5) + "px";
	       }
	}
);