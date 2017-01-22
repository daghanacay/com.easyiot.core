package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;
import osgi.enroute.namespace.WebResourceNamespace;

public interface ConfigurationManagementApplication {
	public static final String CONFIGURATION_MANAGEMENT_APPLICATION = "configuration-management-application";

	@ProvideCapability(ns = WebResourceNamespace.NS, name = CONFIGURATION_MANAGEMENT_APPLICATION)
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideConfigurationManagementApplication {
		String version() default "1.0.0";
		String root() default "static";
	}

	@RequireCapability(ns = WebResourceNamespace.NS, filter = "(&(" + WebResourceNamespace.NS + "="
			+ CONFIGURATION_MANAGEMENT_APPLICATION + ")${frange;${version}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireConfigurationManagementApplication {
		/**
		 * Version of the required ConfigurationManagementApplication bundle
		 * 
		 * @return
		 */
		String version() default "1.0.0";
	}
}
