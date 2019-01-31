'use strict';

/**
 * @ngdoc directive
 * @name psspAdminApp.directive:appFooter
 * @description
 * # appFooter
 */

angular.module('psspAdminApp')
  .directive('appfooter',['$location',function() {
    return {
      templateUrl:'sys/directives/footer/footer.html',
      restrict: 'E',
      replace: true,
    }
  }]);
