/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationTest.java        
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

import com.argusat.gjl.model.GnssChannelObservation;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;

public class GnssChannelObservationTest {

	private GnssChannelObservation mObservation;

	@Before
	public void setUp() throws Exception {
		mObservation = (GnssChannelObservation)Observation
				.newObservation(ObservationType.TYPE_GNSS_CHANNEL);

		mObservation.setDeviceId(007L);
		mObservation.setTimestamp(111889349L);
		mObservation.setMode(ModeType.PASSIVE);
		// assertEquals()
		// assertEquals(137483L, location.getLatitude());
		Location location = mObservation.getLocation();
		location.setLatitude(1982384L);
		location.setLongitude(1237843L);
		location.setAltitude(120.0f);
		location.setHDOP(5.0f);
		location.setVDOP(12.0f);
		mObservation.setLocation(location);

		// Add the GNSS channel information
		mObservation.setPrn(25);
		mObservation.setAzimuth(27.0f);
		mObservation.setElevation(43.1f);
		mObservation.setC0_n(14.7f);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObservation() {
		Observation observation = Observation
				.newObservation(ObservationType.TYPE_GNSS_CHANNEL);

		assertNotNull(observation);
		assertNotNull(observation.getLocation());
	}

	@Test
	public void testNewObservationObservationType() {
		assertTrue(mObservation.getType() == ObservationType.TYPE_GNSS_CHANNEL);
	}

	@Test
	public void testNewObservationObservation() {

		assertNotNull(mObservation);
		assertTrue(mObservation.getType() == ObservationType.TYPE_GNSS_CHANNEL);
		assertTrue(GnssChannelObservation.class == mObservation.getClass());
		assertNotNull(mObservation.getLocation());
	}

	@Test
	public void testIsValid() {

		assertTrue(mObservation.isValid());
	}

	@Test
	public void testGetObservationProtoBuf() {
		assertNotNull(mObservation.getObservationProtoBuf());
	}

}
