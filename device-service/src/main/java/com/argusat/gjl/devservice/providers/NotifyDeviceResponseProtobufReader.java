/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)NotifyDeviceResponseProtobufReader.java        
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

package com.argusat.gjl.devservice.providers;

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

import com.argusat.gjl.service.device.DeviceProtoBuf;

@Provider
@Consumes("application/octet-stream")
public class NotifyDeviceResponseProtobufReader implements
		MessageBodyReader<DeviceProtoBuf.NotifyDeviceResponse> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return DeviceProtoBuf.NotifyDeviceResponse.class.isAssignableFrom(type);
	}

	public DeviceProtoBuf.NotifyDeviceResponse readFrom(Class<DeviceProtoBuf.NotifyDeviceResponse> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		try {
			DeviceProtoBuf.NotifyDeviceResponse notifyDeviceResponse = DeviceProtoBuf.NotifyDeviceResponse
					.newBuilder().mergeFrom(entityStream).build();
			
			return notifyDeviceResponse;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
}