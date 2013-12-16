/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Devices.java        
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

package com.argusat.gjl.devservice;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.postgis.DeviceRepositoryPostGISImpl;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.service.device.DeviceProtoBuf;

// The Java class will be hosted at the URI path "/observations"
@Path("/devices")
public class Devices {

	private static final Logger LOGGER = LoggerFactory.getLogger(Devices.class);

	private static DeviceRepository mObservationRepository = null;

	static {
		try {
			mObservationRepository = new DeviceRepositoryPostGISImpl();
		} catch (ClassNotFoundException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		} catch (SQLException e) {
			LOGGER.error("Couldn't construct PostGIS repository", e);
		}
	}

	public Devices() {

	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("text/plain")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public String registerDevice(DeviceProtoBuf.RegisterDeviceRequest registerDeviceRequest) {
		
		mObservationRepository.storeDevice(Device.newDevice(registerDeviceRequest.getDevice()));

		return "OK";

	}
}
