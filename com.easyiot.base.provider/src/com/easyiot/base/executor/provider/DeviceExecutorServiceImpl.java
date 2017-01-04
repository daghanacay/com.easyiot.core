package com.easyiot.base.executor.provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.easyiot.base.api.Device;
import com.easyiot.base.api.Device.DeviceExecutorMethodTypeEnum;
import com.easyiot.base.api.exception.NoSuchDeviceException;
import com.easyiot.base.executor.DeviceExecutorService;

import osgi.enroute.dto.api.DTOs;

@Component(name = "com.easyiot.base.executor")
public class DeviceExecutorServiceImpl implements DeviceExecutorService {
	/**
	 * NOTE:
	 * 
	 * policy = ReferencePolicy.DYNAMIC : Because we want to be notified for
	 * each service entered to the system, we use "volatile" to indicate dynamic
	 * 
	 * @param properties
	 */
	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private volatile List<Device> _devices;

	@Reference
	private DTOs dtoservice;

	@SuppressWarnings("unchecked")
	@Override
	public <O, I> O activateResource(String deviceId, I input, Class<O> outputType,
			DeviceExecutorMethodTypeEnum methodType) throws NoSuchMethodException, NoSuchDeviceException {

		if (_devices.size() == 0) {
			throw new NoSuchDeviceException(deviceId);
		}
		for (Device deviceService : _devices) {
			// Check if this is the service we are interested
			if (deviceId.equalsIgnoreCase(deviceService.getId())) {
				for (Method method : deviceService.getClass().getDeclaredMethods()) {
					Object providerAnnotation;
					providerAnnotation = method.getAnnotation(methodType.getAnnotation());
					// We only allow zero or one parameter methods
					if (providerAnnotation != null && method.getParameters().length <= 1
							&& (method.getReturnType().equals(Void.TYPE)
									|| outputType.isAssignableFrom(method.getReturnType()))) {
						// Allow calling even private, protected, or no
						// modifier
						method.setAccessible(true);
						switch (methodType) {
						case GET:
							// we found the method lets run it
							try {
								return (O) method.invoke(deviceService);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								throw new NoSuchMethodException(method.getName());
							}
						case POST:
							try {
								Class<?> methodParamType = method.getParameters()[0].getType();
								// If the method input type is not assignable to
								// methodParamType and input itself is already a
								// String then do the conversion to input object
								if (!input.getClass().isAssignableFrom(methodParamType) && input instanceof String) {
									input = (I) dtoservice.decoder(methodParamType).get((String) input);
								}
								if (method.getReturnType().getName().equals("void")) {
									// if the 
									return (O) "";
								} else {
									return (O) method.invoke(deviceService, input);
								}

							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								throw new NoSuchMethodException(method.getName());
							} catch (Exception e) {
								throw new NoSuchMethodException(method.getName()
										+ "does not accept the string to input type converion. Please check your input message.");
							}
						default:
							throw new NoSuchMethodException(
									methodType.toString() + " is not supported in this version.");

						}
					}
				}
			}
		}

		throw new NoSuchMethodException("No device method is annotated with " + methodType);
	}

}
