package com.easyiot.base.security.provider.auth.conf;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.easyiot.base.security.provider.auth.AuthenticationTypeEnum;

@ObjectClassDefinition(name = "Authentication configuration", description = "Used to define the authentication type and the corresponding details.")
public @interface AuthenticationConfiguration {

	@AttributeDefinition(name = "Authentication type", description = "Used to identify the Authentication type e.g. configuration, LDAP, JDBC etc.", required = true)
	AuthenticationTypeEnum authenticationType() default AuthenticationTypeEnum.CONFIGURATION;

	@AttributeDefinition(name = "Users", description = "List of users. Use json array structure to define multiple permissions.", required = false)
	String users();

	@AttributeDefinition(name = "Groups", description = "List of groups. Use json array structure to define multiple permissions.", required = false)
	String groups();

	@AttributeDefinition(name = "Permissions", description = "List of permissions and corresponding groups. Use json array structure to define multiple permissions. Groups are are implemeted using OR condition. E.g. permission is valid to any user that is member of any groups defined per permission.", required = false)
	String permissions();

}
