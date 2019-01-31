'use strict';
/**
 * @ngdoc overview
 * @name psspAdminApp
 * @description
 * # psspAdminApp
 *
 * Main module of the application.
 */

angular.module('psspAdminApp', [
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'LocalStorageModule',
    'pascalprecht.translate',
    'validation',
    'validation.rule',
    'isteven-multi-select',
    'smart-table',
    'html5.sortable',
    'ngDialog',
    'angular-confirm',
    'integralui',
    'angularFileUpload'
]).config([
	'$stateProvider',
	'$urlRouterProvider',
	'$ocLazyLoadProvider',
	'$httpProvider',
	'$translateProvider',
	'$validationProvider',
	'ngDialogProvider',
function (
	$stateProvider,
	$urlRouterProvider,
	$ocLazyLoadProvider,
	$httpProvider,
	$translateProvider,
	$validationProvider,
	ngDialogProvider
){
	$validationProvider.showSuccessMessage = false;
	ngDialogProvider.setForceHtmlReload(true);
	var lang = window.localStorage.lang || 'zh_TW';
	$translateProvider.preferredLanguage(lang);
	$translateProvider.useSanitizeValueStrategy(null);
	
	$translateProvider.useStaticFilesLoader({
		prefix: 'i18n/',
		suffix: '.json'
	});
	
	$ocLazyLoadProvider.config({
		debug: false,
		events: true,
	});

	$urlRouterProvider.otherwise('/main');
	$httpProvider.interceptors.push('requestInterceptor');
	$httpProvider.interceptors.push('responseInterceptor');

	if(!$httpProvider.defaults.headers.get){
		$httpProvider.defaults.headers.get = {};
	}

	$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
	$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

	$stateProvider
	.state('main', {
		url: '/main',
		templateUrl: 'sys/layout/main.html',
		resolve: {
			loadMyDirectives:function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [
					        'sys/directives/header/header.js',
					        'sys/directives/sidebar/sidebar.js',
                            'sys/directives/footer/footer.js',
                            'sys/directives/csSelect/csSelect.js',
                            'sys/directives/csSelect/pageSelect.js',
                            'sys/directives/csSelect/sysPagination.js',
                            'sys/directives/csSelect/rowSelectAll.js',
                            'sys/directives/csSelect/rowSelect.js',
                            'sys/services/alertService.js',
                            'sys/services/cacheService.js',
                            'sys/services/dateUtils.js',
                            'sys/services/uuid.js',
                            'sys/scripts/base.js',
                            'sys/services/sysService.js',
                            'sys/layout/pagination_custom.html'
                            ]
				}),
				$ocLazyLoad.load({
					name: 'toggle-switch',
					files: [
					        "bower_components/angular-toggle-switch/angular-toggle-switch.min.js",
					        "bower_components/angular-toggle-switch/angular-toggle-switch.css"
					        ]
				}),
				$ocLazyLoad.load({
					name: 'ngAnimate',
					files: ['bower_components/angular-animate/angular-animate.js']
				}),
				$ocLazyLoad.load({
					name: 'ngCookies',
					files: ['bower_components/angular-cookies/angular-cookies.js']
				}),
				$ocLazyLoad.load({
					name: 'ngResource',
					files: ['bower_components/angular-resource/angular-resource.js']
				}),
				$ocLazyLoad.load({
					name: 'ngSanitize',
					files: ['bower_components/angular-sanitize/angular-sanitize.js']
				});
			}
		}
	})
		
	.state('login', {
		templateUrl: 'sys/pages/login.html',
		url: '/login',
		controller: 'LoginCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/login.js' ]
				});
			}
		}
	})

	// ly Start
	.state('main.roleManager', {
		templateUrl: 'sys/pages/roleManager.html',
		url: '/roleManager',
		controller: 'RoleManagerCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
						name: 'psspAdminApp',
						files: [ 'sys/scripts/roleManager.js' ]
				});
			}
		}
	})

	.state('main.roleManager_add', {
		templateUrl: 'sys/pages/roleManagerAdd.html',
		url: '/roleManagerAdd?:roleId?:state',
		controller: 'RoleManagerAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/roleManagerAdd.js' ]
					});
				}
			}
	})
                
	.state('main.userManager', {
		templateUrl: 'sys/pages/userManager.html',
		params : {'userInfo':null},
		url: '/userManager',
		controller: 'UserManagerCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/userManager.js' ]
				});
			}
		}
	})
	            
	.state('main.userManager_add', {
		templateUrl: 'sys/pages/userManagerAdd.html',
		url: '/userManagerAdd?:userId?:viewStatu',
		controller: 'UserManagerAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/userManagerAdd.js' ]
				});
			}
		}
	})
	
	.state('main.prod_operation', {
		templateUrl: 'ms/pages/productionOperation.html',
		url: '/productionOperation',
		controller: 'productionOperationCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/productionOperation.js' ]
				});
			}
		}
	})
	 
	.state('main.production_operation_add', {
		templateUrl: 'ms/pages/productionOperationAdd.html',
		url: '/productionOperationAdd?:opId?:opAddStatus?:viewStatu',
		controller: 'productionOperationAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/productionOperationAdd.js' ]
				});
			}
		}
	})
	
	.state('main.orderDataMgt', {
		templateUrl: 'ms/pages/orderDataMgt.html',
		url: '/orderDataMgt',
		controller: 'orderDataMgtCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/orderDataMgt.js' ]
				});
			}
		}
	})
	            
	// ly End
	
	
	//lijun start
	.state('main.resource_group', {
		templateUrl: 'ms/pages/resourceGroup.html',
		url: '/resourcegroup',
		controller: 'resourceCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourcegroup.js' ]
				});
			}
		}
	})
	
	.state('main.resource_groupadd', {
		templateUrl: 'ms/pages/resourceGroupadd.html',
		url: '/resourcegroupadd?:resGrpID',
		controller: 'resourceAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourcegroupAdd.js' ]
				});
			}
		}
	})
	
	.state('main.operationAndTim', {
		templateUrl: 'ms/pages/operationAndTimeDetaile.html',
		url: '/operationAndTim',
		controller: 'operationAndTimCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/opeartionAndTimeDetail.js' ]
				});
			}
		}
	})
	
	.state('main.userloginInfo', {
		templateUrl: 'sys/pages/userLoginInfo.html',
		url: '/userloginInfo',
		controller: 'userloginInfoCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/userLoginInfo.js' ]
				});
			}
		}
	})
	
	.state('main.userInfo', {
		templateUrl: 'sys/pages/userInfo.html',
		url: '/userInfo',
		controller: 'userInfoCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/userInfo.js' ]
				});
			}
		}
	})
	
	.state('main.picManage', {
		templateUrl: 'sys/pages/media/picManage.html',
		url: '/picManage',
		controller: 'PicManageCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/media/picManage.js' ]
				});
			}
		}
	})
	
	.state('main.pic_add', {
		templateUrl: 'sys/pages/media/picAdd.html',
		url: '/picAdd?:id?:state',
		controller: 'picAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/media/picAdd.js' ]
				});
			}
		}
	})
	
	.state('main.cpaAnaBookOrd', {
		templateUrl: 'ms/pages/newSingle.html',
		url: '/newSingle',
		controller: 'newSingleCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/newSingle.js' ]
				});
			}
		}
	})
	//lijun end
	
	// Keven Start
	.state('main.menu_manager', {
		templateUrl: 'sys/pages/menuManager.html',
		url: '/menuManager',
		controller: 'MenuManagerCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/menuManager.js' ]
				});
			}
		}
	})
	            
	.state('main.menu_add', {
		templateUrl: 'sys/pages/menuAdd.html',
		url: '/menuAdd?:id?:state',
		controller: 'menuAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/menuAdd.js' ]
				});
			}
		}
	})
	            
	.state('main.bus_unit', {
		templateUrl: 'sys/pages/busUnit.html',
		url: '/busUnit',
		controller: 'busUnitCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/busUnit.js' ]
				});
			}
		}
	})
	            
	.state('main.bus_add', {
		templateUrl: 'sys/pages/busUnitAdd.html',
		url: '/busUnitAdd?:busUnitCode?:state',
		controller: 'busUnitAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/busUnitAdd.js' ]
				});
			}
		}
	})
	
	.state('main.operation_time', {
		templateUrl: 'ms/pages/operationTime.html',
		url: '/operationTime',
		controller: 'operationTimeCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/operationTime.js' ]
				});
			}
		}
	})
	
	.state('main.operation_time_add', {
		templateUrl: 'ms/pages/operationTimeAdd.html',
		url: '/operationTimeAdd?:id?:state',
		controller: 'operationTimeAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/operationTimeAdd.js' ]
				});
			}
		}
	})
	// Keven End
	//chen
	.state('main.flow', {
		templateUrl: 'ms/pages/flow.html',
		url: '/flow',
		controller: 'flowCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/flow.js' ]
				});
			}
		}
	})
	            
	.state('main.flow_add', {
		templateUrl: 'ms/pages/flowAdd.html',
		url: '/flowAdd?:flowId?:state?:active',
		controller: 'flowAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/flowAdd.js' ]
				});
			}
		}
	})
	
	.state('main.flowLog', {
		templateUrl: 'ms/pages/flowLog.html',
		url: '/flowLog',
		controller: 'flowLogCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/flowLog.js' ]
				});
			}
		}
	})
	
	.state('main.flowLogAdd', {
		templateUrl: 'ms/pages/flowLogAdd.html',
		params : {'flowLogModel':null},
		url: '/flowLogAdd',
		controller: 'flowLogAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/flowLogAdd.js' ]
				});
			}
		}
	})
	
	
	.state('main.operationFlow', {
		templateUrl: 'ms/pages/operationFlowAdd.html',
		url: '/operationFlow',
		controller: 'flowAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/flowAdd.js' ]
				});
			}
		}
	})
	
	.state('main.time_type', {
		templateUrl: 'ms/pages/timeType.html',
		url: '/timetype',
		controller: 'timeTypeCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/timeType.js' ]
				});
			}
		}
	})
	            
	.state('main.timetype_add', {
		templateUrl: 'ms/pages/timeTypeAdd.html',
		url: '/timeTypeAdd?:timeType?:editFlag?:viewFlag',
		controller: 'timeTypeAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/timeTypeAdd.js' ]
				});
			}
		}
	})
	
	.state('main.workCntr', {
		templateUrl: 'ms/pages/workCntr.html',
		url: '/workCntr',
		controller: 'workCntrCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/workCntr.js' ]
				});
			}
		}
	})
	            
	.state('main.workCntr_add', {
		templateUrl: 'ms/pages/workCntrAdd.html',
		url: '/workCntrAdd?:workCntrId?:flag?:flag2',
		controller: 'workCntrAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/workCntrAdd.js' ]
				});
			}
		}
	})
	
	.state('main.cssData', {
		templateUrl: 'ms/pages/cssDataMModel.html',
		url: '/cssData',
		controller: 'cssDataCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/cssDataMModel.js' ]
				});
			}
		}
	})
	
	.state('main.effic', {
		templateUrl: 'ms/pages/effic.html',
		url: '/effic',
		controller: 'efficCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/effic.js' ]
				});
			}
		}
	})
	
	.state('main.totalAtten', {
		templateUrl: 'ms/pages/totalAttendance.html',
		url: '/totalAtten?:plantCode?:year?:month',
		controller: 'totalAttenCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/totalAtten.js' ]
				});
			}
		}
	})
	
	.state('main.efficpreview', {
		templateUrl: 'ms/pages/efficpreview.html',
		url: '/efficpreview?:plantCode?:year?:month',
		controller: 'efficpreviewCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/efficpreview.js' ]
				});
			}
		}
	})
	
	.state('main.resData', {
		templateUrl: 'ms/pages/resData.html',
		url: '/resData',
		controller: 'resDataCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resData.js' ]
				});
			}
		}
	})
                
	//xp Start
	.state('main.menu_system_parameters', {
		templateUrl: 'sys/pages/systemParameters.html',
		url: '/systemPara',
		controller: 'systemParametersCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/systemParameters.js' ]
				});
			}
		}
	})

	.state('main.system_para_add', {
		templateUrl: 'sys/pages/systemParaAdd.html',
		url: '/systemParaAdd?:id?:state',
		controller: 'SystemParaCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'sys/scripts/systemParaAdd.js' ]
				});
			}
		}
	})
	
	.state('main.plant', {
		templateUrl: 'ms/pages/plant.html',
		url: '/plant',
		controller: 'PlantCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/plant.js' ]
				});
			}
		}
	})
	
	.state('main.plant_add', {
		templateUrl: 'ms/pages/plantAdd.html',
		url: '/plantAdd?:plantCode?:state',
		controller: 'PlantAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/plantAdd.js' ]
				});
			}
		}
	})
	
	.state('main.location', {
		templateUrl: 'ms/pages/location.html',
		url: '/location',
		controller: 'LocationCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/location.js' ]
				});
			}
		}
	})
	
	.state('main.location_add', {
		templateUrl: 'ms/pages/locationAdd.html',
		url: '/locationAdd?:locId?:state',
		controller: 'LocationAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/locationAdd.js' ]
				});
			}
		}
	})
	
	.state('main.order', {
		templateUrl: 'ms/pages/order.html',
		url: '/order',
		controller: 'OrderCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/order.js' ]
				});
			}
		}
	})
	
	.state('main.order_add', {
		templateUrl: 'ms/pages/orderAdd.html',
		url: '/orderAdd?:demndId?:state',
		controller: 'OrderAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/orderAdd.js' ]
				});
			}
		}
	})
	
	.state('main.forecast_data', {
		templateUrl: 'ms/pages/yearlyPlanModelDetial.html',
		params : {'yearlyPlanModel':null},
		url: '/forecast',
		controller: 'YearlyPlanModelDetialCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/yearlyPlanModelDetial.js' ]
				});
			}
		}
	})
	
	.state('main.newSingle_detail', {
		templateUrl: 'ms/pages/newSingleDetail.html',
		params : {'newsingleModel':null},
		url: '/newSingleDetail',
		controller: 'NewSingleDetialCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/newSingleDetail.js' ]
				});
			}
		}
	})
	//xp End
	
	//
	.state('main.operationGrp', {
		templateUrl: 'ms/pages/operationGrp.html',
		url: '/operationGrp',
		controller: 'operationGrpCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/operationGrp.js' ]
				});
			}
		}
	})
	
	.state('main.operationGrp_add', {
		templateUrl: 'ms/pages/operationGrpAdd.html',
		url: '/oparationGrpAdd?:grpId?:UUflag?:Uflag',
		
		controller: 'operationGrpAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/operationGrpAdd.js' ]
				});
			}
		}
	})

	
	//TJL Start
	.state('main.spec_type', {
		templateUrl: 'ms/pages/specType.html',
		url: '/specType',
		controller: 'specTypeCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/specType.js' ]
				});
			}
		}
	})

	.state('main.spec_type_add', {
		templateUrl: 'ms/pages/specTypeAdd.html',
		url: '/specTypeAdd?:specTypeId?:state',
		controller: 'specTypeAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/specTypeAdd.js' ]
				});
			}
		}
	})
	
	.state('main.spec', {
		templateUrl: 'ms/pages/spec.html',
		url: '/spec',
		controller: 'specCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/spec.js' ]
				});
			}
		}
	})
	
	.state('main.spec_add', {
		templateUrl: 'ms/pages/specAdd.html',
		url: '/specAdd?:specId?:state',
		controller: 'specAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/specAdd.js' ]
				});
			}
		}
	})
	
	.state('main.res', {
		templateUrl: 'ms/pages/resource.html',
		url: '/res',
		controller: 'resCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resource.js' ]
				});
			}
		}
	})
	
	.state('main.res_add', {
		templateUrl: 'ms/pages/resourceAdd.html',
		url: '/resAdd?:resId?:state',
		controller: 'resAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourceAdd.js' ]
				});
			}
		}
	})
	
	.state('main.resPlan', {
		templateUrl: 'ms/pages/yearlyPlanResDetail.html',
		params : {'yearlyPlanModel':null},
		url: '/resPlan',
		controller: 'resPlanCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/yearlyPlanResDetail.js' ]
				});
			}
		}
	})
	
	.state('main.yearlyCapacityAnalysis', {
		templateUrl: 'ms/pages/yearlyCapacityAnalysis.html',
		url: '/yearlyCapacityAnalysis?:p?:y?:m?o:?:r',
		controller: 'yearlyCapacityAnalysisCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/yearlyCapacityAnalysis.js' ]
				});
			}
		}
	})
	//TJL End
			.state('main.prodU', {
		templateUrl: 'ms/pages/prodU.html',
		url: '/prodU',
		controller: 'prodUCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/prodU.js' ]
				});
			}
		}
	})
	            
	.state('main.prodU_add', {
		templateUrl: 'ms/pages/prodUAdd.html',
		url: '/prodUAdd?:prodLn?:UUflag?:Uflag',
		controller: 'prodUAddCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/prodUAdd.js' ]
				});
			}
		}
	})
	
		.state('main.plantprodU', {
		templateUrl: 'ms/pages/plantprodU.html',
		url: '/plantprodU',
		controller: 'plantprodUCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/plantprodU.js' ]
				});
			}
		}
	})
	//YF
	.state('main.resourceanalysis', {
		templateUrl: 'ms/pages/resourceanalysis.html',
		url: '/resourceanalysis',
		controller: 'resourceanalysisCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourceanalysis.js' ]
				});
			}
		}
	})
	.state('main.res_ana_etdel', {
		templateUrl: 'ms/pages/resourcePreview.html',
		params : {'resourceAnalysisModel':null},
		url: '/resourcePreview',
		controller: 'resourcePreviewCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourcePreview.js' ]
				});
			}
		}
	})
	.state('main.Simulation', {
		templateUrl: 'ms/pages/resourceSIM.html',
		url: '/resourceSIM',
		controller: 'resourceSIMCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/resourceSIM.js' ]
				});
			}
		}
	})
	//YF End
	
	//lh
	.state('main.css_model', {
		templateUrl: 'ms/pages/cssModel.html',
		url: '/cssModel',
		controller: 'cssModelCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/cssModel.js' ]
				});
			}
		}
	})
		
	.state('main.css_model_detail', {
		templateUrl: 'ms/pages/cssModelDetail.html',
		url: '/cssModelDetail:state',
		controller: 'cssModelDetailCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/cssModelDetail.js' ]
				});
			}
		}
	})
	
	.state('main.yearly_plan', {
		templateUrl: 'ms/pages/yearlyPlan.html',
		params : {'yearlyPlanModel':null},
		url: '/yearlyPlan',
		controller: 'yearlyPlanCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/yearlyPlan.js' ]
				});
			}
		}
	})
	
	.state('main.new_single_etdel', {
		templateUrl: 'ms/pages/newSingleETDEL.html',
		params : {'newsingleModel':null},
		url: '/newSingleETDEL',
		controller: 'newSingleETDELCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/newSingleETDEL.js' ]
				});
			}
		}
	})
	
	.state('main.op_spec_data', {
		templateUrl: 'ms/pages/operationSpecData.html',
		url: '/operationSpecData',
		controller: 'operationSpecDataCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/operationSpecData.js' ]
				});
			}
		}
	})
	
	.state('main.demo', {
		templateUrl: 'ms/pages/demo.html',
		url: '/demo',
		controller: 'demoCtrl',
		resolve: {
			loadMyFiles: function ($ocLazyLoad) {
				return $ocLazyLoad.load({
					name: 'psspAdminApp',
					files: [ 'ms/scripts/demo.js' ]
				});
			}
		}
	});
	
}]).factory('T', ['$translate', function($translate) {
	var T = {
		T:function(key) {
			if(key){
				return $translate.instant(key);
			}
			return key;
		}
	};
    return T;
}]).run(function ($rootScope, $state, localStorageService, userService, ngDialog) {
	
	userService.initSysDateFormat();
	
    $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams, fromState, fromStateParams) {
    	
    	ngDialog.closeAll();	//close all dialog and return previous page -by keven in September 5, 2016
    	
        if (toState.name.indexOf("login") == -1) {
        	userService.getCurrentUser().then(function(data) {
        		if (data == null || data == '') {
	       			 event.preventDefault();
	       			 $state.go("login");
        		}
        	}, function(data) {
        		//cannot get user info, go to login page
            	event.preventDefault();
                $state.go("login");
            }); 
        }
    });
});