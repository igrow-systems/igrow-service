/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Observations.java        
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

package com.argusat.gjl.observice;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.ObservationCollection;

// The Java class will be hosted at the URI path "/observations"
@Path("/observations")
public class Observations {

	private static final Logger LOGGER = Logger.getLogger(Observations.class
			.getSimpleName());

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("text/plain")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public String postObservations(ObservationCollection observations) {

		for (Observation observation : observations.getObservations()) {
			LOGGER.finer(observation.getObservationProtoBuf().toString());
			System.out.println(observation.getTimestamp());
		}

		return "OK";
	}
}
