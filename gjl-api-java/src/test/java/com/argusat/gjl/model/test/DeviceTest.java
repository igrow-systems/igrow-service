/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceTest.java        
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

package com.argusat.gjl.model.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.argusat.gjl.model.Device;
import com.argusat.gjl.service.device.DeviceProtoBuf;

public class DeviceTest {

	//private Device mDevice;
	
	private DeviceProtoBuf.Device mDeviceProtoBuf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		DeviceProtoBuf.Device.Builder builder = DeviceProtoBuf.Device.newBuilder();
		builder.setDeviceId("test-id-007");
		builder.setOsType(DeviceProtoBuf.Device.OSType.GOOGLE_ANDROID);
		builder.setOsVersion("4.4.0_r45");
		builder.setPushToken("test_push_token");
		builder.setManufacturer("HUAWEI");
		builder.setModel("HUAWEI G510-0100");
		builder.setProduct("G510-0100");
		builder.setDevice("hwG510-0100");
		
		mDeviceProtoBuf = builder.build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNewDevice() {
		Device device = Device.newDevice(mDeviceProtoBuf);
		
		assertEquals("test-id-007", device.getDeviceId());
		assertEquals(Device.OSType.GOOGLE_ANDROID, device.getOsType());
		assertEquals("4.4.0_r45", device.getOsVersion());
		assertEquals("test_push_token", device.getPushToken());
		assertEquals("HUAWEI", device.getManufacturer());
		assertEquals("HUAWEI G510-0100", device.getModel());
		assertEquals("G510-0100", device.getProduct());
		assertEquals("hwG510-0100", device.getDevice());
	}

	@Test
	public void testIsValid() {
		Device device = Device.newDevice(mDeviceProtoBuf);
		
		assertTrue(device.isValid());
	}

}
