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
		boolean deviceFound = false;
		if (_devices.size() == 0) {
			throw new NoSuchDeviceException(
					deviceId + " does not exists. Please check your application configuration.");
		}

		for (Device deviceService : _devices) {
			// Check if this is the service we are interested
			if (deviceId.equalsIgnoreCase(deviceService.getId())) {
				deviceFound = true;
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
							} catch (IllegalAccessException | IllegalArgumentException e) {
								throw new NoSuchMethodException(method.getName()
										+ " does not follow the framework rules. Please change the @GetMethod method signature.");
							} catch (InvocationTargetException e) {
								throw new NoSuchMethodException(
										"Method execution throws an exception with the following reason. "
												+ e.getCause().getMessage());
							}
						case POST:
							try {
								Class<?> methodParamType = method.getParameters()[0].getType();
								// If the method input type is not assignable to
								// methodParamType and input itself is already a
								// String then do the conversion to input object
								if (!input.getClass().isAssignableFrom(methodParamType) && input instanceof String) {
									// If the string does not have all teh
									// relevant fields then the default field
									// values from the DTO is used. e.g.
									// {"redValue":"LOW"} will convert to
									// {"redValue":"LOW", "blueValue":"HIGH",
									// "greenValue":"LOW"} for a DTO that has
									// default field values
									input = (I) dtoservice.decoder(methodParamType).get((String) input);
								}
								if (method.getReturnType().getName().equals("void")) {
									// if the device has void method then invoke
									// and return empty string.
									method.invoke(deviceService, input);
									return (O) "";
								} else {
									return (O) method.invoke(deviceService, input);
								}

							} catch (IllegalAccessException | IllegalArgumentException e) {
								throw new NoSuchMethodException(method.getName()
										+ " does not follow the framework rules. Please change the @PostMethod method signature.");
							} catch (InvocationTargetException e) {
								throw new NoSuchMethodException(
										"Method execution throws an exception with the following reason. "
												+ e.getCause().getMessage());
							} catch (Exception e) {
								throw new NoSuchMethodException(method.getName()
										+ " does not accept the string to input type conversion. Please check your input message.");
							}
						default:
							throw new NoSuchMethodException(
									methodType.toString() + " is not supported by DeviceExecuterService.");

						}
					}
				}
			}
		}
		if (deviceFound)

		{
			throw new NoSuchMethodException(methodType.toString() + " is not supported by " + deviceId);
		} else {
			throw new NoSuchDeviceException("Device: " + deviceId + " does not exists");
		}
	}

}
