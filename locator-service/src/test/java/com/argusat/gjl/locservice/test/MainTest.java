/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)MainTest.java        
 *
 * Copyright (c) 2013 Argusat Limited
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

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;

import com.argusat.gjl.locservice.Main;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionRequestProtobufReader;
import com.argusat.gjl.locservice.provider.BeginLocatorSessionRequestProtobufWriter;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.MediaTypes;

public class MainTest extends TestCase {

	private BeginLocatorSessionRequest mBeginLocatorSessionRequest;

	private HttpServer httpServer;

	private WebResource r;

	public MainTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the Grizzly2 web container
		httpServer = Main.startServer();

		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(BeginLocatorSessionRequestProtobufReader.class);
		cc.getClasses().add(BeginLocatorSessionRequestProtobufWriter.class);

		Client c = Client.create(cc);

		r = c.resource(Main.BASE_URI);

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

		httpServer.stop();
	}

	/**
	 * Test to see that the /locatorsessions endpoint is available and
	 * processing BeginLocatorSession requests.
	 */
	public void testBeginLocatorSession() {

		BeginLocatorSessionRequest.Builder builder = BeginLocatorSessionRequest
				.newBuilder();
		builder.setDeviceId("test-id-009");

		WebResource wr = r.path("locatorsessions");
		BeginLocatorSessionResponse response = wr.type(
				"application/octet-stream").post(
				BeginLocatorSessionResponse.class, mBeginLocatorSessionRequest);

		assertEquals(BeginLocatorSessionResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test if a WADL document is available at the relative path
	 * "application.wadl".
	 */
	public void testApplicationWadl() {
		String serviceWadl = r.path("application.wadl").accept(MediaTypes.WADL)
				.get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}
}
