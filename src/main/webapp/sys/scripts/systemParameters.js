'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:ProductViewCtrl
 * @description
 * # ProductViewCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
    .controller('systemParametersCtrl',
        function ($scope, $state, $http, $interval, $controller, alertService, localStorageService, $stateParams, ngDialog, $location, FileUploader, cacheService, uuidService) {
    	$controller('BaseCtrl',{$scope:$scope});
    	
    	$scope.tableData = [];
    	$scope.segmentList = [];
		$scope.selected = [];
		$scope.isSaveCk = false;
		$scope.isChoice = false;
		$scope.paramPage = {};
		$scope.searchModel = cacheService.getCache();
		cacheService.setCache($scope.searchModel); 	
		$scope.pageModel = $scope.searchModel;
		$scope.progress = [];
		$scope.progressFlag = false;
		
		// -lh-10-26
		$scope.uploadProgress = {};
		$scope.uploadProgressFlag = false;
			
        $scope.init = function(){
 			$scope.getPowers();
 			$scope.searchSegment();
 			initUploader();
 			alertService.showSuccessMsg();
 		};
 		
        $scope.search =function(searchModel){
        	$http.post('./systemPara/getSelectList', searchModel).success(
    			function(data) {
    				$scope.selected = [];
    				$scope.tableData = data.page.list;
    				$scope.paramPage = data.page;
    			}
        	);
        };
        
        $scope.searchSegment = function(){
	        $http({method: 'GET',params: $scope.searchModel,
		        url:"./systemPara/segmentList"
		        }).success(function(data) {
	            	$scope.segmentList = [].concat(data);
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
         
         $scope.selectDouble = function(row) {
        	 $state.go('main.system_para_add',{'id': row.id});
         };
            
        $scope.addUser = function(){
            $state.go('main.system_para_add');
        };
    	
    	$scope.edit = function(){
    		if($scope.selected.length != 1){
        		
        		alertService.add('danger', 'Please select one record.');
    			return;
        	}
        	var id = $scope.selected[0]["id"];
        	$state.go('main.system_para_add',{'id': id});
    	};
    	
    	$scope.view = function(){
    		if($scope.selected.length != 1){
        		
        		alertService.add('danger', 'Please select one record.');
    			return;
        	}
        	var id = $scope.selected[0]["id"];
        	$state.go('main.system_para_add',{'id': id, 'state':true});
    	};
        
    	$scope.deletes = function(isDelete){
    		 if( $scope.selected.length < 1){
        		 
        		 alertService.add('danger', 'Please select one record.');
    			 return;
             }
    		 if (!isDelete) {
    			 $('#confirmModal').modal({keyboard: false});
    			 return;
    		 }
	         $http.post('./systemPara/delete',$scope.selected).success(function (data) {
                  if (data['errorType'] == "success") {
                	$scope.selected = [];
                	$scope.searchModel.segment=undefined;
                	$scope.search($scope.searchModel);
			        $scope.searchSegment();
					alertService.add(data["errorType"],"Deleted successfully.");
                  } else {
                	
					alertService.add(data["errorType"],data["errorMessage"]);
                  }
	         });
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
       
        $scope.searchUser = function() {
        	$http.post('./systemPara/getSelectList', $scope.searchModel).success(
            	function(data) {
            		if (data['errorType'] == "success") {
    			  		$scope.rowCollection = data.returnDataList;
    		 	 		$scope.data = data.returnDataList;
    	                $scope.selected = [];
    	                $scope.searchModel.fileUrl ="";
    		            alertService.cleanAlert();
            		}
            	}
            );
        };
       
        $scope.reset = function(){
        	$scope.searchModel.segment =undefined;
        	cacheService.clearCache();
    		alertService.cleanAlert();
		};
		
		$scope.templatedownload = function() {
            $http({
                url: './systemPara/templatedownload',
                method: "POST",
                data: $.param({}),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/vnd.ms-excel"});
                saveAs(blob, [headers('Content-Disposition').replace(/attachment;fileName=/,"")]);
                alertService.add('success', 'Export successfully');
            }).error(function (data, status, headers, config) {
            	alertService.add('danger', 'Export fail');
            });
        };
		
        function initUploader() {
        	var threadId = uuidService.uuid();
     		var uploader = $scope.uploader = new FileUploader({
    	         url: './systemPara/upload',
    	         formData: [{"threadId":threadId}],
    	         /*s: 1,*/
    	         removeAfterUpload: false 
    	    });
     		
     		uploader.filters.push({
                name: 'fileTypeFilter',
                fn: function(item) {
                	$scope.isChoice = false;
                	var isSuccessed = true;
    		    	var fileType = item.name.toLowerCase().split('.');
    		        var type = fileType[fileType.length-1];
    		        if(type!='xls' && type!='xlsx'){
    		        	$scope.isSaveCk = true;
    		        	isSuccessed = false;
    		        	uploader.clearQueue();
    		        }else{
    		        	$scope.isSaveCk = false;
    		        }
                	return isSuccessed;
                }
            });
     		
    	    uploader.onWhenAddingFileFailed = function(item , filter, options) {};
    	         
    	    uploader.onAfterAddingFile = function (fileItem) {
    	    	var len = uploader.queue.length;
    	    	if(len > 1) {
    	    		uploader.queue[0] = uploader.queue[1];
    	    		uploader.queue.splice(1, 1);
    	    	}
    	    };
    	    
    	    uploader.resetFile = function(fileItem) {
    	    	fileItem.isReady = false;
    	    	fileItem.isUploading = false;
    	    	fileItem.isUploaded = false;
    	    	fileItem.isSuccess = false;
    	    	fileItem.isCancel = false;
    	    	fileItem.isError = false;
    	    }
    	    
    	    uploader.onCompleteItem = function (fileItem, response, status, headers) {
    	    	uploader.resetFile(fileItem);
    	    	response = JSON.parse(decodeURIComponent(response));
    	    	if(response.error) {
    	    		response.error = response.error.replace(/\+/g, ' ');
    	    	}
    	    	if (status == 200) {
    	    		if(response.error==null){
    					alertService.add('success', 'Import successfully');
    				}else{ 
    					alertService.add('danger', response.error);
    				}
    	    		$scope.searchSegment();
    	    	 } else {
    	    	    
    	 	   	    alertService.add('danger', 'Import failed');
    	    	 }
    	    }; 
    	    	   
    	    $scope.clearuploader = function() {
    	    	  uploader.clearQueue();
    	    	  uploader.destroy();
    	    };
    	    
    	    $scope.upload = function() {
    	    	if(uploader.queue.length < 1){
    	    	    $scope.isChoice = true;
    	    	}else{
    	    		$scope.isChoice = false;
    	    	    uploader.uploadAll();
    	    	}
    	    	
    	    	// -lh-10-26
    	    	if((!$scope.isChoice) && (!$scope.isSaveCk) && (!$scope.uploadProgressFlag)){
    	    		$scope.uploadProgressFlag = true;
    		    	initUploadProgress();
    		    	var start = $interval(function(){
    		    		if(!$scope.uploadProgressFlag){
    		    			$interval.cancel(start);
    		    			$http.post('./systemPara/cancelProgress', threadId);
    		    			$scope.uploadProgress.redProgress = 0;
    		    			$scope.uploadProgress.insertProgress = 0;
    		    			$scope.uploadProgress.statusValue = '遵入已取消';
    		    			$scope.uploadProgress.statusSuffix = '！';
    		    		}else if($scope.uploadProgress.value<0.05){
    		    			// $http.post('./systemPara/redProgress', threadId).success(
    		    			$http({
    		    				method : 'POST',
    		    				url : './systemPara/redProgress',
    		    				data: $.param({
    		    					threadId:threadId
    		    	             }),
    		    	             headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    		    			}).success(
    	        				function(data) {
    	        					$scope.uploadProgress.statusValue = '正在處理';
    	        					$scope.uploadProgress.redProgress = data*0.05;
    	        				}
    	        			);
    		    		}else if($scope.uploadProgress.value<1){
    		    			//$http.post('./systemPara/insertProgress').success(
    		    			$http({
    		    				method : 'POST',
    		    				url : './systemPara/insertProgress',
    		    				data: $.param({
    		    					threadId:threadId
    		    	             }),
    		    	             headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    		    			}).success(
    	        				function(data) {
    	        					$scope.uploadProgress.statusValue = '正在處理';
    	        					$scope.uploadProgress.insertProgress = data*0.95;
    	        				}
    	        			);
    		    		}else{
    			    		$interval.cancel(start);
//    			    		$http.post('./systemPara/initProgress', threadId);
    			    		$http({
    		    				method : 'POST',
    		    				url : './systemPara/initProgress',
    		    				data: $.param({
    		    					threadId:threadId
    		    	             }),
    		    	             headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    		    				
    		    			});
    		    			$scope.uploadProgress.statusValue = '遵入完成';
    		    			$scope.uploadProgress.statusSuffix = '！';
    		    			$scope.uploadProgressFlag = false;
    		    		}
    		    		if($scope.uploadProgress.statusSuffix != '！' && $scope.uploadProgress.statusValue != ''){
    			    		$scope.uploadProgress.statusSuffix = $scope.uploadProgress.statusSuffix + '.';
    		    		}
    		    		if($scope.uploadProgress.statusSuffix.length==4){
    		    			$scope.uploadProgress.statusSuffix = '.';
    		    		}
    		    		$scope.uploadProgress.value = $scope.uploadProgress.redProgress + $scope.uploadProgress.insertProgress;
    		    		$scope.uploadProgress.status = $scope.uploadProgress.statusValue + $scope.uploadProgress.statusSuffix;
    		    	}, 250);
    	    	}else{
        			$scope.uploadProgressFlag = false;
    	    	};
    	  };
    	  // -lh-10-26
    	  function initUploadProgress(){
    		  $scope.uploadProgress.value = 0;
    		  $scope.uploadProgress.redProgress = 0;
    		  $scope.uploadProgress.insertProgress = 0;
    		  $scope.uploadProgress.status = '';
    		  $scope.uploadProgress.statusValue = '';
    		  $scope.uploadProgress.statusSuffix = '';
    	  }
     }
	   	    
	   	 $scope.download = function(flag) {
	   		 var paramData = [];
	   		 if($scope.selected.length >= 1) {
	   			 paramData = $scope.selected;
	   		 }
	         $http({
	             url: './systemPara/download',
	             method: "POST",
	             data: $.param({
	            	segment:$scope.searchModel.segment,
	            	data:JSON.stringify(paramData)
	             }),
	             headers: {'Content-Type': 'application/x-www-form-urlencoded'},
	             responseType: 'arraybuffer'
	         }).success(function (data, status, headers, config) {
	             var blob = new Blob([data], {type: "application/vnd.ms-excel"});
	             saveAs(blob, [headers('Content-Disposition').replace(/attachment;fileName=/,"")]);
	             alertService.add('success', 'Export successfully');
	         }).error(function (data, status, headers, config) {
	        	 alertService.add('danger', 'Export fail');
	         });
	     };
	     
	     $scope.clear = function(){
	 		$scope.progress = [];
	 		alertService.cleanAlert();
	 	};
    })
.filter("roundPer",function(){
    return function(number){
    	number = Math.round((number=='Infinity'?0:number)*100)||0;
        return (number == 0) ? '' : number + '%';
    };
});
