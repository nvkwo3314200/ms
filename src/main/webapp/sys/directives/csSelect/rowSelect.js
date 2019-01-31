'use strict';

angular.module('psspAdminApp').directive('rowSelect', function () {
    return {
        require: '^stTable',
        template: '<input type="checkbox">',
        scope: {
            row: '=rowSelect'
        },
        link: function (scope, element, attr, ctrl) {
        	
        	element.addClass('custom-table-checkbox');
        	
            element.parent().bind('click', function (evt) {

                scope.$apply(function () {
                	
                    ctrl.select(scope.row, 'multiple');
                    
                });

            });

            scope.$watch('row.isSelected', function (newValue) {
            	
                if (newValue === true) {
                	
                    element.parent().addClass('st-selected');
                    element.find('input').prop('checked', true);
                    
                } else {
                	
                    element.parent().removeClass('st-selected');
                    element.find('input').prop('checked', false);
                    
                }
            });
        }
    };
});