'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:menuAddCtrl
 * @description # menuAddCtrl Controller of the psspAdminApp
 */
angular
		.module('psspAdminApp')
		.controller(
				'picUploadCtrl',
				[
						'$scope',
						'$state',
						'$http',
						'$interval',
						'alertService',
						'FileUploader',
						'localStorageService',
						'$stateParams',
						function($scope, $state, $http, $interval,
								alertService, FileUploader, localStorageService, $stateParams) {
							
							$scope.uploadStatus = false; //定义两个上传后返回的状态，成功获失败
							$scope.failureFileItems = [];
						    var uploader = $scope.uploader = new FileUploader({
						        url: 'picture/upload',
						        //queueLimit: 1,     //文件个数 
						        removeAfterUpload: true   //上传后删除文件
						    });
						    $scope.clearItems = function(){    //重新选择文件时，清空队列，达到覆盖文件的效果
						    	$scope.fileItems = [];
						        uploader.clearQueue();
						    }
						    $scope.removeItem = function(fileItem) {
						    	uploader.removeFromQueue(fileItem);
						    	var index = $scope.fileItems.indexOf(fileItem);
						    	$scope.fileItems.splice(index, 1);
						    }
						    uploader.onAfterAddingFile = function(fileItem) {
						        $scope.fileItems.push(fileItem);    //添加文件之后，把文件信息赋给scope
						    };
						    uploader.onSuccessItem = function(fileItem, response, status, headers) {
						        
						    };
						    
						    uploader.onErrorItem = function(item, response, status, headers) {
						    	alertService.add("danger","上传失败.");
						    	$scope.clearItems();
						    	$scope.failureFileItems.push(item);
						    }
						    
						    uploader.onCompleteAll   = function() {
						    	$scope.uploadStatus = true;   //上传成功则把状态改为true
						        alertService.add("success","上传成功.");
						        $state.go('main.picManage');
						    }
						    
						    /**
						    var uploader1 = $scope.uploader1 = new FileUploader({
						        url: 'picture/upload',
						        //queueLimit: 1,
						        removeAfterUpload: true   
						    });
						    $scope.clearItems1 = function(){
						        uploader1.clearQueue();
						    }
						    uploader1.onAfterAddingFile = function(fileItem) {
						        $scope.fileItem1 = fileItem._file;    //添加文件之后，把文件信息赋给scope
						        //能够在这里判断添加的文件名后缀和文件大小是否满足需求。
						    };
						    uploader1.onSuccessItem = function(fileItem,response, status, headers){
						        $scope.uploadStatus1 = true;
						    }
						    */
						    
						    $scope.UploadFile = function(){
						        uploader.uploadAll();
						    }
							
						    $scope.cancel = function () {
						    	$state.go('main.picManage');
						    }
						    
						} ]);
