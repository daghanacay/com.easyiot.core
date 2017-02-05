package com.easyiot.base.security.provider.auth;

import java.util.Dictionary;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

import com.easyiot.base.capability.AuthenticationAuthorization.ProvideAuthenticationAuthorization;
import com.easyiot.base.security.provider.auth.conf.AuthenticationConfiguration;
import com.easyiot.base.security.provider.dto.GroupDto;
import com.easyiot.base.security.provider.dto.PermissionDto;
import com.easyiot.base.security.provider.dto.UserDto;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.dto.api.TypeReference;

@ProvideAuthenticationAuthorization(version = "1.0.0")
@Designate(ocd = AuthenticationConfiguration.class)
@Component(service = {
		UserAdminConfigurator.class }, name = "com.easyiot.security", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class UserAdminConfigurator {
	private AuthenticationConfiguration config;

	@Reference
	private UserAdmin userAdmin;

	@Reference
	private DTOs dtoService;

	@Deactivate
	public void removeAuthentication() {
	}

	@Activate
	public void initUserAdmin(AuthenticationConfiguration config) {
		this.config = config;
		generateAuthentication();
	}

	private void generateAuthentication() {

		if (config.authenticationType() == AuthenticationTypeEnum.CONFIGURATION) {
			try {
				// deserialize all the configurations
				TypeReference<List<UserDto>> uTr = new TypeReference<List<UserDto>>() {
				};

				List<UserDto> users = dtoService.decoder(uTr).get(this.config.users());

				TypeReference<List<GroupDto>> gTr = new TypeReference<List<GroupDto>>() {
				};
				List<GroupDto> groups = dtoService.decoder(gTr).get(this.config.groups());
				TypeReference<List<PermissionDto>> pTr = new TypeReference<List<PermissionDto>>() {
				};
				List<PermissionDto> permissions = dtoService.decoder(pTr).get(this.config.permissions());

				// Forming users
				users.stream().forEach(user -> {
					User tempUser = (User) userAdmin.createRole(user.name, Role.USER);
					Dictionary<String, String> creds = (Dictionary<String, String>) tempUser.getCredentials();
					creds.put("password", user.password);
				});

				// Forming "Groups"
				groups.stream().forEach(group -> {
					Group tempGroup = (Group) userAdmin.createRole(group.name, Role.GROUP);
					group.user_names.stream().forEach(user_name -> tempGroup.addMember(userAdmin.getRole(user_name)));
				});

				// Forming "Permissions"
				permissions.stream().forEach(permission -> {
					Group tempPermision = (Group) userAdmin.createRole(permission.name, Role.GROUP);
					permission.group_names.stream()
							.forEach(group_name -> tempPermision.addMember(userAdmin.getRole(group_name)));
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Only Configuration type authentication is implemented");
		}

	}

}
