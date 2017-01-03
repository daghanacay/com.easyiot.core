package com.easyiot.base.device.rest.test;

import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.http.client.fluent.Request;
import org.junit.Test;

import com.easyiot.base.capability.DeviceRest.RequireDeviceRest;
import com.easyiot.base.test.util.IntegrationTestBase;

import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireDeviceRest
public class RestTest extends IntegrationTestBase {

	@Test
	public void testRest() throws Exception {
		Map<String,String> config = new HashMap<>();
		config.put("org.apache.felix.http.enable","true");
		config.put("org.osgi.service.http.port","8080");
		config.put("org.apache.felix.https.enable","false");
		config.put("org.osgi.service.http.port.secure","8433");
		config.put("org.apache.felix.https.keystore","configuration/ssl/keystore.jks");
		config.put("org.apache.felix.https.keystore.password","da_iot");
		config.put("org.apache.felix.https.keystore.key.password","da_iot");
		config.put("org.apache.felix.https.truststore","configuration/ssl/cacerts.jks");
		config.put("org.apache.felix.https.truststore.type","jks");
		
		pushConfig(config, "org.apache.felix.http");
		Servlet restService = getService(Servlet.class);
		String result = Request.Get("http://localhost:8080/easyiot/devices").execute().returnContent().asString();
		assertFalse(result.isEmpty());
	}
}
