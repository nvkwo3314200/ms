'use strict';

angular.module('psspAdminApp').factory('cacheService', function($location) {
	
	var cacheService = {};
	
	cacheService.cache = {};
	
	cacheService.setCache = function(cache) {
		
		cacheService.cache = cache;
		
	};
	
	cacheService.getCache = function() {
		
		var isSelfCache = ($location.url() != location.hash.replace(/#/, ''));
		
		if(!isSelfCache) cacheService.clearCache();
		
		return cacheService.cache;
		
	};
	
	cacheService.clearCache = function() {
		
		cacheService.cache = {};
		
	};

	return cacheService;
});
