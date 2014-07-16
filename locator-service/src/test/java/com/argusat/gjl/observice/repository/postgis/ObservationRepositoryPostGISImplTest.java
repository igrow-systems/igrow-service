/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationRepositoryPostGISImplTest.java        
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

package com.argusat.gjl.observice.repository.postgis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.model.ObservationCollection;

public class ObservationRepositoryPostGISImplTest {

	private ObservationRepositoryPostGISImpl mObservationRepository;

	private ObservationCollection mObservationCollection;

	@Before
	public void setUp() throws Exception {

		mObservationCollection = new ObservationCollection();

		for (int i = 0; i < 4; ++i) {
			Observation observation = Observation
					.newObservation(ObservationType.TYPE_LOCATION_ONLY);

			observation.setTimestamp(111889349L);
			observation.setMode(ModeType.PASSIVE);
			// assertEquals()
			// assertEquals(137483L, location.getLatitude());
			Location location = observation.getLocation();
			location.setLatitude(1982384L);
			location.setLongitude(1237843L);
			location.setAltitude(120.0f);
			location.setHDOP(5.0f);
			location.setVDOP(12.0f);
			observation.setLocation(location);

			mObservationCollection.add(observation);
		}

		mObservationRepository = new ObservationRepositoryPostGISImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStoreObservations() {

		mObservationRepository.storeObservations(mObservationCollection
				.getObservations());

	}

}
