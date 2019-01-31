'use strict';

angular.module('psspAdminApp').factory('uuidService', ['$filter', function ($filter) {
	var format = "yyyy-MM-dd";
    return {  
        uuid: function () {  
           return guid(); 
        }  
    }; 
    //用于生成uuid
    function S4() {
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    function guid() {
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }
}]);  