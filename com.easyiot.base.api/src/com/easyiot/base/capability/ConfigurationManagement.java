package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;

public interface ConfigurationManagement {
	public static final String CONFIGURATION_MANAGEMENT = "configuration-management";

	@ProvideCapability(ns = EasyiotNamespace.NS, name = CONFIGURATION_MANAGEMENT)
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideConfigurationManagement {
		String version() default "1.0.0";
	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + CONFIGURATION_MANAGEMENT
			+ ")${frange;${version}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireConfigurationManagement {
		/**
		 * Version of the required ConfigurationManagement bundle
		 * 
		 * @return
		 */
		String version() default "1.0.0";
	}
}
