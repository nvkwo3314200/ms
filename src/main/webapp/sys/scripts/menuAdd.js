'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:menuAddCtrl
 * @description # menuAddCtrl Controller of the psspAdminApp
 */
angular
		.module('psspAdminApp')
		.controller(
				'menuAddCtrl',
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
							$scope.menuModel = {};
							$scope.isSaveCk = false;
							$http.post('./menuManager/selectMenu', {
								'id' : $stateParams.id,
							}).success(function(data) {
								$scope.state = $stateParams.state;
								$scope.menuModel = data;
								$scope.selectMenuList();
								alertService.cleanAlert();
							});

							$scope.save = function() {
								$scope.isSaveCk = true;
								$http.post('./menuManager/save',
												$scope.menuModel)
										.success(
												function(data) {
													if (data['errorType'] == "success") {
//														$scope.menuModel = data.returnData;
//														if (id == null) {
//															alertService
//																	.add(
//																			data["errorType"],
//																			"Saved successfully. ");
//														} else {
//															alertService
//																	.add(
//																			data["errorType"],
//																			"Saved successfully. ");
//														}
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
								
								 $state.go('main.menu_manager');
								
							};

							$scope.menuchange = function() {
								$scope.menuModel.lev2 = "";
								$scope.menuModel.uiSref = "";

							};
							
							$scope.autoCheck = function(menuModel, key){
								menuModel[key] = menuModel[key].replace(/\D/g,'');
						       	};
						       	
							$scope.selectMenuList = function() {
								$http.post('./menuManager/selectMenulist', {
									'type' : 'menu'
								}).success(function(data) {
									$scope.data = data;

									alertService.cleanAlert();
								});
							};
						} ]);
