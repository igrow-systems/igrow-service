/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)MainTest.java        
 *
 * Copyright (c) 2013 - 2014 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.argusat.gjl.locservice.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.internal.MediaTypes;

import com.argusat.gjl.locservice.Main;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionRequestProtobufReader;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionRequestProtobufWriter;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionResponseProtobufReader;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionResponseProtobufWriter;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;

public class MainTest extends TestCase {

	private BeginLocatorSessionRequest mBeginLocatorSessionRequest;

	private HttpServer httpServer;

	private WebTarget r;

	public MainTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the Grizzly2 web container
		httpServer = Main.startServer();

		ClientConfig cc = new ClientConfig();
		cc.register(BeginLocatorSessionRequestProtobufReader.class);
		cc.register(BeginLocatorSessionRequestProtobufWriter.class);
		cc.register(BeginLocatorSessionResponseProtobufReader.class);
		cc.register(BeginLocatorSessionResponseProtobufWriter.class);

		Client c = ClientBuilder.newClient(cc);

		r = c.target(Main.BASE_URI);

		{
			BeginLocatorSessionRequest.Builder builder = BeginLocatorSessionRequest
					.newBuilder();
			Location location = new Location();
			location.setLatitude(50.9399695f);
			location.setLongitude(-1.415058f);
			// the following need to be set
			// to form a valid Location but
			// the values are currently ignored
			// by the service.
			location.setAltitude(0.0f);
			location.setHDOP(0.0f);
			location.setVDOP(0.0f);
			builder.setLocation(location.getLocationProtoBuf());
			builder.setDeviceId("test-id-441");

			mBeginLocatorSessionRequest = builder.build();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		httpServer.shutdownNow();
	}

	/**
	 * Test to see that the /locatorsessions endpoint is available and
	 * processing BeginLocatorSession requests.
	 */
	public void testBeginLocatorSession() {

		BeginLocatorSessionRequest.Builder builder = BeginLocatorSessionRequest
				.newBuilder();
		builder.setDeviceId("test-id-009");

		WebTarget wr = r.path("locatorsessions");
		BeginLocatorSessionResponse response = wr.request(
				"application/octet-stream").post(
				Entity.entity(mBeginLocatorSessionRequest,
						"application/octet-stream"),
				BeginLocatorSessionResponse.class);

		assertEquals(BeginLocatorSessionResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test if a WADL document is available at the relative path
	 * "application.wadl".
	 */
	public void testApplicationWadl() {
		String serviceWadl = r.path("application.wadl")
				.request(MediaTypes.WADL).get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}
}
