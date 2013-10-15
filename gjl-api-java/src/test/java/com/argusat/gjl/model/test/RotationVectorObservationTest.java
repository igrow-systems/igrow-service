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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.model.RotationVectorObservation;

public class RotationVectorObservationTest {

	private RotationVectorObservation mObservation;

	@Before
	public void setUp() throws Exception {
		mObservation = (RotationVectorObservation)Observation
				.newObservation(ObservationType.TYPE_ROTATION_VECTOR);

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

		// Add the rotation vector
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObservation() {
		Observation observation = Observation
				.newObservation(ObservationType.TYPE_ROTATION_VECTOR);

		assertNotNull(observation);
		assertNotNull(observation.getLocation());
	}

	@Test
	public void testNewObservationObservationType() {
		assertTrue(mObservation.getType() == ObservationType.TYPE_ROTATION_VECTOR);

	}

	@Test
	public void testNewObservationObservation() {

		assertNotNull(mObservation);
		assertTrue(mObservation.getType() == ObservationType.TYPE_ROTATION_VECTOR);
		assertTrue(RotationVectorObservation.class == mObservation.getClass());
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
