package com.easyiot.base.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Defines a part of the Iot Stack. By Iot <b>stack</b> we consider a
 * combination of at least one {@link Protocol}, at least one {@link Device} and
 * single {@link Application}. In practice {@link Application} contains front
 * end code and configuration that brings together the whole <b>stack</b>
 * together. It is important to emphasize that every <b>stack</b> can have
 * multiple instances of the same {@link Protocol} and multiple instances of
 * {@link Device}. Instances are created through configurations identified by
 * {@link StackComponent} and defined inside {@link Application} projects.
 * 
 * @author daghan
 *
 */
@ProviderType
public interface StackComponent {
	/**
	 * Name of this stack component. This id should be unique in all the
	 * components inside a stack and should identify the {@link StackComponent}
	 * at runtime.
	 * 
	 * @return
	 */
	String getId();
	
}

