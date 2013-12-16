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

package com.argusat.gjl.devservice.test;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;

import com.argusat.gjl.devservice.Main;
import com.argusat.gjl.devservice.RegisterDeviceRequestProtobufReader;
import com.argusat.gjl.devservice.DeviceProtobufWriter;
import com.argusat.gjl.model.Device.OSType;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceResponse;
import com.argusat.gjl.service.observation.ObservationProtoBuf;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.MediaTypes;

public class MainTest extends TestCase {

	private RegisterDeviceRequest mRegisterDeviceRequest;

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
		cc.getClasses().add(RegisterDeviceRequestProtobufReader.class);
		cc.getClasses().add(DeviceProtobufWriter.class);

		Client c = Client.create(cc);

		r = c.resource(Main.BASE_URI);

		DeviceProtoBuf.Device.Builder deviceBuilder = DeviceProtoBuf.Device
				.newBuilder();
		deviceBuilder.setDeviceId("test-id-007");
		deviceBuilder.setOsType(DeviceProtoBuf.Device.OSType.GOOGLE_ANDROID);
		deviceBuilder.setOsVersion("4.1.1_r99");
		deviceBuilder.setPushToken("test_push_token");

		RegisterDeviceRequest.Builder builder = DeviceProtoBuf.RegisterDeviceRequest
				.newBuilder();
		builder.setDevice(deviceBuilder.build());

		mRegisterDeviceRequest = builder.build();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		httpServer.stop();
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	public void testDevices() {

		WebResource wr = r.path("devices");
		RegisterDeviceResponse response = wr.type("application/octet-stream")
				.post(DeviceProtoBuf.RegisterDeviceResponse.class,
						mRegisterDeviceRequest);

		assertEquals("OK", response);
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
