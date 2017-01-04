package com.easyiot.base.device.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.easyiot.base.api.Device;
import com.easyiot.base.capability.DeviceRest.RequireDeviceRest;
import com.easyiot.base.test.util.IntegrationTestBase;

@RequireDeviceRest
public class RestTest extends IntegrationTestBase {

	@BeforeClass
	public static void prepareTestClass() throws IOException {
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
	}

	// we need to modify DeviceContextHelper class in
	// com.easyiot.base.security.provider project for these test to pass
	@Test
	@Ignore
	public void testRestGet() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String result = Request.Get("http://localhost:8080/easyiot/devices/testDevice").execute().returnContent()
				.asString();
		assertFalse(result.isEmpty());
	}

	// we need to modify DeviceContextHelper class in
	// com.easyiot.base.security.provider project for these test to pass
	@Test
	@Ignore
	public void testRestPost() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String result = Request.Post("http://localhost:8080/easyiot/devices/testDevice")
				.bodyString("{\"test\":\"testVal\"}", ContentType.APPLICATION_JSON).execute().returnContent()
				.asString();
		assertFalse(result.isEmpty());
	}

	// we need to modify DeviceContextHelper class in
	// com.easyiot.base.security.provider project for these test to pass
	@Test
	@Ignore
	public void testRestTypedPost() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String result = Request.Post("http://localhost:8080/easyiot/devices/testDevice2")
				.bodyString("{\"name\":\"testVal\",\"id\":\"1\"}", ContentType.APPLICATION_JSON).execute()
				.returnContent().asString();
		assertEquals("{\"name\":\"testVal\", \"id\":1}\n", result.toString());
	}
}
