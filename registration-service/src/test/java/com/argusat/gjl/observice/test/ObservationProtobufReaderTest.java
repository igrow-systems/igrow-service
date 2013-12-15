/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationProtobufReaderTest.java        
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

package com.argusat.gjl.observice.test;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.observice.Main;
import com.argusat.gjl.observice.ObservationProtobufReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ObservationProtobufReaderTest extends TestCase {

	private HttpServer httpServer;

	private WebResource r;

	public ObservationProtobufReaderTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the Grizzly2 web container
		httpServer = Main.startServer();

		// create the client
		Client c = Client.create();
		r = c.resource(Main.BASE_URI);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		httpServer.stop();
	}

	public void testReadFrom() throws WebApplicationException, IOException {

		ObservationProtobufReader reader = new ObservationProtobufReader();

		InputStream entityStream = this.getClass().getResourceAsStream(
				"/observations.bin");

		ObservationCollection observations = reader.readFrom(null, null, null,
				null, null, entityStream);

		for (Observation observation : observations.getObservations()) {
			assertTrue(observation.isValid());
		}
	}

	public void testIsReadable() {
		ObservationProtobufReader reader = new ObservationProtobufReader();

		boolean result = reader.isReadable(ObservationCollection.class, null, null, null);
		assertTrue(result);
	}

}
