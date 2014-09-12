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

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.DeviceRepositoryException;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Location;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.FindLocalDevicesResponse;
import com.argusat.gjl.service.device.DeviceProtoBuf.RegisterDeviceResponse;

// The Java class will be hosted at the URI path "/devices"
@Path("/devices")
public class Devices {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(Devices.class);
	
	@Inject
	private DeviceRepository mDeviceRepository;

	@Inject
	private ObservationListener mObservationListener;
	
	static {

	}

	public Devices() {
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
			DeviceProtoBuf.RegisterDeviceRequest registerDeviceRequest)
			throws DeviceRepositoryException {

		LOGGER.info(String.format(">registerDevice [ %s ]",
				registerDeviceRequest.getDevice().getDeviceId()));

		Device device = Device.newDevice(registerDeviceRequest.getDevice());

		mDeviceRepository.storeDevice(device);

		DeviceProtoBuf.RegisterDeviceResponse.Builder builder = DeviceProtoBuf.RegisterDeviceResponse
				.newBuilder();
		builder.setResponseCode(RegisterDeviceResponse.ErrorCode.NONE);

		return builder.build();

	}

	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("text/plain")
	@Path("{deviceId}")
	public DeviceProtoBuf.Device getDevice(
			@PathParam("deviceId") String deviceId)
			throws DeviceRepositoryException {

		LOGGER.info(String.format(">getDevice [ %s ]", deviceId));

		Device device = mDeviceRepository.findDevice(deviceId);

		return device.getDeviceProtoBuf();

	}

	// The Java method will process HTTP GET requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	@Path("local")
	public DeviceProtoBuf.FindLocalDevicesResponse findLocalDevices(
			DeviceProtoBuf.FindLocalDevicesRequest findLocalDevicesRequest)
			throws DeviceRepositoryException {

		Location centre = new Location(findLocalDevicesRequest.getCentre());
		long radius = findLocalDevicesRequest.getRadius();
		long limit = findLocalDevicesRequest.getLimit();

		LOGGER.info(String.format(
				">findLocalDevices [ %s, radius = %d, limit = %d ]", centre,
				radius, limit));

		List<Device> devices = mDeviceRepository.findLocalDevices(
				centre.getLatitude(), centre.getLongitude(), radius, limit);

		DeviceProtoBuf.FindLocalDevicesResponse.Builder builder = DeviceProtoBuf.FindLocalDevicesResponse
				.newBuilder();

		if (devices == null) {
			builder.setResponseCode(FindLocalDevicesResponse.ErrorCode.NO_LOCAL_DEVICES);
		} else {

			for (Device device : devices) {
				builder.addDevices(device.getDeviceProtoBuf());
			}
			builder.setResponseCode(FindLocalDevicesResponse.ErrorCode.NONE);
		}

		return builder.build();

	}

}
