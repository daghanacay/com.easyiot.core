package com.easyiot.base.executor;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Device;
import com.easyiot.base.api.Device.DeviceExecutorMethodTypeEnum;
import com.easyiot.base.api.exception.NoSuchDeviceException;

/**
 * A service that executes one of the {@link DeviceExecutorMethodTypeEnum}
 * methods on a {@link Device} registered under a name. It can be parameterized
 * based on the expected input and output type of the device method.
 * 
 * @author daghan
 *
 */
@ProviderType
public interface DeviceExecutorService {

	/**
	 * The service that executed the methods on an implementation device. For
	 * example if we have an device implementation as follows
	 * 
	 * public MyDevice implements Device { ...
	 * 
	 * @PostMethod public OutputObject getImplementation(InputObject input){
	 *             ....}
	 * 
	 *             then the client can access this method through
	 *             {@link DeviceExecutorService} as follows
	 * 
	 *             OutputObject result = deviceExecutor.
	 *             <InputObject,OutputObject>activateResource("myDeviceId", new
	 *             InputObject(),
	 *             OutputObject.class,DeviceExecutorMethodTypeEnum.POST)
	 * 
	 * @param requestServiceId
	 *            The unique ID if the device to be called
	 * @param input
	 *            input type of the underlying implementation. It is recommended
	 *            that the input object is immutable.
	 * @param outputType
	 *            Output type of the underlying implementation
	 * @param methodType
	 * @return
	 * @throws NoSuchMethodException
	 *             If the device is found but it does not expose a method with
	 *             the requested method then {@link NoSuchMethodException} is
	 *             thrown
	 * @throws NoSuchDeviceException
	 *             If the {@link Device} with the given name cannot be found
	 *             then {@link NoSuchDeviceException} is thrown.
	 */
	public <O, I> O activateResource(String deviceId, I input, Class<O> outputType,
			DeviceExecutorMethodTypeEnum methodType) throws NoSuchMethodException, NoSuchDeviceException;

}
