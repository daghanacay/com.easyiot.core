package com.easyiot.base.capability;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import aQute.bnd.annotation.headers.ProvideCapability;
import aQute.bnd.annotation.headers.RequireCapability;

/**
 * A web security bundle is added to the deployment.
 */
public interface WebSecurity {
	public static final String HTTPS_SECURITY = "https_security";

	@ProvideCapability(ns = EasyiotNamespace.NS, name = HTTPS_SECURITY, version="1.0.0")
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideWebSecurity_v1_0_0 {

	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + HTTPS_SECURITY
			+ ")${frange;${versionStr}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireWebSecurity {
		/**
		 * Version of the required security bundle
		 * 
		 * @return
		 */
		String versionStr() default "1.0.0";
	}

}
