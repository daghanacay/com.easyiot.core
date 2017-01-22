package com.easyiot.base.configuration.admin.application;

import org.osgi.service.component.annotations.Component;

import com.easyiot.base.capability.ConfigurationManagement.RequireConfigurationManagement;
import com.easyiot.base.capability.ConfigurationManagementApplication.ProvideConfigurationManagementApplication;

import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.google.angular.capabilities.RequireAngularWebResource;
import osgi.enroute.twitter.bootstrap.capabilities.RequireBootstrapWebResource;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@ProvideConfigurationManagementApplication
@RequireConfigurationManagement
@RequireAngularWebResource(resource = { "angular.js", "angular-resource.js", "angular-route.js" }, priority = 1000)
@RequireBootstrapWebResource(resource = "css/bootstrap.css")
@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name = "com.easyiot.base.configuration.admin")
public class AdminApplication {

}
