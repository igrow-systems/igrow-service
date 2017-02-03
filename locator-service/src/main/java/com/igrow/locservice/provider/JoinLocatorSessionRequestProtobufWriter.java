/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)JoinLocatorSessionRequestProtobufWriter.java        
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

package com.igrow.locservice.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.igrow.protobuf.locator.LocatorProtoBuf.JoinLocatorSessionRequest;

@Provider
@Produces("application/octet-stream")
public class JoinLocatorSessionRequestProtobufWriter implements
		MessageBodyWriter<JoinLocatorSessionRequest> {

	@Override
	public void writeTo(JoinLocatorSessionRequest request, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		try {
			JoinLocatorSessionRequest.Builder joinLocatorSessionRequestBuilder = JoinLocatorSessionRequest
					.newBuilder(request);

			joinLocatorSessionRequestBuilder.build().writeTo(entityStream);
			return;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public long getSize(JoinLocatorSessionRequest request, Class<?> type,
			Type typeParam, Annotation[] annotations, MediaType mediaType) {

		try {
			JoinLocatorSessionRequest.Builder joinLocatorSessionRequestBuilder = JoinLocatorSessionRequest
					.newBuilder(request);

			return joinLocatorSessionRequestBuilder.build()
					.getSerializedSize();

		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type typeParam,
			Annotation[] annotations, MediaType mediaType) {

		return JoinLocatorSessionRequest.class.isAssignableFrom(type);
	}
}