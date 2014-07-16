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
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

public class ObservationCollectionTest {

	private static final String TEST_FILENAME = "observations.bin";

	private ObservationCollection mObservationCollection;

	@Before
	public void setUp() throws Exception {

		mObservationCollection = new ObservationCollection();

		for (int i = 0; i < 4; ++i) {
			Observation observation = Observation
					.newObservation(ObservationType.TYPE_LOCATION_ONLY);

			observation.setDeviceId("test-id-007");
			observation.setTimestamp(111889349L);
			observation.setMode(ModeType.PASSIVE);
			// assertEquals()
			// assertEquals(137483L, location.getLatitude());
			Location location = observation.getLocation();
			location.setLatitude(19.82384f);
			location.setLongitude(12.37843f);
			location.setAltitude(120.0f);
			location.setHDOP(5.0f);
			location.setVDOP(12.0f);
			observation.setLocation(location);

			observation.isValid();
			//List<String> errors = observation.getObservationProtoBuf()
			//		.findInitializationErrors();

			mObservationCollection.add(observation);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObservationCollection() {

		assertNotNull(mObservationCollection);
		assertEquals(4, mObservationCollection.getObservations().size());

	}

	@Test
	public void testNewObservationCollectionFromProtoBuf() throws IOException {

		InputStream entityStream = this.getClass().getResourceAsStream(
				"/" + TEST_FILENAME);

		ObservationProtoBuf.Observations observationsProtobuf = ObservationProtoBuf.Observations
				.parseFrom(entityStream);
		// ObservationCollection observations = new ObservationCollection();

		assertNotNull(observationsProtobuf);
		assertTrue(observationsProtobuf.isInitialized());
		// assertEquals()
		// assertEquals(137483L, location.getLatitude());
		// location.setLatitude();
		// location.setLongitude(1237843L);
		// location.setAltitude(120.0f);
		// location.setHDOP(5.0f);
		// location.setVDOP(12.0f);

		// Observation observation =
		// Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
		//

	}

	@Test
	public void testIsValid() {
		// assertTrue(mObservationCollection.isValid());
		// mObservationCollection.isValid();
	}

	@Test
	public void testGetObservationsProtoBuf() throws IOException {

		ObservationProtoBuf.Observations.Builder protoBufBuilder = ObservationProtoBuf.Observations
				.newBuilder();
		
		for (Observation observation : mObservationCollection.getObservations()) {

			protoBufBuilder.addObservations(observation
					.getObservationProtoBuf());

		}

		assertTrue(protoBufBuilder.isInitialized());
		ObservationProtoBuf.Observations observationsProtoBuf = protoBufBuilder
				.build();

		TemporaryFolder tempFolder = new TemporaryFolder();
		tempFolder.create();
		File entityFile = tempFolder.newFile(TEST_FILENAME);

		OutputStream entityStream = new FileOutputStream(entityFile);

		observationsProtoBuf.writeTo(entityStream);

	}

}
