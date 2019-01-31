'use strict';

angular.module('psspAdminApp')
    .directive('rowSelectAll', function() {
        return {
            require: '^stTable',
            template: '<input type="checkbox">',
            scope: {
                all: '=rowSelectAll',
                selected: '='
            },
            link: function (scope, element, attr) {
                scope.isAllSelected = false;

            	element.addClass('custom-table-checkbox');
                
                element.bind('click', function (evt) {

                    scope.$apply(function () {

                        scope.all.forEach(function (val) {

                            val.isSelected = scope.isAllSelected;

                        });

                    });
                });

                scope.$watchCollection('selected', function(newVal, oldVal) {
                	scope.isAllSelected = false;
                    var s = newVal.length;
                    var a = scope.all.length;
                    if ((s == a) && s > 0 && a > 0) {
                        element.find('input').prop('checked', true);
                        scope.isAllSelected = false;
                    } else {

                        element.find('input').prop('checked', false);
                        scope.isAllSelected = true;

                    }

                });
            }
        };
    });