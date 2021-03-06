/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)NotifyDeviceRequestProtobufWriter.java        
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

package com.igrow.devservice.providers;

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

import com.igrow.protobuf.device.DeviceProtoBuf;

@Provider
@Produces("application/octet-stream")
public class NotifyDeviceRequestProtobufWriter implements
		MessageBodyWriter<DeviceProtoBuf.NotifyDeviceRequest> {

	@Override
	public void writeTo(DeviceProtoBuf.NotifyDeviceRequest request,
			Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		try {
			DeviceProtoBuf.NotifyDeviceRequest.Builder builder = DeviceProtoBuf.NotifyDeviceRequest
					.newBuilder(request);
			builder.build().writeTo(entityStream);
			return;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public long getSize(DeviceProtoBuf.NotifyDeviceRequest request,
			Class<?> type, Type typeParam, Annotation[] annotations,
			MediaType mediaType) {

		try {
			DeviceProtoBuf.NotifyDeviceRequest.Builder builder = DeviceProtoBuf.NotifyDeviceRequest
					.newBuilder(request);
			return builder.build().getSerializedSize();

		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type typeParam,
			Annotation[] annotations, MediaType mediaType) {

		return DeviceProtoBuf.NotifyDeviceRequest.class
				.isAssignableFrom(type);
	}
}