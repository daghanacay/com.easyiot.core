package com.easyiot.base.configuration.admin.provider.command;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.base.configuration.admin.config.AdminConfig;
import com.easyiot.base.configuration.admin.provider.AdminImpl;
import com.easyiot.base.configuration.admin.provider.AdminImpl.PropertyRequest;

import osgi.enroute.debug.api.Debug;

@Designate(ocd = AdminConfig.class, factory = true)
@Component(name = "config.command.component", service = ConfigurationServiceCommand.class, immediate = true, property = {
		Debug.COMMAND_SCOPE + "=conf", Debug.COMMAND_FUNCTION + "=getRegisteredDevicesOrProtocols",
		Debug.COMMAND_FUNCTION + "=getRegisteredDevicesOrProtocolsMetaData",
		Debug.COMMAND_FUNCTION + "=getDeviceOrProtocolInstances",
		Debug.COMMAND_FUNCTION + "=getDeviceOrProtocolInstanceProperties",
		Debug.COMMAND_FUNCTION + "=getDeviceOrProtocolInstancePropertiesMeta",
		Debug.COMMAND_FUNCTION + "=updateDeviceOrProtocolInstanceProperties",
		Debug.COMMAND_FUNCTION + "=deleteDeviceOrProtocolInstance",
		Debug.COMMAND_FUNCTION + "=createDeviceOrProtocolInstance" })
public class ConfigurationServiceCommand {
	@Reference
	private AdminImpl adminService;

	/**
	 * returns all the easyiot configurations.
	 * 
	 * usage: g! getRegisteredDevicesAndProtocols
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	public void getRegisteredDevicesOrProtocols() throws IOException, InvalidSyntaxException {
		adminService.getRegisteredDevicesOrProtocols(null).forEach(System.out::println);
	}

	/**
	 * returns all the easyiot configurations metatype information.
	 * 
	 * usage: g! getRegisteredDevicesOrProtocolsProperties test.factory.pid
	 * 
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	public void getRegisteredDevicesOrProtocolsMetaData(String deviceOrProtocolPid)
			throws IOException, InvalidSyntaxException {
		System.out.println(adminService.getRegisteredDevicesOrProtocolsMetaData(null, deviceOrProtocolPid));
	}

	/**
	 * Get the existing instances that have been created through configuration
	 * 
	 * usage: g! getDeviceOrProtocolInstances test.factory.pid
	 * 
	 * @param factoryPid
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public void getDeviceOrProtocolInstances(String deviceOrProtocolPid) throws IOException, InvalidSyntaxException {
		adminService.getDeviceOrProtocolInstances(null, deviceOrProtocolPid).forEach(System.out::println);
	}

	/**
	 * get the properties of device on portocol that are created through
	 * configuration.
	 * 
	 * usage: g! getDeviceOrProtocolInstanceProperties
	 * test.factory.pid.a9d13487-83da-45d1-a282-aa76515edfa8
	 * 
	 * @param pid
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public void getDeviceOrProtocolInstanceProperties(String deviceOrProtocolPid)
			throws IOException, InvalidSyntaxException {
		System.out.println(adminService.getDeviceOrProtocolInstanceProperties(null, deviceOrProtocolPid));
	}

	/**
	 * get the properties of device on portocol that are created through
	 * configuration.
	 * 
	 * usage: g! getDeviceOrProtocolInstanceProperties
	 * test.factory.pid.a9d13487-83da-45d1-a282-aa76515edfa8
	 * 
	 * @param pid
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public void getDeviceOrProtocolInstancePropertiesMeta(String deviceOrProtocolPid)
			throws IOException, InvalidSyntaxException {
		System.out.println(adminService.getDeviceOrProtocolInstancePropertiesMeta(null, deviceOrProtocolPid));
	}

	/**
	 * Usage updateDeviceOrProtocolInstanceProperties {configurationName}
	 * {mapInput}
	 * 
	 * updateDeviceOrProtocolInstanceProperties daghan [prop1=1 prop2=test]
	 * 
	 * 
	 * @param pid
	 * @param properties
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public void updateDeviceOrProtocolInstanceProperties(String pid, Map<String, String> properties)
			throws IOException, InvalidSyntaxException {
		adminService.putDeviceOrProtocolInstanceProperties(getRequest(properties), pid);
		getDeviceOrProtocolInstanceProperties(pid);
	}

	private PropertyRequest getRequest(Map<String, String> properties) {
		PropertyRequest temp = new PropertyRequest() {

			@Override
			public HttpServletResponse _response() {
				return null;
			}

			@Override
			public HttpServletRequest _request() {
				return null;
			}

			@Override
			public String _host() {
				return null;
			}

			@Override
			public Map<String, String> _body() {
				return properties;
			}
		};
		return temp;
	}

	/**
	 * Deletes an existing protocol or device.
	 * 
	 * Usage: deleteDeviceOrProtocolInstance {pid}
	 * 
	 * @param pid
	 * @throws IOException
	 */
	public void deleteDeviceOrProtocolInstance(String pid) throws IOException {
		adminService.deleteDeviceOrProtocolInstance(null, pid);
	}

	/**
	 * Creates an instance of a device or protocol.
	 * 
	 * Usage createDeviceOrProtocolInstance {factoryPid} [prop1=1 prop2=test]
	 * 
	 * @param factoryPid
	 * @param properties
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public void createDeviceOrProtocolInstance(String factoryPid, Map<String, String> properties)
			throws IOException, InvalidSyntaxException {
		String confName = adminService.postDeviceOrProtocolInstance(getRequest(properties), factoryPid);
		updateDeviceOrProtocolInstanceProperties(confName, properties);
	}

}
