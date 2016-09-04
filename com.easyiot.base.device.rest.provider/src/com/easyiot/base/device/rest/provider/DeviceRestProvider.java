package com.easyiot.base.device.rest.provider;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.easyiot.base.api.Device.DeviceExecutorMethodTypeEnum;
import com.easyiot.base.api.exception.NoSuchDeviceException;
import com.easyiot.base.capability.DeviceRest.ProvideDeviceRest_v1_0_0;
import com.easyiot.base.capability.WebSecurity.RequireWebSecurity;
import com.easyiot.base.executor.DeviceExecutorService;

/**
 * Maps all the device methods to rest api
 * 
 * @author daghan
 *
 */
@ProvideDeviceRest_v1_0_0
@RequireWebSecurity(versionStr="1.0.0")
@Component(service = Servlet.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=("
				+ HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=devicesContext)",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/devices" })
public class DeviceRestProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Reference
	private DeviceExecutorService rm;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object returnVal = null;
		String[] pathInfo = req.getPathInfo().split("/");
		if (pathInfo.length > 2) {
			resp.getWriter().println("url does not support level after " + pathInfo[1]);
		}
		try {
			returnVal = rm.activateResource(pathInfo[1], null, Object.class, DeviceExecutorMethodTypeEnum.GET);
		} catch (NoSuchMethodException me) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println(String.format("Could not execute GET method on the device %s." + pathInfo[1]));
			return;
		} catch (NoSuchDeviceException de) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println(String.format("Device id with %s cannot be found.", pathInfo[1]));
			return;
		}
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().println(returnVal.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		resp.getWriter().println("Current version cannot execute post methods on devices.");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		resp.getWriter().println("Current version cannot execute delete methods on devices.");
	}
}
