'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:ProductViewCtrl
 * @description # ProductViewCtrl Controller of the psspAdminApp
 */
angular
		.module('psspAdminApp')
		.controller(
				'RoleManagerAddCtrl',
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
							$scope.busUnitList = null;
							$scope.isSaveCk = false;
							$scope.state = false;
							$scope.rowCollection = [];
							$scope.page = 10;
							$scope.roleVo = {};

							$http({
								method : 'GET',
								params : {
									roleId : $stateParams.roleId
								},
								url : "./roleManager/roleDetail"
							}).success(function(data) {
								$scope.state = $stateParams.state;
								$scope.roleVo = data;
								$scope.rowCollection =$scope.roleVo.list;
								$scope.data=[].concat(data.list);
								
							});

							$http
									.post('./busUnit/search', {})
									.success(
											function(data) {
												$scope.busUnitList = data.returnDataList;
											});

							$scope.open1 = function() {
								$scope.popup1.opened = true;
							};
							$scope.popup1 = {
								opened : false
							};
							$scope.dateOptions = {
								dateDisabled : false,
								formatYear : 'yy',
								// maxDate: new Date(2020, 5, 22),
								// minDate: new Date(),
								startingDay : 1
							};

							// save user
							$scope.save = function() {
								$scope.isSaveCk = true;
								if ($scope.roleVo == '') {
									$scope.roleVo = {};
									$scope.roleVo.id = "";

								}
								if ($scope.roleVo.roleCode != null
										&& $scope.roleVo.roleName != null
										&& $scope.roleVo.roleOrder != null) {
									$http
											.post('./roleManager/saveRole',
													$scope.roleVo)
											.success(
													function(data) {

														if (data['errorType'] == "success") {
															// console.log(data.returnDataList);
//															$scope.roleVo = data.returnData;
//															alertService
//																	.cleanAlert();
															
//															if (id == null) {
//																alertService
//																		.add(
//																				data["errorType"],
//																				"Saved successfully.");
//															} else {
//																alertService
//																		.add(
//																				data["errorType"],
//																				"Saved successfully.");
//															}
															alertService.saveSuccess();
											        		$scope.cancel();
														} else if (data['errorType'] != null) {
															
															alertService
																	.add(
																			data["errorType"],
																			data["errorMessage"]);
														}
													});
								}
								;
							};

							$scope.autoCheck = function(roleVo, key) {
								roleVo[key] = roleVo[key].replace(/\D/g, '');
							};

							$scope.setdate = function() {
								$scope.roleVo.inactiveDate = "";
							};

							$scope.cancel = function() {
								$state.go('main.roleManager');
							};

						} ]);
