package com.easyiot.base.test.util;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Base class for integration tests
 * 
 * @author daghan
 *
 */
public class IntegrationTestBase {
	protected final static BundleContext context = FrameworkUtil.getBundle(IntegrationTestBase.class)
			.getBundleContext();

	protected static <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void pushFactoryConfig(Map<String, String> propertiesMap, String factoryPid) throws IOException {
		ServiceReference configurationAdminReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (configurationAdminReference != null) {

			ConfigurationAdmin confAdmin = (ConfigurationAdmin) context.getService(configurationAdminReference);

			Configuration configuration = confAdmin.createFactoryConfiguration(factoryPid, null);
			Dictionary properties = new Hashtable<>();
			// See com.easyiot.http.protocol.provider HttpProtocolConfiguration
			for (Entry<String, String> entry : propertiesMap.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}

			configuration.update(properties);

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void pushConfig(Map<String, String> propertiesMap, String pid) throws IOException {
		ServiceReference configurationAdminReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (configurationAdminReference != null) {

			ConfigurationAdmin confAdmin = (ConfigurationAdmin) context.getService(configurationAdminReference);

			Configuration configuration = confAdmin.getConfiguration(pid, null);
			Dictionary properties = new Hashtable<>();
			// See com.easyiot.http.protocol.provider HttpProtocolConfiguration
			for (Entry<String, String> entry : propertiesMap.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}

			configuration.update(properties);

		}
	}
}
