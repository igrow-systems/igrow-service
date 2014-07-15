/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)RegisterDeviceResponseProtobufWriter.java        
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
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.argusat.gjl.service.device.DeviceProtoBuf;

@Provider
@Produces("application/octet-stream")
public class RegisterDeviceResponseProtobufWriter implements
		MessageBodyWriter<DeviceProtoBuf.RegisterDeviceResponse> {

	@Override
	public void writeTo(DeviceProtoBuf.RegisterDeviceResponse response,
			Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		try {
			DeviceProtoBuf.RegisterDeviceResponse.Builder builder = DeviceProtoBuf.RegisterDeviceResponse
					.newBuilder(response);
			builder.build().writeTo(entityStream);
			return;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public long getSize(DeviceProtoBuf.RegisterDeviceResponse response,
			Class<?> type, Type typeParam, Annotation[] annotations,
			MediaType mediaType) {

		try {
			DeviceProtoBuf.RegisterDeviceResponse.Builder builder = DeviceProtoBuf.RegisterDeviceResponse
					.newBuilder(response);
			return builder.build().getSerializedSize();

		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type typeParam,
			Annotation[] annotations, MediaType mediaType) {

		return DeviceProtoBuf.RegisterDeviceResponse.class
				.isAssignableFrom(type);
	}
}