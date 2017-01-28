package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;

public interface DeviceRest {
	public static final String DEVICE_REST = "device-rest";

	@ProvideCapability(ns = EasyiotNamespace.NS, name = DEVICE_REST)
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideDeviceRest {
		String version() default "1.0.0";
	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + DEVICE_REST
			+ ")${frange;${version}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireDeviceRest {
		/**
		 * Version of the required security bundle
		 * 
		 * @return
		 */
		String version() default "1.0.0";
	}
}
