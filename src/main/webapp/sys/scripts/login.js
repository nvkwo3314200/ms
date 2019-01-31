﻿'use strict';
/**
 * @ngdoc function
 * @name psspAdminApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the psspAdminApp
 */
angular.module('psspAdminApp')
	.controller('LoginCtrl', function ($scope, $state, localStorageService, alertService, $translate, $http) {
        $scope.language = "zh_TW";
        $scope.rememberMe = true;
        
        $scope.langList = [{
            code: "en",
            description: 'English'
        },{
            code: "zh_CN",
            description: '简体中文'
        },{
            code: "zh_TW",
            description: "繁體中文"
        }];
        
    	$translate.use($scope.language);
    	window.localStorage.lang = $scope.language;
       
//        if(typeof(window.localStorage.lang)!='undefined'){
//        	$scope.language = window.localStorage.lang;
//        }
        
        function updateLoginMsg(language){
    		$http({
    			method : 'POST',
    			params : {
    				lastLanguage:language
    			},
    			url : "./login/updateLoginMsg"
    		});
        }
        
        $scope.switching = function(lang){
        	$translate.use(lang.language);
        	window.localStorage.lang = lang.language;
        };
        
        $scope.clearErrorMsg = function () {
        	alertService.cleanAlert();
        };
        
        $scope.enter = function(evt){
        	
        	if(evt.keyCode == 13) $scope.login();
        	
        };
        
        $scope.login = function () {
        	$http({method:'POST',
            	params:{
            		username: $scope.userName,
            		password: $scope.password,
            		language: $scope.language,
            		rememberMe: $scope.rememberMe
            	},
            	url:"./j_spring_security_check"
            }).success(function(data) {
            	if (data['errorType'] == "success") {
	            	alertService.cleanAlert();
	                updateLoginMsg($scope.language);
	                $state.go('main');
            	} else {
            		alertService.add(data["errorType"], data["errorMessage"]);
            	}
            });
        	
        	
            /*AuthService.login($scope.userId, $scope.password, $scope.language, $scope.rememberMe)
            .then(function (res) {
            	var data = res.data;
            	if (data['errorType'] == "success") {
                	localStorageService.set("id", data['returnData']['id']);
                    localStorageService.set("userCode", data['returnData']['userCode']);
                    localStorageService.set("userName", data['returnData']['userName']);
//                  localStorageService.set("authToken", data['returnData']['authToken']);
                    localStorageService.set("userRole", data['returnData']['userRole']);
                    localStorageService.set("email", data['returnData']['email']);
//                    window.localStorage.lang = $scope.language;
                    alertService.cleanAlert();
                    updateLoginMsg($scope.language);
                    //User Story 56 -	Switch off functions for soft launch
                    //localStorageService.set("switchOff", $scope.switchOff);
                    $state.go('main');
                } else {
                	 
                    alertService.add(data["errorType"], data["errorMessage"]);
                };
            });*/
        };
});