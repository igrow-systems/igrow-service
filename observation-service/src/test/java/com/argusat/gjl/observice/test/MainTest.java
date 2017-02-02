/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)MainTest.java        
 *
 * Copyright (c) 2013 - 2014, 2017 Argusat Limited
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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.TestCase;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ModeType;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.observice.Main;
import com.argusat.gjl.observice.ObservationProtobufReader;
import com.argusat.gjl.observice.ObservationProtobufWriter;

public class MainTest extends TestCase {

	/*
	 * The first 4 pairs are within 1km of 50.9399695,-1.415058,15 and the last
	 * 4 pairs are more than 1km from 50.9399695,-1.415058,15
	 * 
	 * 50.936195, -1.415285 
	 * 50.942523, -1.409577 
	 * 50.942604, -1.420563 
	 * 50.932652, -1.418117
	 * 
	 * 50.927242, -1.423310 
	 * 50.952123, -1.406076 
	 * 50.949744, -1.442468 
	 * 50.966449, -1.452510
	 */

	private static float[] lats = { 50.936195f, 50.942523f, 50.942604f,
			50.932652f, 50.927242f, 50.952123f, 50.949744f, 50.966449f };

	private static float[] lons = { -1.415285f, -1.409577f, -1.420563f,
			-1.418117f, -1.423310f, -1.406076f, -1.442468f, -1.452510f };

	private ObservationCollection mObservationCollection;

  private HttpServer mHttpServer;

	private WebTarget mTarget;

	public MainTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// start the Grizzly2 web container
		mHttpServer = Main.startServer();

		ClientConfig cc = new ClientConfig();
		cc.register(ObservationProtobufReader.class);
		cc.register(ObservationProtobufWriter.class);

    Client client = ClientBuilder.newClient(cc);

    // TODO: I think this is inefficient use of Client and WebTarget
    //       need to check the documentation.
    mTarget = client.target(Main.BASE_URI);

		mObservationCollection = new ObservationCollection();

		for (int i = 0; i < 8; ++i) {
			Observation observation = Observation
					.newObservation(ObservationType.TYPE_LOCATION_ONLY);

			observation.setDeviceId("test-id-00" + Integer.toString(i));
			observation.setTimestamp(111889349L);
			observation.setMode(ModeType.PASSIVE);
			// assertEquals()
			// assertEquals(137483L, location.getLatitude());
			Location location = observation.getLocation();
			location.setLatitude(lats[i]);
			location.setLongitude(lons[i]);
			location.setAltitude(15.0f);
			location.setHDOP(5.0f);
			location.setVDOP(12.0f);
			observation.setLocation(location);

			assertTrue(observation.isValid());

			mObservationCollection.add(observation);
		}

		// mObservationsProtoBuf = builder.build();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

    mHttpServer.shutdownNow();

	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	public void testPostObservations() {

		WebTarget wt = mTarget.path("observations");
		String response = wt.request(MediaType.TEXT_PLAIN_TYPE)
      .post(Entity.entity(mObservationCollection,
                          MediaType.APPLICATION_OCTET_STREAM_TYPE),
            String.class);
    
		assertEquals("OK", response);
	}

	public void testGetLocalObservations() {

		float latitude = 50.9399695f;
		float longitude = -1.415058f;
		int radius = 1000;
		int limit = 4;
		
		WebTarget wt = mTarget.path("observations");

    ObservationCollection response = wt
      .queryParam("lat", Float.toString(latitude))
      .queryParam("lon", Float.toString(longitude))
      .queryParam("radius", Integer.toString(radius))
      .queryParam("limit", Integer.toString(limit))
      .request()
      .accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
      .get(ObservationCollection.class);

		assertEquals(4, response.getObservations().size());
	}

	/**
	 * Test if a WADL document is available at the relative path
	 * "application.wadl".
	 */
	public void testApplicationWadl() {
		String serviceWadl = mTarget.path("application.wadl")
      .request()
      .accept(MediaType.APPLICATION_XML_TYPE)
      .get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}
}
