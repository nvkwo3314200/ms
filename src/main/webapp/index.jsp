<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-control" content="no-cache">
		<meta http-equiv="Cache" content="no-cache">
		<title>Management System</title>
		<link rel="shortcut icon" href="sys/images/favicon.ico" />
		
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

		<!-- Bootstrap 3.3.5 -->
		<link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css">
		
		<!-- Font Awesome -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">

		<!-- Ionicons-->
		<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
     
		<!-- Theme style -->
		<link rel="stylesheet" href="dist/css/AdminLTE.min.css">
		<link rel="stylesheet" href="dist/css/skins/skin-blue.min.css">

		<!-- bower -->
		<script src="bower_components/jquery/dist/jquery.min.js"></script>
		<script src="bower_components/angular/angular.min.js"></script>
		<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
		<script src="bower_components/angular-confirm-master/angular-confirm.js"></script>
		<script src="bower_components/angular-loading-bar/loading-bar.min.js"></script>
		<script src="bower_components/angular-local-storage/dist/angular-local-storage.js"></script>
		<script src="bower_components/angular-multi-select-master/isteven-multi-select.js"></script>
		<script src="bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
		<script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
		<script src="bower_components/bower-angular-translate-2.9.0.1/angular-translate.js"></script>
		<script src="bower_components/bower-angular-translate-loader-static-files-master/angular-translate-loader-static-files.min.js"></script>
		<script src="bower_components/filesaver/FileSaver.min.js"></script>
		<script src="bower_components/json3/lib/json3.min.js"></script>
		<script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>
		<script src="bower_components/oclazyload/dist/ocLazyLoad.min.js"></script>
		<script src="bower_components/smart-table/smart-table.min.js"></script>
		
		<script src="bower_components/angular-file-upload-master/dist/angular-file-upload.js"></script>
		<script src="bower_components/angular-file-upload-master/dist/angular-file-upload.min.js"></script>

		<link rel="stylesheet" href="bower_components/angular-loading-bar/loading-bar.min.css">
		<link rel="stylesheet" href="bower_components/angular-multi-select-master/isteven-multi-select.css">
		<!-- endbower -->
     
		<!-- validation -->
		<script src="dist/angular-validation.js"></script>
		<script src="dist/angular-validation-rule.js"></script>
		<script src="dist/html5-sortable.js"></script>

		<link rel="stylesheet" href="dist/validation.css">
		<!-- end validation -->

		<!-- tree grid -->         
		<link rel="stylesheet" href="dist/tree/css/integralui.css" />
		<link rel="stylesheet" href="dist/tree/css/integralui.treegrid.css" />
		<link rel="stylesheet" href="dist/tree/css/integralui.checkbox.css" />
		<link rel="stylesheet" href="dist/tree/css/themes/theme-flat-blue.css" />
		<script type="text/javascript" src="dist/tree/js/angular.integralui.min.js"></script>
		<script type="text/javascript" src="dist/tree/js/angular.integralui.lists.min.js"></script>
		<script type="text/javascript" src="dist/tree/js/angular.integralui.treegrid.min.js"></script>
		<script type="text/javascript" src="dist/tree/js/angular.integralui.checkbox.min.js"></script>
		<!-- tree grid -->
		
		<script src="dist/ngDialog.js"></script>
		<script src="dist/js/app.min.js"></script>
		<link rel="stylesheet" href="dist/ngDialog.css">
		<link rel="stylesheet" href="dist/ngDialog-theme-default.css">
              
		<script src="sys/app.js"></script>
		<script src="sys/interceptors/responseInterceptor.js"></script>    
		<script src="sys/interceptors/requestInterceptor.js"></script>   
		<script src="sys/services/userService.js"></script>
		<script src="sys/services/alertService.js"></script>
		<script src="sys/services/cacheService.js"></script>
		<script src="sys/services/sysService.js"></script>
		
		<link rel="stylesheet" href="sys/css/iconfont/iconfont.css">
		<link rel="stylesheet" href="sys/css/common.css">
	</head>
	<body class="hold-transition skin-blue sidebar-mini login-page">
		<div ng-app="psspAdminApp">
			<div ui-view></div>
		</div>
	</body>
	<div style="width: 100%;text-align: center; margin-top: 15px;">
		<div>
			<a href="http://www.beian.miit.gov.cn/" target="_blank">
				<img data-src="//img.alicdn.com/tfs/TB1..50QpXXXXX7XpXXXXXXXXXX-40-40.png" height="25px" src="//img.alicdn.com/tfs/TB1..50QpXXXXX7XpXXXXXXXXXX-40-40.png">
				<span class="ali-report-link-text">粤ICP备20057669号</span>
			</a>
		</div>
	</div>
</html>