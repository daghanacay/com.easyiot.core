package com.easyiot.base.configuration.admin.provider;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.easyiot.base.capability.ConfigurationManagement.ProvideConfigurationManagement_v1_0_0;

import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.rest.api.REST;
import osgi.enroute.rest.api.RESTRequest;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

/**
 * manages all the configurations in the OSGi container.
 */
@ProvideConfigurationManagement_v1_0_0
@RequireConfigurerExtender
@RequireWebServerExtender
@Component(name = "com.easyiot.base.configuration.admin", immediate = true, service = { AdminImpl.class, REST.class })
public class AdminImpl implements REST {
	private static final Configuration[] EMPTY = new Configuration[0];

	@Reference
	private ConfigurationAdmin cm;

	@Reference
	private EasyIotBundleTracker myBundleTracker;

	/**
	 * Returns all the factory configurations from EasyIotBundles.
	 * 
	 * http://localhost:8080/rest/RegisteredDevicesOrProtocols
	 * 
	 * @return
	 */
	public List<String> getRegisteredDevicesOrProtocols(RESTRequest rr) {
		return myBundleTracker.getIotFactoryConfigurations();
	}

	/**
	 * Given a factory configuration it returns all the existing child
	 * configurations.
	 * 
	 * http://localhost:8080/rest/DeviceOrProtocolInstances/{factoryPid}
	 * 
	 * @param factoryPid
	 * @return
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public List<String> getDeviceOrProtocolInstances(RESTRequest rr, String factoryPid)
			throws IOException, InvalidSyntaxException {
		return getConfigurations0(String.format("(service.pid=%s*)", factoryPid)).stream().map(conf -> conf.getPid())
				.collect(Collectors.toList());
	}

	/**
	 * Returns all the properties of a saved configuration.
	 * 
	 * http://localhost:8080/rest/DeviceOrProtocolInstanceProperties/{pid}
	 * 
	 * @param pid
	 * @return
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public Map<String, Object> getDeviceOrProtocolInstanceProperties(RESTRequest rr, String pid)
			throws IOException, InvalidSyntaxException {
		List<Configuration> confs = getConfigurations0(String.format("(service.pid=%s)", pid));
		return confs.size() == 1 ? toMap(confs.get(0).getProperties()) : null;
	}

	public interface PropertyRequest extends RESTRequest {
		Map<String, String> _body();
	}

	/**
	 * Update an existing configuration or create if does not exist.
	 * 
	 * PUT http://localhost:8080/rest/DeviceOrProtocolInstanceProperties/{pid}
	 * with a payload of {"prop1":"Sam3","prop1":"SAM3"}
	 * 
	 * @param pid
	 *            the (instance) PID of the configuration
	 * @param map
	 *            the set of properties
	 */

	public void putDeviceOrProtocolInstanceProperties(PropertyRequest request, String pid) throws IOException {
		Hashtable<String, String> temp = new Hashtable<String, String>();
		temp.putAll(request._body());

		cm.getConfiguration(pid, "?").update(temp);
	}

	/**
	 * Remove a configuration.
	 * 
	 * Delete http://localhost:8080/rest/DeviceOrProtocolInstance/{pid}
	 * 
	 * @param pid
	 *            the (instance) PID of a configuration
	 */
	public void deleteDeviceOrProtocolInstance(RESTRequest rr, String pid) throws IOException {
		cm.getConfiguration(pid, "?").delete();
	}

	/**
	 * Create a new instance configuration for a given factoryPid, returning the
	 * instance PID.
	 * 
	 * POST http://localhost:8080/rest/DeviceOrProtocolInstanceProperties/{
	 * factoryPid} with a payload of {"prop1":"Sam3","prop1":"SAM3"}
	 * 
	 * @param factoryPid
	 *            the factory PID
	 * @return the instance PID
	 */
	public String postDeviceOrProtocolInstance(PropertyRequest request, String factoryPid) throws IOException {
		// create the configuration instance
		String returnVal = cm.createFactoryConfiguration(factoryPid, "?").getPid();
		// update the configuration instance
		putDeviceOrProtocolInstanceProperties(request, returnVal);
		return returnVal;
	}

	/*
	 * Just handle the conversion from an array (potentially null) to a stream.
	 */
	private List<Configuration> getConfigurations0(String filter) throws IOException, InvalidSyntaxException {

		Configuration[] configurations = cm.listConfigurations(filter);
		if (configurations == null)
			configurations = EMPTY;

		return Arrays.asList(configurations);
	}

	/*
	 * A utility function to convert a dictonary to a map.
	 */
	private <K, V> Map<K, V> toMap(Dictionary<K, V> properties, Map<K, V> map) {
		if (properties != null) {
			for (Enumeration<K> e = properties.keys(); e.hasMoreElements();) {
				K key = e.nextElement();
				map.put(key, properties.get(key));
			}
		}
		return map;
	}

	/*
	 * A utility function to convert a dictonary to a map.
	 */
	private Map<String, Object> toMap(Dictionary<String, Object> properties) {
		return toMap(properties, new HashMap<>());
	}
}
