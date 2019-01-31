'use strict';

/**
 * @ngdoc directive
 * @name psspAdminApp.directive:appHeader
 * @description # appHeader
 */
angular.module('psspAdminApp').directive('appheader', function() {
	return {
		templateUrl : 'sys/directives/header/header.html',
		restrict : 'E',
		replace : true,
		controller : function($scope,$http,$state,$location,localStorageService) {
			var o = $.AdminLTE.options;
			if (o.sidebarPushMenu) {
				$.AdminLTE.pushMenu.activate(o.sidebarToggleSelector);
			}
			
			$scope.logOut = function () {
				console.log("===========logout");
//			    var id= localStorageService.get("id");
//			    return $http({method:'POST', params:{ id:id}, url:"./login/logOut"}).
//	            success(function(data) { 
//	            	localStorageService.set("id", "");
//                    localStorageService.set("userId", "");
//                    localStorageService.set("userName", "");
//                    localStorageService.set("authToken", "");
//                    localStorageService.set("userRole", "");
//                  $state.go('login');
//	            });
				console.log($location);
				$location.path('/logout').replace();;
	        };
	        
	        
		}
	}
})

