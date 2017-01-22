var iotManagementPortalApp = angular.module('iotManagementPortalApp', [
	'ngRoute', 'configModule' ]);

iotManagementPortalApp.controller('MainController', [ '$location',
	function($location) {
	    var vm = this;

	    this.page = function() {
		return $location.path();
	    }
	} ]);

iotManagementPortalApp.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/configurations', {
	templateUrl : '/admin/main/htm/ConfigView.htm',
	controller : 'ConfigController'
    }).otherwise({
	redirectTo : '/configurations'
    });
} ]);