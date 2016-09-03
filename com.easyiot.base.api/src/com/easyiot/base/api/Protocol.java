package com.easyiot.base.api;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Defines a protocol that can be used by different devices. Protocols are
 * configured per Iot {@link Application}. Protocols might have their own
 * configuration that should be well documented to be used by the users of the
 * protocols. Protocols can be injected into {@link Device} through using the
 * name assigned to the particular protocol through configuration.
 * 
 * It is required {@link Protocol} configurations should use
 * {@link StackComponentConfiguration} for creating their own configuration.
 * 
 * @author daghan
 *
 */
@ProviderType
public interface Protocol extends StackComponent { 

}
