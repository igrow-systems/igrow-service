/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)PublisherRabbitMQTest.java        
 *
 * Copyright (c) 2014 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.argusat.gjl.observice.publisher.rabbitmq.test;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.observice.publisher.rabbitmq.PublisherRabbitMQ;

public class PublisherRabbitMQTest {

	private Observation mObservation;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		mObservation = Observation
				.newObservation(ObservationType.TYPE_LOCATION_ONLY);

		mObservation.setDeviceId("test-id-007");
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
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnect() throws IOException {
		
		PublisherRabbitMQ publisher = new PublisherRabbitMQ();
		
		publisher.connect();
		
		publisher.close();
		
	}

	@Test
	public void testPublish() throws IOException {

		PublisherRabbitMQ publisher = new PublisherRabbitMQ();
		
		publisher.connect();
		publisher.publish(mObservation);
		
		publisher.close();
	}

	@Test
	public void testClose() throws IOException {
		
		PublisherRabbitMQ publisher = new PublisherRabbitMQ();
		
		publisher.connect();
		
		publisher.close();
	
	}

}
