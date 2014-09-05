/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)MainTest.java        
 *
 * Copyright (c) 2014 Argusat Limited
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
import org.junit.Test;

import com.argusat.gjl.devservice.Main;
import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufWriter;
import com.argusat.gjl.devservice.providers.RegisterDeviceRequestProtobufReader;
import com.argusat.gjl.devservice.providers.RegisterDeviceRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.RegisterDeviceResponseProtobufReader;
import com.argusat.gjl.devservice.providers.RegisterDeviceResponseProtobufWriter;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.FindLocalDevicesRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.FindLocalDevicesResponse;
import com.argusat.gjl.service.device.DeviceProtoBuf.NotifyDeviceRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.NotifyDeviceResponse;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.MediaTypes;

public class MainTest extends TestCase {

	private RegisterDeviceRequest mRegisterDeviceRequest;

	private NotifyDeviceRequest mNotifyDeviceRequest;
	
	private FindLocalDevicesRequest mFindLocalDevicesRequest;

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
		cc.getClasses().add(RegisterDeviceRequestProtobufWriter.class);
		cc.getClasses().add(RegisterDeviceResponseProtobufReader.class);
		cc.getClasses().add(RegisterDeviceResponseProtobufWriter.class);
		cc.getClasses().add(NotifyDeviceRequestProtobufReader.class);
		cc.getClasses().add(NotifyDeviceRequestProtobufWriter.class);
		cc.getClasses().add(NotifyDeviceResponseProtobufReader.class);
		cc.getClasses().add(NotifyDeviceResponseProtobufWriter.class);
		cc.getClasses().add(FindLocalDevicesRequestProtobufReader.class);
		cc.getClasses().add(FindLocalDevicesRequestProtobufWriter.class);
		cc.getClasses().add(FindLocalDevicesResponseProtobufReader.class);
		cc.getClasses().add(FindLocalDevicesResponseProtobufWriter.class);

		Client c = Client.create(cc);

		r = c.resource(Main.BASE_URI);

		{
			DeviceProtoBuf.Device.Builder deviceBuilder = DeviceProtoBuf.Device
					.newBuilder();
			deviceBuilder.setDeviceId("test-id-010");
			deviceBuilder
					.setOsType(DeviceProtoBuf.Device.OSType.GOOGLE_ANDROID);
			deviceBuilder.setOsVersion("4.1.1_r99");
			deviceBuilder.setPushToken("test_push_token");
			deviceBuilder.setManufacturer("HUAWEI");
			deviceBuilder.setModel("HUAWEI G510-0100");
			deviceBuilder.setProduct("G510-0100");
			deviceBuilder.setDevice("hwG510-0100");
			
			RegisterDeviceRequest.Builder builder = DeviceProtoBuf.RegisterDeviceRequest
					.newBuilder();
			builder.setDevice(deviceBuilder.build());

			mRegisterDeviceRequest = builder.build();
		}
		
		{
			DeviceProtoBuf.FindLocalDevicesRequest.Builder builder = DeviceProtoBuf.FindLocalDevicesRequest.newBuilder();
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
			builder.setCentre(location.getLocationProtoBuf());
			builder.setRadius(1000L);
			builder.setLimit(5);
			
			mFindLocalDevicesRequest = builder.build();
		}

		{
			NotifyDeviceRequest.Builder builder = DeviceProtoBuf.NotifyDeviceRequest
					.newBuilder();
			builder.setDeviceId("b024ac7008bb0b69");
			builder.setMessage("{\"msg_type\":\"begin_locator_session_request\" }");

			mNotifyDeviceRequest = builder.build();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		httpServer.stop();
	}

	/**
	 * Test to see that a device is correctly registered
	 */
	@Test
	public void testRegisterDevice() {

		WebResource wr = r.path("devices");
		RegisterDeviceResponse response = wr.type("application/octet-stream")
				.post(DeviceProtoBuf.RegisterDeviceResponse.class,
						mRegisterDeviceRequest);

		assertEquals(RegisterDeviceResponse.ErrorCode.NONE,
				response.getResponseCode());
	}
	
	/**
	 * Test to see that local devices can be found
	 */
	@Test
	public void testFindLocalDevices() {

		WebResource wr = r.path("devices/local");
		FindLocalDevicesResponse response = wr.type("application/octet-stream")
				.post(DeviceProtoBuf.FindLocalDevicesResponse.class,
						mFindLocalDevicesRequest);

		assertEquals(FindLocalDevicesResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test to see that a device can be notified
	 */
	@Test
	public void testNotifyDevice() {

		WebResource wr = r.path("notifications");
		NotifyDeviceResponse response = wr.type("application/octet-stream")
				.post(DeviceProtoBuf.NotifyDeviceResponse.class,
						mNotifyDeviceRequest);

		assertEquals(NotifyDeviceResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test if a WADL document is available at the relative path
	 * "application.wadl".
	 */
	@Test
	public void testApplicationWadl() {
		String serviceWadl = r.path("application.wadl").accept(MediaTypes.WADL)
				.get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}
}
