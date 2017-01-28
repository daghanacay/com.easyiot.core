package com.easyiot.base.configuration.admin.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Admin Configuration")
public @interface AdminConfig {
	
	@AttributeDefinition(name = "Instance ID", description = "GPIO protocol instance ID", required = true)
	public String id() default "rasberry.pi";

}
