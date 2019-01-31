'use strict';

/**
 * @ngdoc directive
 * @name psspAdminApp.directive:sidebar
 * @description # sidebar
 * 
 * 
 * userRole:supplier ,approver,systemAdmin,
 */

angular.module('psspAdminApp').directive('sidebar', [ '$location', function() {
	return {
		templateUrl : 'sys/directives/sidebar/sidebar.html',
		restrict : 'E',
		replace : true,
		controller : function($scope, localStorageService, userService) {
			userService.getCurrentUser().then(function(data) {
				$scope.userName = data.userName;
				document.getElementById('sidebarMenu').innerHTML = data.currentMenu;
			});
			
//			var userName = localStorageService.get("userName");
//			$scope.userName = userName;

			// Create Menu
//			var sidebarMenuCode = localStorageService.get("currentMenu");
//			document.getElementById('sidebarMenu').innerHTML = sidebarMenuCode;
//			if(userName!=null && sidebarMenuCode==null){
//				window.location.reload();
//			}
		}
	};
}]);
