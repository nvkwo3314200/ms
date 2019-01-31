'use strict';

angular.module('psspAdminApp').factory('alertService', function($rootScope) {
	var alertService = {};

	$rootScope.alerts = [];

	alertService.isSaveSuccess = false;
	
	alertService.add = function(type, msg) {
		
		alertService.cleanAlert();
		
		$rootScope.alerts.push({
			'type' : type,
			'msg' : msg,
			'close' : function(index){ alertService.closeAlert(index); }
		});
		
	};

	alertService.closeAlert = function(index) {
		
		$rootScope.alerts.splice(index, 1);
		
	};
	
	alertService.cleanAlert = function() {
		
		$rootScope.alerts = [];
		
	};
	
	alertService.showSuccessMsg = function(){
		
		if(alertService.isSaveSuccess){
			alertService.add('success', 'Saved successfully.');
			alertService.isSaveSuccess = false;
		}
		
	};
	
	alertService.saveSuccess = function(){
		
		alertService.isSaveSuccess = true;
		
	};

	return alertService;
});
