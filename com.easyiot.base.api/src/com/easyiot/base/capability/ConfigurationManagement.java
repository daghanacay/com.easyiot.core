package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;

public interface ConfigurationManagement {
	public static final String CONFIGURATION_MANAGEMENT = "configuration-management";

	@ProvideCapability(ns = EasyiotNamespace.NS, name = CONFIGURATION_MANAGEMENT, version="1.0.0")
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideConfigurationManagement_v1_0_0 {

	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + CONFIGURATION_MANAGEMENT
			+ ")${frange;${versionStr}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireConfigurationManagement {
		/**
		 * Version of the required ConfigurationManagement bundle
		 * 
		 * @return
		 */
		String versionStr() default "1.0.0";
	}
}
