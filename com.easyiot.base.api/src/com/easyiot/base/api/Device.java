package com.easyiot.base.api;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Every device has a set of methods that it needs to provide. The methods of a
 * device implementation can be named in anyway. This allows the implementation
 * to provide its on naming scheme. Iot infrastructure only looks for the
 * methods that are annotated as one of the following annotations. The device
 * implementation class can provide one or more these methods. In case the
 * request is made on a device implementation and the corresponding method is
 * not found the {@link DeviceExecutor} throws {@link NoSuchMethodException}.
 * 
 * {@link GetMethod}: Annotates the implementation method when the
 * {@link DeviceExecutor} request GET operation on the implementation.
 * 
 * {@link PostMethod}: Annotates the implementation method when the
 * {@link DeviceExecutor} request POST operation on the implementation.
 * 
 * These annotations can change in the future versions of the framework.
 * 
 * It is required {@link Device} configurations should use
 * {@link StackComponentConfiguration} for creating their own configuration.
 * 
 * @author daghan
 *
 */
@ProviderType
public interface Device extends StackComponent {
	/**
	 * Associates a method type to an annotation. All these methods can be
	 * requested from a device. Implementing device should annotate the method
	 * to be called by the annotation defined in this enumeration.
	 * 
	 * @author daghan
	 *
	 */
	public enum DeviceExecutorMethodTypeEnum {
		// Call the GET method on the device.
		GET(GetMethod.class),
		// Call the POST method on the device.
		POST(PostMethod.class);

		private Class<? extends Annotation> annotation;

		DeviceExecutorMethodTypeEnum(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

		public Class<? extends Annotation> getAnnotation() {
			return annotation;
		}

	}

	/**
	 * Provides the get method type. The method will be called when the GET
	 * request is called on the device by the {@link DeviceExecutor}. Semantics
	 * of get method is defined by the w3
	 * <a>https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html</a> and the
	 * implementation should obey the implementation semantics. The annotated
	 * method should be public and should not have any input parameters.
	 * 
	 * @author daghan
	 *
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface GetMethod {
	}

	/**
	 * Provides the post method type. The method will be called when the POST
	 * request is called on the device by the {@link DeviceExecutor}. Semantics
	 * of get method is defined by the w3
	 * <a>https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html</a> and the
	 * implementation should obey the implementation semantics. The annotated
	 * method should be public and should have a single input parameter.
	 * Implementation method should not modify the input parameter.
	 * 
	 * @author daghan
	 *
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PostMethod {
	}

}
