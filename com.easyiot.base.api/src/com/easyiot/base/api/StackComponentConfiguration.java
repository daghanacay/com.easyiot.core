package com.easyiot.base.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Basic {@link StackComponent} configuration. All the {@link Protocol},
 * {@link Device}, {@link Application} configurations should use this annotation
 * as meta-annotation for proper functioning of the system.
 * 
 * @author daghan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface StackComponentConfiguration {
	public String id();
}
