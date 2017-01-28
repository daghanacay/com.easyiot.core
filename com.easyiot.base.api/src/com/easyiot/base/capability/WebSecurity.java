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

	@ProvideCapability(ns = EasyiotNamespace.NS, name = HTTPS_SECURITY)
	@Retention(RetentionPolicy.CLASS)
	public @interface ProvideWebSecurity {
		String version() default "1.0.0";
	}

	@RequireCapability(ns = EasyiotNamespace.NS, filter = "(&(" + EasyiotNamespace.NS + "=" + HTTPS_SECURITY
			+ ")${frange;${version}})")
	@Retention(RetentionPolicy.CLASS)
	public @interface RequireWebSecurity {
		/**
		 * Version of the required security bundle
		 * 
		 * @return
		 */
		String version() default "1.0.0";
	}

}
