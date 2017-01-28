var configModule = angular.module('configModule', []);

configModule.controller('ConfigController', [
	'$http',
	'$filter',
	function($http, $filter) {
	    // Init
	    var vm = this;
	    this.configs = [];
	    this.selectedConfig = null;
	    this.childConfigs = null
	    this.childFrom = null;
	    this.showTheForm = false;
	    getAllConfigs();

	    // functions
	    this.selectConfig = function(config) {
		this.selectedConfig = config;
		this.getChildConfigs(config);
		this.showTheForm = false;
	    };

	    // Get the configurations
	    function getAllConfigs() {
		$http({
		    method : 'GET',
		    url : '/rest/RegisteredDevicesOrProtocols'
		}).then(function successCallback(response) {
		    vm.configs = response.data;
		    vm.selectConfig(vm.configs[0]);
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
	    }
	    ;

	    // gets all the child configs for a factory config
	    this.getChildConfigs = function(config) {
		$http({
		    method : 'GET',
		    url : '/rest/DeviceOrProtocolInstances/' + config
		}).then(function successCallback(response) {
		    vm.childConfigs = response.data;
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
		this.showTheForm = true;
		this.selectedConfig = config;
		this.childSelected = false;
	    };

	    // Create a child form for a new configuration
	    this.newChildForm = function(config) {
		$http(
			{
			    method : 'GET',
			    url : '/rest/RegisteredDevicesOrProtocolsMetaData/' + config
			}).then(function successCallback(response) {
		    vm.childForm = response.data;
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
		this.showTheForm = true;
		this.selectedConfig = config;
		this.childSelected = false;
	    };
	    // TODO LEFT HERE
	    // Create an update form for existing child
	    this.updateChildForm = function(childConfigStr) {
		$http({
		    method : 'GET',
		    url : '/system/console/configMgr/' + childConfigStr,
		    params : {
			'post' : 'true'
		    }
		}).then(function successCallback(response) {
		    vm.childForm = response.data;
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
		this.showTheForm = true;
		this.selectedChildConfig = childConfigStr;
		this.childSelected = true;
	    };

	    this.addConfig = function() {
		$http({
		    method : 'POST',
		    url : '/rest/DeviceOrProtocolInstanceProperties',
		    data : vm.childForm
		}).then(function successCallback(response) {
		    getAllConfigs();
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
	    };

	    this.updateConfig = function() {
		$http(
			{
			    method : 'PUT',
			    url : '/rest/DeviceOrProtocolInstanceProperties/'
				    + vm.childForm.pid,
			    data : vm.childForm
			}).then(function successCallback(response) {
		    getAllConfigs();
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
	    };

	    this.removeConfig = function() {
		$http({
		    method : 'DELETE',
		    url : '/rest/DeviceOrProtocolInstance/' + vm.childForm.pid
		}).then(function successCallback(response) {
		    getAllConfigs();
		}, function errorCallback(response) {
		    this.alerts.push({
			type : 'failure',
			msg : response
		    });
		});
	    };

	} ]);
