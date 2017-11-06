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

package com.igrow.observice.test;

import javax.ws.rs.core.MediaType;

import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.TestCase;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import com.igrow.model.Location;
import com.igrow.model.Observation;
import com.igrow.model.ObservationCollection;
import com.igrow.model.Observation.ModeType;
import com.igrow.model.Observation.ObservationType;
import com.igrow.observice.Main;
import com.igrow.observice.ObservationProtobufReader;
import com.igrow.observice.ObservationProtobufWriter;

public class MainTest extends TestCase {

	/*
	 * The first 4 pairs are within 1km of 50.9399695,-1.415058,15 and the last 4
	 * pairs are more than 1km from 50.9399695,-1.415058,15
	 * 
	 * 50.936195, -1.415285 50.942523, -1.409577 50.942604, -1.420563 50.932652,
	 * -1.418117
	 * 
	 * 50.927242, -1.423310 50.952123, -1.406076 50.949744, -1.442468 50.966449,
	 * -1.452510
	 */

	private static float[] lats = { 50.936195f, 50.942523f, 50.942604f, 50.932652f, 50.927242f, 50.952123f, 50.949744f,
			50.966449f };

	private static float[] lons = { -1.415285f, -1.409577f, -1.420563f, -1.418117f, -1.423310f, -1.406076f, -1.442468f,
			-1.452510f };

	private static long[] timestamps = { 1486501200000L, 1486501205000L, 1486501210000L, 1486501215000L, 1486501220000L,
			1486501225000L, 1486501230000L, 1486501235000L };

	private ObservationCollection mObservationCollection;

	private HttpServer mHttpServer;

	private WebTarget mTarget;

	private String mSensorId;

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
		// need to check the documentation.
		mTarget = client.target(Main.BASE_URI);

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

		generateObservations();

		WebTarget wt = mTarget.path("observations");
		String response = wt.request(MediaType.TEXT_PLAIN_TYPE)
				.post(Entity.entity(mObservationCollection, MediaType.APPLICATION_OCTET_STREAM_TYPE), String.class);

		assertEquals("OK", response);
	}

	public void testGetSpatiallyLocalObservations() {

		float latitude = 50.9399695f;
		float longitude = -1.415058f;
		int radius = 1000;
		int limit = 4;

		WebTarget wt = mTarget.path("observations");

		ObservationCollection response = wt.queryParam("lat", Float.toString(latitude))
				.queryParam("lon", Float.toString(longitude)).queryParam("radius", Integer.toString(radius))
				.queryParam("limit", Integer.toString(limit)).request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.get(ObservationCollection.class);

		assertEquals(4, response.getObservations().size());
	}

	public void testGetTemporallyLocalObservations() {

		// We need to call the postObservations test
		// because we can't guarantee the order of
		// test execution. A side effect of this is
		// to call generateObservations() which creates
		// a new UUID which is stored in mSensorId
		testPostObservations();
		// 250ms after the first timestamp
		long timestampStart = timestamps[0] - 250L;
		// 250ms before the final timestamp
		long timestampEnd = timestamps[timestamps.length - 1] + 250L;
		String sensorId = mSensorId;
		int limit = 1000;

		WebTarget wt = mTarget.path("sensor/" + sensorId + "/observations");

		ObservationCollection response = wt.queryParam("tss", Long.toString(timestampStart))
				.queryParam("tse", Long.toString(timestampEnd)).queryParam("limit", Integer.toString(limit)).request()
				.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).get(ObservationCollection.class);

		assertEquals(8, response.getObservations().size());
	}

	/**
	 * Test if a WADL document is available at the relative path "application.wadl".
	 */
	public void testApplicationWadl() {
		String serviceWadl = mTarget.path("application.wadl").request().accept(MediaType.APPLICATION_XML_TYPE)
				.get(String.class);

		assertTrue(serviceWadl.length() > 0);
	}

	private void generateObservations() {

		mSensorId = UUID.randomUUID().toString();

		mObservationCollection = new ObservationCollection();

		for (int i = 0; i < 8; ++i) {
			Observation observation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);

			observation.setSensorId(mSensorId);
			observation.setTimestamp(timestamps[i]);
			observation.setMode(ModeType.ACTIVE);
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
	}
}
