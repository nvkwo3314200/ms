'use strict';

angular.module('psspAdminApp').factory('userService', function($http, $q, $rootScope) {
	var userService = {};

	userService.getCurrentUser = function() {
        var deferred = $q.defer();
        
		$http({method:'POST', url:"./login/currentUser"}).
        success(function(data) {
        	deferred.resolve(data);
        });
        
        return deferred.promise;
		
	};
	
	userService.initSysDateFormat = function(){
		
		$http.post('./login/initSysDateFormat').success(
				
			function(data) {
				
				$rootScope.sysDateFormat = data.returnData;
				
			}
			
		);
		
	};
	
	return userService;
});
