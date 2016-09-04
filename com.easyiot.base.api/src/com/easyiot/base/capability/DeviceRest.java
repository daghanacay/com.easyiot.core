package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;

public interface DeviceRest {
	public static final String DEVICE_REST = "device-rest";

	@ProvideCapability(ns = EasyiotNamespace.NS, name = DEVICE_REST, version="1.0.0")
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideDeviceRest_v1_0_0 {

	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + DEVICE_REST
			+ ")${frange;${versionStr}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireDeviceRest {
		/**
		 * Version of the required security bundle
		 * 
		 * @return
		 */
		String versionStr() default "1.0.0";
	}
}
