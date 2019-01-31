'use strict';

angular.module('psspAdminApp').factory('dateUtilsService', ['$filter', function ($filter) {
	var format = "yyyy-MM-dd";
    return {  
        getDate2Str: function (date, format) {  
            if (angular.isDate(date) && angular.isString(format)) {  
                return $filter('date')(date, format);  
            }  
        },  
        getStr2Date: function (string, format) {  
            if (angular.isString(string) && angular.isString(format)) {
            	var dateStr = formatString(string, format);
                return new Date(dateStr);  
            }  
        }  
    }; 
    
   function formatString (dateStr , format) {
	   var formats = format.split(/\W/g);
	   var dateStrs = dateStr.split(/\W/g);
	   var year = '';
	   var month = '';
	   var day = '';
	   if(formats.length == 3) {
		   for(var i = 0; i < 3; i++) {
			   if(formats[i].toUpperCase().indexOf("Y") > -1) {
				   year = dateStrs[i];
			   } else if(formats[i].toUpperCase().indexOf("M") > -1) {
				   month = dateStrs[i];
			   }else if(formats[i].toUpperCase().indexOf("D") > -1) {
				   day = dateStrs[i];
			   } else {
				   return utilConvertDateToString.getDateToString(new Date(), "yyyy/MM/dd");
			   }
		   }
	   }
	   if(year != '' && month != '' && day != '') {
		   return (year+'/'+month+'/'+day);
	   } else {
		   return utilConvertDateToString.getDateToString(new Date(), "yyyy/MM/dd");
	   }
   }
}]);  