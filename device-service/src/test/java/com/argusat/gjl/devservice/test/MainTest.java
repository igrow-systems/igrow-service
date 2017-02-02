/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)MainTest.java        
 *
 * Copyright (c) 2014, 2017 Argusat Limited
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
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
import com.sun.jersey.core.header.MediaTypes;

public class MainTest extends TestCase {

	private RegisterDeviceRequest mRegisterDeviceRequest;

	private NotifyDeviceRequest mNotifyDeviceRequest;

	private FindLocalDevicesRequest mFindLocalDevicesRequest;

	private HttpServer mHttpServer;

	private WebTarget mTarget;

	public MainTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the Grizzly2 web container
		mHttpServer = Main.startServer();

		ClientConfig cc = new ClientConfig();
		cc.register(RegisterDeviceRequestProtobufReader.class);
		cc.register(RegisterDeviceRequestProtobufWriter.class);
		cc.register(RegisterDeviceResponseProtobufReader.class);
		cc.register(RegisterDeviceResponseProtobufWriter.class);
		cc.register(NotifyDeviceRequestProtobufReader.class);
		cc.register(NotifyDeviceRequestProtobufWriter.class);
		cc.register(NotifyDeviceResponseProtobufReader.class);
		cc.register(NotifyDeviceResponseProtobufWriter.class);
		cc.register(FindLocalDevicesRequestProtobufReader.class);
		cc.register(FindLocalDevicesRequestProtobufWriter.class);
		cc.register(FindLocalDevicesResponseProtobufReader.class);
		cc.register(FindLocalDevicesResponseProtobufWriter.class);

		Client client = ClientBuilder.newClient(cc);

		mTarget = client.target(Main.BASE_URI);

		{
			DeviceProtoBuf.Device.Builder deviceBuilder = DeviceProtoBuf.Device
					.newBuilder();
			deviceBuilder.setDeviceId("b024ac7008bb0b69");
			deviceBuilder
					.setOsType(DeviceProtoBuf.Device.OSType.GOOGLE_ANDROID);
			deviceBuilder.setOsVersion("4.1.1_r99");
			deviceBuilder.setPushToken("test_push_token");
			deviceBuilder.setManufacturer("HUAWEI");
			deviceBuilder.setModel("HUAWEI G510-0100");
			deviceBuilder.setProduct("G510-0100");
			deviceBuilder.setDevice("hwG510-0100");

      // Set a spoofed location in order to run tests
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
      deviceBuilder.setLastKnownLocation(location.getLocationProtoBuf());

			RegisterDeviceRequest.Builder builder = DeviceProtoBuf.RegisterDeviceRequest
					.newBuilder();
			builder.setDevice(deviceBuilder.build());

			mRegisterDeviceRequest = builder.build();
		}

		{
			DeviceProtoBuf.FindLocalDevicesRequest.Builder builder = DeviceProtoBuf.FindLocalDevicesRequest
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

		mHttpServer.shutdownNow();
	}

	/**
	 * Test to see that a device is correctly registered
	 */
	@Test
	public void testRegisterDevice() {

		WebTarget wt = mTarget.path("devices");
		RegisterDeviceResponse response = wt
				.request("application/octet-stream").post(
						Entity.entity(mRegisterDeviceRequest,
								"application/octet-stream"),
						RegisterDeviceResponse.class);

		assertEquals(RegisterDeviceResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test to see that local devices can be found
	 */
	@Test
	public void testFindLocalDevices() {

		WebTarget wt = mTarget.path("devices/local");
		FindLocalDevicesResponse response = wt.request(
				"application/octet-stream").post(
				Entity.entity(mFindLocalDevicesRequest,
						"application/octet-stream"),
				FindLocalDevicesResponse.class);

		assertEquals(FindLocalDevicesResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test to see that a device can be notified
	 */
	@Test
	public void testNotifyDevice() {

		WebTarget wt = mTarget.path("notifications");
		NotifyDeviceResponse response = wt
				.request("application/octet-stream")
				.post(Entity.entity(mNotifyDeviceRequest,
						"application/octet-stream"), NotifyDeviceResponse.class);

		assertEquals(NotifyDeviceResponse.ErrorCode.NONE,
				response.getResponseCode());
	}

	/**
	 * Test if a WADL document is available at the relative path
	 * "application.wadl".
	 */
	@Test
	public void testApplicationWadl() {
		String serviceWadl = mTarget.path("application.wadl")
				.request(MediaTypes.WADL).get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}
}
