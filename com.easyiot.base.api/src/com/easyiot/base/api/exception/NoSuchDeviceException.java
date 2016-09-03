package com.easyiot.base.api.exception;

/**
 * Thrown when a device with a given name is not available.
 * 
 * @author daghan
 *
 */
public class NoSuchDeviceException extends FrameworkException {
	private static final long serialVersionUID = 1L;
	private String deviceName;

	public NoSuchDeviceException(String deviceName) {
		super(deviceName+ " is not found." );
		this.deviceName = deviceName;
	}

	/**
	 * Use to get the device that is missing.
	 * 
	 * @return the name of the missing device
	 */
	public String getDeviceName() {
		return this.deviceName;
	}
}
