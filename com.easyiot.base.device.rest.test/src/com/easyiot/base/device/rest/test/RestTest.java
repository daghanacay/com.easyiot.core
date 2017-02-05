package com.easyiot.base.device.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import com.easyiot.base.api.Device;
import com.easyiot.base.capability.AuthenticationAuthorization.RequireAuthenticationAuthorization;
import com.easyiot.base.capability.DeviceRest.RequireDeviceRest;
import com.easyiot.base.test.util.IntegrationTestBase;

import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireDeviceRest
@RequireWebServerExtender
@RequireAuthenticationAuthorization
@RequireConfigurerExtender
public class RestTest extends IntegrationTestBase {

	@BeforeClass
	public static void prepareTestClass() throws IOException, InterruptedException {
		Map<String, String> config = new HashMap<>();
		config.put("org.apache.felix.http.enable", "true");
		config.put("org.osgi.service.http.port", "8080");
		config.put("org.apache.felix.https.enable", "false");
		config.put("org.osgi.service.http.port.secure", "8433");
		config.put("org.apache.felix.https.keystore", "configuration/ssl/keystore.jks");
		config.put("org.apache.felix.https.keystore.password", "da_iot");
		config.put("org.apache.felix.https.keystore.key.password", "da_iot");
		config.put("org.apache.felix.https.truststore", "configuration/ssl/cacerts.jks");
		config.put("org.apache.felix.https.truststore.type", "jks");

		pushConfig(config, "org.apache.felix.http");

		Map<String, String> securityConfig = new HashMap<>();
		securityConfig.put("authenticationType", "CONFIGURATION");
		securityConfig.put("users",
				"[{\"name\":\"daghan\",\"password\":\"daghan\"},{\"name\":\"pinar\",\"password\":\"pinar\"}]");
		securityConfig.put("groups",
				"[{\"name\":\"admin\",\"user_names\":[\"daghan\"]},{\"name\":\"authenticated\",\"user_names\":[\"daghan\",\"pinar\"]}]");
		securityConfig.put("permissions",
				"[{\"name\":\"readDevice\",\"group_names\":[\"admin\",\"authenticated\"]},{\"name\":\"writeDevice\",\"group_names\":[\"admin\"]}]");
		pushConfig(securityConfig, "com.easyiot.security");

		Device myService = new Device() {

			@Override
			public String getId() {
				return "testDevice";
			}

			@GetMethod
			public String getData() {
				return "testGetResultFromDevice";
			}

			@PostMethod
			public void postData(String data) {
				System.out.println(data);
			}
		};

		Device myTypedService = new Device() {

			@Override
			public String getId() {
				return "testDevice2";
			}

			@GetMethod
			public String getData() {
				return "testGetResultFromDevice";
			}

			@PostMethod
			public String postData(TestDeviceData data) {
				return data.toString();
			}
		};

		context.registerService(Device.class, myService, null);
		context.registerService(Device.class, myTypedService, null);
		
		Thread.sleep(100);
	}

	@Test
	public void testRestGet() throws Exception {
		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String encodedPass = new String(Base64.getEncoder().encode("daghan:daghan".getBytes()));
		String result = Request.Get("http://localhost:8080/easyiot/devices/testDevice")
				.addHeader("Authorization", "Basic " + encodedPass).execute().returnContent().asString();
		assertFalse(result.isEmpty());
	}

	@Test
	public void testRestPost() throws Exception {
		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String encodedPass = new String(Base64.getEncoder().encode("daghan:daghan".getBytes()));
		String result = Request.Post("http://localhost:8080/easyiot/devices/testDevice")
				.bodyString("{\"test\":\"testVal\"}", ContentType.APPLICATION_JSON)
				.addHeader("Authorization", "Basic " + encodedPass).execute().returnContent().asString();
		assertFalse(result.isEmpty());
	}

	@Test
	public void testRestPostForbidden() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String encodedPass = new String(Base64.getEncoder().encode("pinar:pinar".getBytes()));
		HttpResponse result = Request.Post("http://localhost:8080/easyiot/devices/testDevice")
				.bodyString("{\"test\":\"testVal\"}", ContentType.APPLICATION_JSON)
				.addHeader("Authorization", "Basic " + encodedPass).execute().returnResponse();
		assertEquals(403, result.getStatusLine().getStatusCode());
	}

	@Test
	public void testRestTypedPost() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String encodedPass = new String(Base64.getEncoder().encode("daghan:daghan".getBytes()));
		String result = Request.Post("http://localhost:8080/easyiot/devices/testDevice2")
				.bodyString("{\"name\":\"testVal\",\"id\":\"1\"}", ContentType.APPLICATION_JSON)
				.addHeader("Authorization", "Basic " + encodedPass).execute().returnContent().asString();
		assertEquals("{\"name\":\"testVal\", \"id\":1}\n", result.toString());
	}
}
