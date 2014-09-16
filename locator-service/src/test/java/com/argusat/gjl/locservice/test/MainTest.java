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
import com.argusat.gjl.locservice.provider.EndLocatorSessionRequestProtobufReader;
import com.argusat.gjl.locservice.provider.EndLocatorSessionRequestProtobufWriter;
import com.argusat.gjl.locservice.provider.EndLocatorSessionResponseProtobufReader;
import com.argusat.gjl.locservice.provider.EndLocatorSessionResponseProtobufWriter;
import com.argusat.gjl.locservice.provider.JoinLocatorSessionRequestProtobufReader;
import com.argusat.gjl.locservice.provider.JoinLocatorSessionRequestProtobufWriter;
import com.argusat.gjl.locservice.provider.JoinLocatorSessionResponseProtobufReader;
import com.argusat.gjl.locservice.provider.JoinLocatorSessionResponseProtobufWriter;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.JoinLocatorSessionRequest;

public class MainTest extends TestCase {

	private BeginLocatorSessionRequest mBeginLocatorSessionRequest;

	private HttpServer deviceHttpServer;
	
	private HttpServer httpServer;

	private WebTarget r;

	public MainTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the device/notifications server
		deviceHttpServer = com.argusat.gjl.devservice.Main.startServer();
		
		// start the Grizzly2 web container
		httpServer = Main.startServer();

		ClientConfig cc = new ClientConfig();
		cc.register(BeginLocatorSessionRequestProtobufReader.class);
		cc.register(BeginLocatorSessionRequestProtobufWriter.class);
		cc.register(BeginLocatorSessionResponseProtobufReader.class);
		cc.register(BeginLocatorSessionResponseProtobufWriter.class);
		cc.register(JoinLocatorSessionRequestProtobufReader.class);
		cc.register(JoinLocatorSessionRequestProtobufWriter.class);
		cc.register(JoinLocatorSessionResponseProtobufReader.class);
		cc.register(JoinLocatorSessionResponseProtobufWriter.class);
		cc.register(EndLocatorSessionRequestProtobufReader.class);
		cc.register(EndLocatorSessionRequestProtobufWriter.class);
		cc.register(EndLocatorSessionResponseProtobufReader.class);
		cc.register(EndLocatorSessionResponseProtobufWriter.class);

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
		
		deviceHttpServer.shutdownNow();
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
		assertTrue(response.hasSessionId());
	}
	
	/**
	 * Test to see that the /locatorsessions endpoint is available and
	 * processing JoinLocatorSession requests.
	 */
	public void testJoinLocatorSession() {

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
		
		//String sessionId = response.getSessionId();
		
		JoinLocatorSessionRequest.Builder joinBuilder = JoinLocatorSessionRequest
				.newBuilder();
		joinBuilder.setDeviceId("test-id-010");
		

		wr = r.path("locatorsessions/join");
		BeginLocatorSessionResponse joinResponse = wr.request(
				"application/octet-stream").post(
				Entity.entity(joinBuilder.build(),
						"application/octet-stream"),
				BeginLocatorSessionResponse.class);

		assertEquals(BeginLocatorSessionResponse.ErrorCode.NONE,
				joinResponse.getResponseCode());
	}
	
	/**
	 * Test to see that the /locatorsessions endpoint is available and
	 * processing BeginLocatorSession requests.
	 */
	public void testEndLocatorSession() {

		// first create a session (call the other test?)
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
		assertTrue(response.hasSessionId());
		
		// then delete it.
		
		EndLocatorSessionRequest.Builder endSessionBuilder = EndLocatorSessionRequest
				.newBuilder();
		endSessionBuilder.setDeviceId("test-id-009");

		wr = r.path(String.format("locatorsessions/%s", response.getSessionId()));
		BeginLocatorSessionResponse endSessionResponse = wr.request(
				"application/octet-stream").delete(BeginLocatorSessionResponse.class);

		assertEquals(BeginLocatorSessionResponse.ErrorCode.NONE,
				endSessionResponse.getResponseCode());
		//assertTrue(endSessionResponse.hasSessionId());
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
