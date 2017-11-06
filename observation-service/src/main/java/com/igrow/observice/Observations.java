/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Observations.java        
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

package com.igrow.observice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.protobuf.observation.ObservationProtoBuf;
import com.igrow.model.Observation;
import com.igrow.model.ObservationCollection;
import com.igrow.observice.repository.ObservationRepository;
import com.igrow.observice.repository.postgis.ObservationRepositoryTimescaleDbPostGISImpl;
import com.igrow.publisher.Publisher;
import com.igrow.publisher.PublisherRabbitMQ;

// The Java class will be hosted at the URI path "/observations"
@Path("/")
public class Observations {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Observations.class);

	private static ObservationRepository mObservationRepository = null;

	private static Publisher mPublisher = null;

	static {
		try {
			mObservationRepository = new ObservationRepositoryTimescaleDbPostGISImpl();
		} catch (ClassNotFoundException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		} catch (SQLException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		}
		try {
			mPublisher = new PublisherRabbitMQ();

			InputStream entityStream = PublisherRabbitMQ.class
					.getResourceAsStream("/"
							+ PublisherRabbitMQ.PROPERTIES_FILENAME);

			Properties properties = new Properties();
			properties.load(entityStream);
			entityStream.close();

			mPublisher.initialise(properties);

			mPublisher.connect();

		} catch (IOException e) {
			LOGGER.error("Couldn't connect to RabbitMQ server", e);
		}
	}

	public Observations() {

	}

	@Path("/observations")
	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("text/plain")
	// The Java method will consume content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public String postObservations(ObservationCollection observations) {

		List<Observation> obsList = observations.getObservations();
		if (obsList != null && obsList.size() > 0) {
			mObservationRepository.storeObservations(obsList);
			for (Observation observation : obsList) {
				LOGGER.debug(observation.getType() + " - "
						+ observation.getTimestamp());
				try {
					if (mPublisher != null && mPublisher.isConnected()) {
						mPublisher.publish(
								String.format("observation.%s",
										observation.getSensorId()),
								ObservationProtoBuf.Observation.class,
								observation.getObservationProtoBuf());
					}
				} catch (IOException e) {
					LOGGER.error("Failed to publish {}", observation.toString());
				}
			}
		}

		return "OK";

	}

	@Path("/observations")
	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will consume content identified by the MIME Media
	// type "text/plain"
	@Consumes("text/plain")
	public ObservationCollection getObservations(
			@QueryParam("lat") float latitude,
			@QueryParam("lon") float longitude,
			@QueryParam("radius") long radius,
			@QueryParam("limit") long limit) {

		List<Observation> obs = mObservationRepository.findObservations(
				latitude, longitude, radius, limit);
		ObservationCollection obsCollection = new ObservationCollection();
		obsCollection.setObservations(obs);

		return obsCollection;

	}
	
	@Path("/sensor/{sensorid}/observations")
	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will consume content identified by the MIME Media
	// type "text/plain"
	@Consumes("text/plain")
	public ObservationCollection getObservationsByTimestampRange(
			@QueryParam("tss") long timestampStart,
			@QueryParam("tse") long timestampEnd,
			@PathParam("sensorid") String sensorId,
			@QueryParam("limit") long limit) {

		
		List<Observation> obs = mObservationRepository.findObservations(
				sensorId, timestampStart, timestampEnd, limit);
		ObservationCollection obsCollection = new ObservationCollection();
		obsCollection.setObservations(obs);

		return obsCollection;

	}

}
