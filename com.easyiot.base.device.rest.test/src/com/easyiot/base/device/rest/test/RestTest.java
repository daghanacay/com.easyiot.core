package com.easyiot.base.device.rest.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

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
	}

	@Test
	public void testRestGet() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String result = Request.Get("http://localhost:8080/easyiot/devices/dana").execute().returnContent().asString();
		assertFalse(result.isEmpty());
	}

	@Test
	public void testRestPost() throws Exception {

		Servlet restService = getService(Servlet.class);
		assertNotNull(restService);
		String result = Request.Post("http://localhost:8080/easyiot/devices/dana")
				.bodyString("{\"test\":\"testVal\"}", ContentType.APPLICATION_JSON).execute().returnContent()
				.asString();
		assertFalse(result.isEmpty());
	}
}
