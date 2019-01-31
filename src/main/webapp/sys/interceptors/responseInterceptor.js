'use strict';

angular.module('psspAdminApp').factory('responseInterceptor', ['$q', '$location', 'alertService', function($q, $location, alertService) {  
    var responseInterceptor = {
            responseError: function (response) {
//                console.log("[app.js]-> response status:" + response.status);
                if (response.status == 401) {
                    $location.path('/login');
                }
                
                if (response.status == 400) {
//                    console.log(response.data);
   	                alertService.add("danger", response.data["message"]);
                }
                return $q.reject(response);
            }
        };

        return responseInterceptor;
}]);
