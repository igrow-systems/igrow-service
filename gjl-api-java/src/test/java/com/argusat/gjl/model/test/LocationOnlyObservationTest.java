/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocationOnlyObservationTest.java        
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

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

public class LocationOnlyObservationTest {

	private Observation mObservation;
	
	@Before
	public void setUp() throws Exception {
		mObservation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObservation() {
		
		assertNotNull(mObservation);
		assertNotNull(mObservation.getLocation());
	}

	@Test
	public void testNewObservationFromProtoBuf() throws IOException {
		
		InputStream entityStream = this.getClass().getResourceAsStream("/observation-location-only.bin");
    	
		ObservationProtoBuf.Observation observationProtobuf = ObservationProtoBuf.Observation.parseFrom(entityStream);
		Observation observation = Observation.newObservation(observationProtobuf);
		
		assertNotNull(observation);
		assertEquals(ObservationType.TYPE_LOCATION_ONLY, observation.getType());
		
		Location location = observation.getLocation();
		assertNotNull(location);
		//assertEquals()
		//assertEquals(137483L, location.getLatitude());
    	//location.setLatitude();
    	//location.setLongitude(1237843L);
    	//location.setAltitude(120.0f);
    	//location.setHDOP(5.0f);
    	//location.setVDOP(12.0f);
    	
    	//Observation observation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
    	//
		
	}

	@Test
	public void testNewObservationObservation() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValid() {
		
		
		
		assertTrue(mObservation.isValid());
		
	}

	@Test
	public void testGetObservationProtoBuf() {
		fail("Not yet implemented");
	}

}
