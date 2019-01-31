'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:RoleManagerCtrl
 * @description
 * # RoleManagerCtrl
 * Controller of the psspAdminApp
 */
angular
		.module('psspAdminApp')
		.controller(
				'UserManagerCtrl',
				function($scope, $state, $stateParams,cacheService, $http, $interval,
						alertService, localStorageService, ngDialog, $location, $controller) {
					$controller('BaseCtrl', {$scope:$scope});
					
					$scope.UserInfoVo = {};
					
					$scope.userName = "";
					// $scope.userVo.activate = "Y";
					$scope.activate = "Y";
					/**
					 * id 
					 */
					$scope.page = 10;
					$scope.selected = [];
					$scope.tableData = [];
					$scope.userPage = {};
					console.log(cacheService);
					$scope.searchUserInfoVo = cacheService.getCache();
					cacheService.setCache($scope.searchUserInfoVo);
					$scope.pageModel = $scope.searchUserInfoVo;
					$scope.init = function() {
						$scope.getPowers();
						alertService.showSuccessMsg();
					};

					$scope.selectAll = function(collection) {
						if ($scope.selected.length === 0) {
							angular.forEach(collection, function(val) {
								$scope.selected.push(val);

							});
						} else if ($scope.selected.length > 0
								&& $scope.selected.length != $scope.tableData.length) {
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

					$scope.search = function() {
						$http({
							method : 'POST',
							data : $scope.pageModel,
							url : "./userManager/search"
						}).success(function(data) {
							$scope.tableData = data.list;
							$scope.userPage = data;
							$scope.selected = [];
						});
					};

					$scope.addUser = function() {
						$state.go('main.userManager_add');
					};

					$scope.editUser = function() {
						if ($scope.selected.length != 1) {
							
							alertService.add('danger',
									'Please select one record.');
							return;
						}
						var userId = $scope.selected[0]["id"];
						$state.go('main.userManager_add', {
							'userId' : userId
						});
					};

					$scope.edit2 = function(row) {
						var userId = row["id"];
						$state.go('main.userManager_add', {
							'userId' : userId
						});
					};

					$scope.auditTrail = function() {
						if ($scope.selected.length != 1) {
							
							alertService.add('danger',
									'Please select one record.');
							return;
						}
						$scope.auditTrailData = $scope.selected[0];
						ngDialog
								.open({
									className : 'ngdialog-theme-default custom-width-800',
									template : 'sys/layout/auditTrailDialog.html',
									closeByEscape: false,
									closeByDocument: false,
									scope : $scope,
									controller : [ '$scope', function($scope) {
										$scope.cancel = function() {
											$scope.closeThisDialog(0);
										};
									} ],
									preCloseCallback : function(value) {
										alertService.cleanAlert();
									}
								});
					};

					// delete user
					$scope.deleteUser = function(isDelete) {
						if ($scope.selected.length < 1) {
							
							alertService.add('danger',
									'Please select one record.');
							return;
						}
						if (!isDelete) {
							$('#confirmModal').modal({
								keyboard : false
							});
							return;
						}
						var userIds = "";
						for (var i = 0; i < $scope.selected.length; i++) {
							userIds = userIds + $scope.selected[i]["id"]
									+ ",";
						}
						$http({
							method : 'POST',
							params : {
								userIds : userIds
							},
							url : "./userManager/deleteUser"
						})
								.success(
										function(data) {
											if (data != null && data != '') {
												if (data['errorType'] != "success") {
													
													alertService
															.add(data["errorType"],data["errorMessage"]);
												} else {
													
													alertService
															.add("success","Deleted successfully.");
													$scope.search($scope.searchUserInfoVo);
												}
											}

										});
					};

					$scope.view = function() {
						var viewStatu = true;
						if ($scope.selected.length != 1) {
							
							alertService.add('danger',
									'Please select one record.');
							return;
						}
						console.log($scope.selected[0]);
						var userId = $scope.selected[0]["id"];
						$state.go('main.userManager_add', {
							'userId' : userId,
							'viewStatu' : viewStatu
						});
					};

					$scope.reset = function() {
						$scope.searchUserInfoVo.userCode = null;
						$scope.searchUserInfoVo.userName = null;
						cacheService.clearCache();
						alertService.cleanAlert();
					};

				});
