'use strict';

angular.module('psspAdminApp').directive('sysPagination', function() {
	return {
		templateUrl : 'sys/layout/sys_pagination.html',
		restrict : 'E',
		replace : true,
		scope:{
			page: "=page",
			searchModel: "=searchModel",
			search: "&onSearch",
			
		},
		link: function ($scope) {
			$scope.initSearchModel = function(){
				$scope.searchModel.size = $scope.page.pageSize || 10;
				$scope.searchModel.page = $scope.page.pageNum || 1;
			};
			
			$scope.selectPage = function(current){
				console.log($scope.searchModel);
				if(current) {
					current = current || $scope.page.pageNum
					current = Math.abs( parseInt(current || 1) );
					$scope.searchModel.page = Math.min(current, $scope.page.pages || 1);
					$scope.search($scope.searchModel);
				}
			};
			
			$scope.selectRow = function(row){
				row = Math.abs( parseInt(row) );
				$scope.searchModel.size = Math.min(row, $scope.page.total || row);
				$scope.selectPage(1);
				
			};
			
			$scope.$watch('page', function(newVal, oldVal){
				console.log($scope.page);
				$scope.initSearchModel();
			});
			
			$scope.initSearchModel();
			$scope.selectPage(1);
        }
	};
});