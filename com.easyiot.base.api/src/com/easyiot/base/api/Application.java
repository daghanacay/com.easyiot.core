package com.easyiot.base.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Application defines an application that controls the underlying stack. It can
 * provide its own configuration.
 * 
 * It is required {@link Application} configurations should use
 * {@link StackComponentConfiguration} for creating their own configuration.
 * 
 * @author daghan
 *
 */
@ProviderType
public interface Application extends StackComponent {
}
