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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.LocationOnlyObservation;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

public class LocationOnlyObservationTest {

	private static final String TEST_FILENAME = "observation-location-only.bin";
	
	private Observation mObservation;
	
	@Before
	public void setUp() throws Exception {
		mObservation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
		
		mObservation.setDeviceId("test-id-007");
		mObservation.setTimestamp(111889349L);
		mObservation.setMode(ModeType.PASSIVE);
		//assertEquals()
		//assertEquals(137483L, location.getLatitude());
    	Location location = mObservation.getLocation();
		location.setLatitude(19.843892f);
		location.setLongitude(-40.78372f);
		location.setAltitude(120.0f);
    	location.setHDOP(5.0f);
    	location.setVDOP(12.0f);
    	mObservation.setLocation(location);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObservation() {
		
		assertNotNull(mObservation);
		assertTrue(mObservation.getType() == ObservationType.TYPE_LOCATION_ONLY);
		assertTrue(LocationOnlyObservation.class == mObservation.getClass());

		Location location = mObservation.getLocation();
		assertNotNull(location);
		assertEquals(19.843892f, location.getLatitude(), 1e-6);
		assertEquals(-40.78372f, location.getLongitude(), 1e-6);
		assertEquals(120.0f, location.getAltitude(), 1e-6);
		assertEquals(5.0f, location.getHDOP(), 1e-6);
		assertEquals(12.0f, location.getVDOP(), 1e-6);
	}

	@Test
	public void testNewObservationFromProtoBuf() throws IOException {
		
		InputStream entityStream = this.getClass().getResourceAsStream("/" + TEST_FILENAME);
    	
		ObservationProtoBuf.Observation observationProtobuf = ObservationProtoBuf.Observation.parseFrom(entityStream);
		Observation observation = Observation.newObservation(observationProtobuf);
		
		assertNotNull(observation);
		assertEquals(ObservationType.TYPE_LOCATION_ONLY, observation.getType());
		assertEquals(ModeType.PASSIVE, observation.getMode());
		Location location = observation.getLocation();
		assertNotNull(location);
		assertTrue(observation.isValid());
		assertNotNull(location);
		assertEquals(19.843892f, location.getLatitude(), 1e-6);
		assertEquals(-40.78372f, location.getLongitude(), 1e-6);
		assertEquals(120.0f, location.getAltitude(), 1e-6);
		assertEquals(5.0f, location.getHDOP(), 1e-6);
		assertEquals(12.0f, location.getVDOP(), 1e-6);
		
	}

	@Test
	public void testIsValid() {
		
		assertTrue(mObservation.isValid());
		
	}

	@Test
	public void testGetObservationProtoBuf() throws IOException {
    	
    	assertTrue(mObservation.isValid());
    	
    	ObservationProtoBuf.Observation protoBuf = mObservation.getObservationProtoBuf();
    	
    	assertTrue(protoBuf.getType() == ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
    	assertEquals("test-id-007", protoBuf.getDeviceId());
    	assertTrue(111889349L == protoBuf.getTimestamp());
    	assertEquals(19843891L, protoBuf.getLocation().getLatitude());
    	assertEquals(-40783718L, protoBuf.getLocation().getLongitude());
    	assertEquals(120000000L, protoBuf.getLocation().getAltitude());
    	assertEquals(5.0f, protoBuf.getLocation().getHdop(), 0.001);
    	assertEquals(12.0f, protoBuf.getLocation().getVdop(), 0.001);
    	
    	TemporaryFolder tempFolder = new TemporaryFolder();
    	tempFolder.create();
    	File entityFile = tempFolder.newFile(TEST_FILENAME);
    	
    	OutputStream entityStream = new FileOutputStream(entityFile);
    	
    	protoBuf.writeTo(entityStream);
    	
	}

}
