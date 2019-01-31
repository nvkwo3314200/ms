'use strict';

angular.module('psspAdminApp').factory('sysService', function($http, $location, $rootScope) {
	
	var sysService = {};
	
	sysService.test = function() {
		
		$http.post(
				'./login/getCurrentPowers',
				{url : $location.url().replace('/main/', '')}
			).success(function(data) {
//				$scope.powers = data.returnData;
//				console.log(data);
			});
		
	};

	return sysService;
});
