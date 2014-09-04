/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocationTest.java        
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

package com.argusat.gjl.model.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.argusat.gjl.model.Location;

public class LocationTest {

	private Location mLocation;

	@Before
	public void setUp() throws Exception {
		mLocation = new Location();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLocation() {

	}

	@Test
	public void testLocationLocation() {

	}

	@Test
	public void testIsValid() {

		assertFalse(mLocation.isValid());

		mLocation.setLatitude(12.32783f);
		mLocation.setLongitude(0.088762f);
		mLocation.setAltitude(126.4f);
		mLocation.setHDOP(32.0f);
		mLocation.setVDOP(8.0f);

		assertTrue(mLocation.isValid());
	}

	@Test
	public void testGetProtoBuf() {

		mLocation.setLatitude(12.32783f);
		mLocation.setLongitude(0.088762f);
		mLocation.setAltitude(126.4f);
		mLocation.setHDOP(32.0f);
		mLocation.setVDOP(8.0f);

		assertNotNull(mLocation.getLocationProtoBuf());
		assertTrue(mLocation.getLocationProtoBuf().isInitialized());

		assertEquals(12327830L, mLocation.getLocationProtoBuf().getLatitude());
		assertEquals(88762L, mLocation.getLocationProtoBuf().getLongitude());
		assertEquals(126400001L, mLocation.getLocationProtoBuf().getAltitude());
	}
	
	@Test
	public void testEquality() {
		
		Location location1 = new Location();
		location1.setLatitude(12.32783f);
		location1.setLongitude(0.088762f);
		location1.setAltitude(126.4f);
		location1.setHDOP(32.0f);
		location1.setVDOP(8.0f);
		
		Location location2 = new Location();
		location2.setLatitude(12.32783f);
		location2.setLongitude(0.088762f);
		location2.setAltitude(126.4f);
		location2.setHDOP(32.0f);
		location2.setVDOP(8.0f);
		
		assertEquals(location1, location2);
	}

}
