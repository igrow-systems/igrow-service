/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Devices.java        
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

package com.argusat.gjl.devservice;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.DeviceRepositoryException;
import com.argusat.gjl.devservice.repository.postgis.DeviceRepositoryPostGISImpl;
import com.argusat.gjl.devservice.subscriber.MessageHandler;
import com.argusat.gjl.devservice.subscriber.Subscriber;
import com.argusat.gjl.devservice.subscriber.SubscriberRabbitMQ;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceResponse;

// The Java class will be hosted at the URI path "/devices"
@Path("/devices")
public class Devices implements MessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Devices.class);

	private static DeviceRepository mDeviceRepository = null;
	
	private static Subscriber mSubscriber = null;

	static {
		try {
			mDeviceRepository = new DeviceRepositoryPostGISImpl();
		} catch (ClassNotFoundException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		} catch (SQLException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		}
	}

	public Devices() {
		
		mSubscriber = new SubscriberRabbitMQ();
		
		try {
			mSubscriber.registerMessageHandler(this);
			mSubscriber.connect();
			mSubscriber.subscribe("*");
		} catch (IOException e) {
			LOGGER.error("Subscriber couldn't connect to broker", e);
		}
	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public DeviceProtoBuf.RegisterDeviceResponse registerDevice(
			DeviceProtoBuf.RegisterDeviceRequest registerDeviceRequest) throws DeviceRepositoryException {

		Device device = Device.newDevice(registerDeviceRequest
				.getDevice());

		mDeviceRepository.storeDevice(device);

		DeviceProtoBuf.RegisterDeviceResponse.Builder builder = DeviceProtoBuf.RegisterDeviceResponse
				.newBuilder();
		builder.setResponseCode(RegisterDeviceResponse.ErrorCode.NONE);

		return builder.build();

	}

	@Override
	public void handleMessage(Observation observation) {
		
		Device device = mDeviceRepository.findDevice(observation.getDeviceId());
		
		device.setLastKnownLocation(observation.getLocation());
		
		try {
			mDeviceRepository.storeDevice(device);
		} catch (DeviceRepositoryException e) {
			LOGGER.error("Couldn't store device", e);
		}
		
	}

}
