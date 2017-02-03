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

package com.igrow.devservice.repository.postgis;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.igrow.devservice.repository.DeviceRepositoryException;
import com.igrow.devservice.repository.postgis.DeviceRepositoryPostGISImpl;
import com.igrow.model.Device;
import com.igrow.model.Location;
import com.igrow.model.Device.OSType;

public class DeviceRepositoryPostGISImplTest {

	private DeviceRepositoryPostGISImpl mDeviceRepository;

	private Device mInvalidDevice;
	
	private Device mValidDevice;

	@Before
	public void setUp() throws Exception {

		mInvalidDevice = new Device();
		mInvalidDevice.setDeviceId("test-id-009");
		mInvalidDevice.setOsType(OSType.GOOGLE_ANDROID);
		mInvalidDevice.setOsVersion("4.1.1_r99");
		mInvalidDevice.setPushToken("test_push_token");
		
		mValidDevice = new Device();
		mValidDevice.setDeviceId("test-id-011");
		mValidDevice.setOsType(OSType.GOOGLE_ANDROID);
		mValidDevice.setOsVersion("4.1.1_r99");
		mValidDevice.setPushToken("test_push_token");
		mValidDevice.setManufacturer("HUAWEI");
		mValidDevice.setModel("HUAWEI G510-0100");
		mValidDevice.setProduct("G510-0100");
		mValidDevice.setDevice("hwG510-0100");
		
		Location lastKnownLocation =new Location();
		lastKnownLocation.setLatitude(12.32783f);
		lastKnownLocation.setLongitude(0.088762f);
		lastKnownLocation.setAltitude(126.4f);
		
		mValidDevice.setLastKnownLocation(lastKnownLocation);
		
		
		mDeviceRepository = new DeviceRepositoryPostGISImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStoreDevice() throws DeviceRepositoryException {

		mDeviceRepository.storeDevice(mValidDevice);
		Device device = mDeviceRepository.findDevice(mValidDevice.getDeviceId());
		
		assertEquals(mValidDevice.getDeviceId(), device.getDeviceId());
		assertEquals(mValidDevice.getOsType(), device.getOsType());
		assertEquals(mValidDevice.getOsVersion(), device.getOsVersion());
		assertEquals(mValidDevice.getPushToken(), device.getPushToken());
		assertEquals(mValidDevice.getManufacturer(), device.getManufacturer());
		assertEquals(mValidDevice.getModel(), device.getModel());
		assertEquals(mValidDevice.getProduct(), device.getProduct());
		assertEquals(mValidDevice.getDevice(), device.getDevice());
		assertEquals(mValidDevice.getLastKnownLocation(), device.getLastKnownLocation());
		
	}
	
	@Test(expected=DeviceRepositoryException.class)
	public void testStoreDeviceThrowsDeviceRepositoryExceptionInvalidDevice() throws DeviceRepositoryException {

		mDeviceRepository.storeDevice(mInvalidDevice);
		
	}
	
	@Test(expected=DeviceRepositoryException.class)
	public void testStoreDeviceThrowsDeviceRepositoryExceptionNullDevice() throws DeviceRepositoryException {

		mDeviceRepository.storeDevice(null);
		
	}
	
	@Test
	public void testFindLocalDevices() throws DeviceRepositoryException {
		
		// this is a *really* bad test
		// TODO: do it properly
		for (int i = 0; i < 5; ++i) {
			mValidDevice.setDeviceId(String.format("test-id-44%d", i));
			mDeviceRepository.storeDevice(mValidDevice);
		}
		
		List<Device> devices = mDeviceRepository.findLocalDevices(12.32783f, 0.088762f, 1000, 5);
		
		assertEquals(5, devices.size());
		
	}
	

}
