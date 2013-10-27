/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationProtobufReader.java        
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

@Provider
@Consumes("application/octet-stream")
public class ObservationProtobufReader implements
		MessageBodyReader<ObservationCollection> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return ObservationCollection.class.isAssignableFrom(type);
	}

	public ObservationCollection readFrom(Class<ObservationCollection> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		ObservationCollection observations = new ObservationCollection();
		try {
			ObservationProtoBuf.Observations observationsProtobuf = ObservationProtoBuf.Observations
					.newBuilder().mergeFrom(entityStream).build();
			observations.setDeviceId(observationsProtobuf.getDeviceId());
			for (ObservationProtoBuf.Observation observationProtoBuf : observationsProtobuf
					.getObservationsList()) {
				Observation observation = Observation
						.newObservation(observationProtoBuf);
				observations.add(observation);
			}
			return observations;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
}