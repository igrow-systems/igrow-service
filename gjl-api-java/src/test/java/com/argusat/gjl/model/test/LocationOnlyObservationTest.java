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

	private Observation mObservation;
	
	@Before
	public void setUp() throws Exception {
		mObservation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
		
		mObservation.setDeviceId(007L);
		mObservation.setTimestamp(111889349L);
		mObservation.setMode(ModeType.PASSIVE);
		//assertEquals()
		//assertEquals(137483L, location.getLatitude());
    	Location location = mObservation.getLocation();
		location.setLatitude(1982384L);
    	location.setLongitude(1237843L);
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
		assertNotNull(mObservation.getLocation());
	}

	@Test
	public void testNewObservationFromProtoBuf() throws IOException {
		
		InputStream entityStream = this.getClass().getResourceAsStream("/observation-location-only.bin");
    	
		ObservationProtoBuf.Observation observationProtobuf = ObservationProtoBuf.Observation.parseFrom(entityStream);
		Observation observation = Observation.newObservation(observationProtobuf);
		
		assertNotNull(observation);
		assertEquals(ObservationType.TYPE_LOCATION_ONLY, observation.getType());
		assertTrue(observation.isValid());
		
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
	public void testIsValid() {
		
		assertTrue(mObservation.isValid());
		
	}

	@Test
	public void testGetObservationProtoBuf() throws IOException {
    	
    	assertTrue(mObservation.isValid());
    	
    	ObservationProtoBuf.Observation protoBuf = mObservation.getObservationProtoBuf();
    	
    	assertTrue(protoBuf.getType() == ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
    	assertTrue(007L == protoBuf.getDeviceId());
    	assertTrue(111889349L == protoBuf.getTimestamp());
    	assertTrue(1982384L == protoBuf.getLocation().getLatitude());
    	assertTrue(1237843L == protoBuf.getLocation().getLongitude());
    	assertEquals(120.0f, protoBuf.getLocation().getAltitude(), 0.001);
    	assertEquals(5.0f, protoBuf.getLocation().getHdop(), 0.001);
    	assertEquals(12.0f, protoBuf.getLocation().getVdop(), 0.001);
    	
    	TemporaryFolder tempFolder = new TemporaryFolder();
    	tempFolder.create();
    	File entityFile = tempFolder.newFile("observation-location-only.bin");
    	
    	OutputStream entityStream = new FileOutputStream(entityFile);
    	
    	protoBuf.writeTo(entityStream);
    	
	}

}
