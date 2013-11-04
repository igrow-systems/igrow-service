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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.argusat.gjl.model.GnssChannelObservation;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

public class GnssChannelObservationTest {

	private static final String TEST_FILENAME = "observation-c0n-gnss.bin";

	private GnssChannelObservation mObservation;

	@Before
	public void setUp() throws Exception {
		mObservation = (GnssChannelObservation) Observation
				.newObservation(ObservationType.TYPE_GNSS_CHANNEL);

		mObservation.setTimestamp(111889349L);
		mObservation.setMode(ModeType.PASSIVE);
		// assertEquals()
		// assertEquals(137483L, location.getLatitude());
		Location location = mObservation.getLocation();
		location.setLatitude(19.843892f);
		location.setLongitude(-40.78372f);
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
	public void testNewObservationFromProtoBuf() throws IOException {

		InputStream entityStream = this.getClass().getResourceAsStream(
				"/" + TEST_FILENAME);

		ObservationProtoBuf.Observation observationProtobuf = ObservationProtoBuf.Observation
				.parseFrom(entityStream);
		Observation observation = Observation
				.newObservation(observationProtobuf);

		assertNotNull(observation);
		assertEquals(ObservationType.TYPE_GNSS_CHANNEL, observation.getType());
		assertEquals(ModeType.PASSIVE, observation.getMode());
		Location location = observation.getLocation();
		assertNotNull(location);
		assertTrue(observation.isValid());

		// Test the GNSS channel information
		assertEquals(25, mObservation.getPrn());
		assertEquals(27.0f, mObservation.getAzimuth(), 1e-6);
		assertEquals(43.1f, mObservation.getElevation(), 1e-6);
		assertEquals(14.7f, mObservation.getC0_n(), 1e-6);
		
		assertTrue(mObservation.getValues() != null);
		assertEquals(3, mObservation.getValues().length);
	}

	@Test
	public void testIsValid() {

		assertTrue(mObservation.isValid());
	}

	@Test
	public void testGetObservationProtoBuf() throws IOException {

		assertTrue(mObservation.isValid());

		ObservationProtoBuf.Observation protoBuf = mObservation
				.getObservationProtoBuf();

		assertTrue(protoBuf.getType() == ObservationProtoBuf.Observation.ObservationType.C0N_GNSS);
		assertTrue(111889349L == protoBuf.getTimestamp());
		assertEquals(19843891L, protoBuf.getLocation().getLatitude());
		assertEquals(-40783718L, protoBuf.getLocation().getLongitude());
		assertEquals(120000000L, protoBuf.getLocation().getAltitude());
		assertEquals(5.0f, protoBuf.getLocation().getHdop(), 1e-6);
		assertEquals(12.0f, protoBuf.getLocation().getVdop(), 1e-6);

		TemporaryFolder tempFolder = new TemporaryFolder();
		tempFolder.create();
		File entityFile = tempFolder.newFile(TEST_FILENAME);

		OutputStream entityStream = new FileOutputStream(entityFile);

		protoBuf.writeTo(entityStream);
	}

}
