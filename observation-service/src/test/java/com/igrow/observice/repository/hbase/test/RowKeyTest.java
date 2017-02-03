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

package com.igrow.observice.repository.hbase.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.igrow.model.Location;
import com.igrow.model.Observation;
import com.igrow.model.Observation.ModeType;
import com.igrow.model.Observation.ObservationType;
import com.igrow.observice.repository.hbase.RowKey;

public class RowKeyTest {

	private Observation mObservation;

	@Before
	public void setUp() throws Exception {

		Location location = new Location();
		location.setLatitude(137483L);
		location.setLongitude(1237843L);
		location.setAltitude(120.0f);
		location.setHDOP(5.0f);
		location.setVDOP(12.0f);

		mObservation = Observation
				.newObservation(ObservationType.TYPE_LOCATION_ONLY);
		mObservation.setSensorId("test-id-007");
		mObservation.setLocation(location);
		mObservation.setTimestamp(11172763L);
		mObservation.setMode(ModeType.PASSIVE);

		assertTrue(mObservation.isValid());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRowKey() {

		RowKey rowKey = new RowKey(mObservation);
		byte[] rowKeyBytes = rowKey.getByteArray();

//		assertNotNull(rowKeyBytes);
//		assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
//				(byte) 0xFF }, rowKeyBytes);

	}

}
