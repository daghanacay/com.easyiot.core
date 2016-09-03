package com.easyiot.base.security.provider.auth;

import java.util.Dictionary;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

import osgi.enroute.configurer.api.RequireConfigurerExtender;

@RequireConfigurerExtender
@Component(name= "com.easyiot.security", immediate = true)
public class UserAdminConfigurator {
	@Reference
	private UserAdmin userAdmin;

	@SuppressWarnings("unchecked")
	@Activate
	public void initUserAdmin() {
		// Forming "Groups"
		Group authenticatedGroup = (Group) userAdmin.createRole("AuthenticatedGroup", Role.GROUP);
		Dictionary<String, String> groupProperties = (Dictionary<String, String>) authenticatedGroup.getProperties();
		groupProperties.put("Definition", "Defines an authenticated users");

		// Admin group
		Group adminGroup = (Group) userAdmin.createRole("AdminGroup", Role.GROUP);
		groupProperties = (Dictionary<String, String>) adminGroup.getProperties();
		groupProperties.put("Definition",
				"Defines Admin users which can make changes to configurations and read device data");

		// Device viewer group
		Group deviceGroup = (Group) userAdmin.createRole("DeviceGroup", Role.GROUP);
		groupProperties = (Dictionary<String, String>) deviceGroup.getProperties();
		groupProperties.put("Definition", "Defines Device users which can read device data");
		authenticatedGroup.addMember(adminGroup);
		authenticatedGroup.addMember(deviceGroup);
		// Adding User daghan
		User daghan = (User) userAdmin.createRole("Daghan", Role.USER);
		Dictionary<String, String> userProperties = (Dictionary<String, String>) daghan.getProperties();
		userProperties.put("Position", "Architect");
		userProperties.put("Company", "IoTforMasses");
		Dictionary<String, String> creds = (Dictionary<String, String>) daghan.getCredentials();
		creds.put("password", "da_iot");
		adminGroup.addMember(daghan);

		// Adding User pinar
		User pinar = (User) userAdmin.createRole("Pinar", Role.USER);
		userProperties = (Dictionary<String, String>) pinar.getProperties();
		userProperties.put("Position", "Lawyer");
		userProperties.put("Company", "Awesome Lawyers");
		creds = (Dictionary<String, String>) pinar.getCredentials();
		creds.put("password", "pa_law");
		deviceGroup.addMember(pinar);

	}

}
