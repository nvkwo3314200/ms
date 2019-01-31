'use strict';

angular.module('psspAdminApp').factory('requestInterceptor', ['$q', 'alertService', function($q, alertService) {  
    var requestInterceptor = {
    		request: function(config) {
    			var url = config.url;
    			
//    			if(url.lastIndexOf(".")==-1 || url.lastIndexOf(".") == 0){
    			if(url.indexOf("/login/currentUser") != -1){
//    				console.log(url);
//    				console.log("cleanAlert");
    				alertService.cleanAlert();
    			}
                return config;
            }
        };

        return requestInterceptor;
}]);
