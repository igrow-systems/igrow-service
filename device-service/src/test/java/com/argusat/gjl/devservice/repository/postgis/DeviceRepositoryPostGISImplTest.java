/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceRepositoryPostGISImplTest.java        
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

package com.argusat.gjl.devservice.repository.postgis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Device.OSType;

public class DeviceRepositoryPostGISImplTest {

	private DeviceRepositoryPostGISImpl mDeviceRepository;

	private Device mDevice;

	@Before
	public void setUp() throws Exception {

		mDevice = new Device();
		mDevice.setDeviceId("test-id-009");
		mDevice.setOsType(OSType.GOOGLE_ANDROID);
		mDevice.setOsVersion("4.1.1_r99");
		mDevice.setPushToken("test_push_token");
		mDeviceRepository = new DeviceRepositoryPostGISImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStoreDevice() {

		mDeviceRepository.storeDevice(mDevice);
		Device device = mDeviceRepository.findDevice(mDevice.getDeviceId());
		
		assertEquals(mDevice.getDeviceId(), device.getDeviceId());
		assertEquals(mDevice.getOsType(), device.getOsType());
		assertEquals(mDevice.getOsVersion(), device.getOsVersion());
		assertEquals(mDevice.getPushToken(), device.getPushToken());
	}

}
