/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)RowKeyTest.java        
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

package com.argusat.gjl.devservice.repository.hbase;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Location;

public class RowKeyTest {

	private Device mDevice;

	@Before
	public void setUp() throws Exception {

		Location location = new Location();
		location.setLatitude(137483L);
		location.setLongitude(1237843L);
		location.setAltitude(120.0f);
		location.setHDOP(5.0f);
		location.setVDOP(12.0f);

		mDevice = new Device();
		mDevice.setDeviceId("test-id-007");
		
		
		
		assertTrue(mDevice.isValid());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRowKey() {

		RowKey rowKey = new RowKey(mDevice);
		byte[] rowKeyBytes = rowKey.getByteArray();

		assertNotNull(rowKeyBytes);
		assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF }, rowKeyBytes);

	}

}
