/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)BeginLocatorSessionRequestProtobufReader.java        
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
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.igrow.protobuf.locator.LocatorProtoBuf.BeginLocatorSessionRequest;

@Provider
@Consumes("application/octet-stream")
public class BeginLocatorSessionRequestProtobufReader implements
		MessageBodyReader<BeginLocatorSessionRequest> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return BeginLocatorSessionRequest.class.isAssignableFrom(type);
	}

	public BeginLocatorSessionRequest readFrom(Class<BeginLocatorSessionRequest> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		try {
		
			BeginLocatorSessionRequest beginLocatorSessionRequest = BeginLocatorSessionRequest
					.newBuilder().mergeFrom(entityStream).build();
			return beginLocatorSessionRequest;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
}