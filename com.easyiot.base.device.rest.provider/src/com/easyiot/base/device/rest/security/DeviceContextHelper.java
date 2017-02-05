package com.easyiot.base.device.rest.security;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * Handles the security for devices endpoint
 * 
 * @author daghan
 *
 */
@Component(service = { ServletContextHelper.class, EventHandler.class }, property = {
		EventConstants.EVENT_TOPIC + "=org/osgi/framework/ServiceEvent/*",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=devicesContext",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=/easyiot" })
public class DeviceContextHelper extends ServletContextHelper implements EventHandler {
	@Reference
	private UserAdmin userAdmin;

	private AtomicBoolean secureServiceAvailable = new AtomicBoolean(false);

	private User user;

	@Override
	public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (secureServiceAvailable.get()) {
			// i.e. Basic authentication
			if (request.getHeader("Authorization") == null) {
				response.addHeader("WWW-Authenticate", "Basic");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			// Check if authenticated
			if (authenticated(request, response)) {
				if (authorized(request, response)) {
					return true;
				} else {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
			} else {
				response.addHeader("WWW-Authenticate", "Basic");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
		} else {
			return true;
		}

	}

	private boolean authorized(HttpServletRequest request, HttpServletResponse response) {
		if (request.getMethod().equalsIgnoreCase("GET") && userAdmin.getAuthorization(user).hasRole("readDevice")) {
			return true;
		} else if (request.getMethod().equalsIgnoreCase("POST")
				&& userAdmin.getAuthorization(user).hasRole("writeDevice")) {
			return true;
		}
		return false;
	}

	protected boolean authenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authzHeader = request.getHeader("Authorization");
		String usernameAndPassword = new String(Base64.getDecoder().decode(authzHeader.substring(6)));

		int userNameIndex = usernameAndPassword.indexOf(":");
		String username = usernameAndPassword.substring(0, userNameIndex);
		String password = usernameAndPassword.substring(userNameIndex + 1);

		// Check if the password matches
		user = (User) userAdmin.getRole(username);
		if (user == null) {
			response.addHeader("WWW-Authenticate", "Basic");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return ((String) user.getCredentials().get("password")).equals(password);

	}

	@Override
	public void handleEvent(Event event) {
		if (((String[]) event.getProperty("service.objectClass"))[0]
				.equals("com.easyiot.base.security.provider.auth.UserAdminConfigurator")) {
			if (event.getTopic().endsWith("REGISTERED")) {
				this.secureServiceAvailable.set(true);
			} else {
				this.secureServiceAvailable.set(false);
			}
		}

	}
}
