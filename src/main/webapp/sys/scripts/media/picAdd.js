'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:menuAddCtrl
 * @description # menuAddCtrl Controller of the psspAdminApp
 */
angular
		.module('psspAdminApp')
		.controller(
				'picAddCtrl',
				[
						'$scope',
						'$state',
						'$http',
						'$interval',
						'alertService',
						'localStorageService',
						'$stateParams',
						function($scope, $state, $http, $interval,
								alertService, localStorageService, $stateParams) {
							$scope.state=false;
							$scope.picModel = {};
							$scope.isSaveCk = false;
							
							$http.post('./picture/get', {
								'id' : $stateParams.id,
							}).success(function(data) {
								$scope.state = $stateParams.state;
								$scope.picModel = data.returnData;
								console.log($scope.picModel);
								alertService.cleanAlert();
							});
							
							$scope.save = function() {
								$scope.isSaveCk = true;
								$http.post('./picture/save',
												$scope.picModel)
										.success(
												function(data) {
													if (data['errorType'] == "success") {
														alertService.saveSuccess();
										        		$scope.cancel();
													} else if(data['errorType']!=null){
														
														alertService
																.add(
																		data["errorType"],
																		data["errorMessage"]);
													}
												});
							};

							$scope.cancel = function() {
								 $state.go('main.pic_manager');
								
							};
							
							$scope.autoCheck = function(menuModel, key){
								menuModel[key] = menuModel[key].replace(/\D/g,'');
						       	};
						       	
						} ]);
