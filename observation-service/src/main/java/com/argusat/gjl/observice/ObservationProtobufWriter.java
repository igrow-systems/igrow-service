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
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.ObservationCollection;
import com.argusat.gjl.service.observation.ObservationProtoBuf;

@Provider
@Consumes("application/observation-protobuf")
public class ObservationProtobufWriter implements
		MessageBodyWriter<ObservationCollection> {
	
	@Override
	public void writeTo(ObservationCollection observations, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		try {
			ObservationProtoBuf.Observations.Builder observationsProtoBufBuilder = ObservationProtoBuf.Observations
					.newBuilder();
			
			observationsProtoBufBuilder.setDeviceId(observations.getDeviceId());
			
			for (Observation observation : observations.getObservations()) {
				ObservationProtoBuf.Observation observationProtobuf = observation
						.getObservationProtoBuf();
				observationsProtoBufBuilder.addObservations(observationProtobuf);
			}
			observationsProtoBufBuilder.build().writeTo(entityStream);
			return;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public long getSize(ObservationCollection observations, Class<?> type,
			Type typeParam, Annotation[] annotations, MediaType mediaType) {
		
		return 0;
		
	}

	@Override
	public boolean isWriteable(Class<?> type, Type typeParam, Annotation[] annotations,
			MediaType mediaType) {
		
		return ObservationCollection.class.isAssignableFrom(type);
	}
}