package com.easyiot.base.util;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
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
public class EasyIotOsgiUtil {
	public final static BundleContext context = FrameworkUtil.getBundle(EasyIotOsgiUtil.class).getBundleContext();

	/**
	 * Returns a service based on service class.
	 * 
	 * @param clazz
	 * @return
	 * @throws InterruptedException
	 */
	public static <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

	/**
	 * Returns a service based on service filter. filter should be in the form
	 * of LDAP filter e.g. "(factoryId=factoryId-1112)" means give me the
	 * service with factoryId property is equal to factoryId-1112
	 * 
	 * @param factory
	 * @return
	 * @throws InterruptedException
	 */
	public static <T> T getService(String serviceFilter) throws InterruptedException, InvalidSyntaxException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, context.createFilter(serviceFilter), null);
		st.open();
		return st.waitForService(1000);
	}

	/**
	 * Creates a factory configuration
	 * 
	 * @param propertiesMap
	 *            properties that corresponds to factory PID
	 * @param factoryPid
	 *            the name of the component that is listening to factory
	 *            configurations e.g. eg @Component(name="component.FacoryPid.name")
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void pushFactoryConfig(Map<String, String> propertiesMap, String factoryPid) throws IOException {
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

	/**
	 * Creates a single configuration
	 * 
	 * @param propertiesMap
	 *            properties that corresponds to pid
	 * @param pid
	 *            the name of the component that is listening to configuration eg @Component(name="component.pid.name")
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void pushConfig(Map<String, String> propertiesMap, String pid) throws IOException {
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
