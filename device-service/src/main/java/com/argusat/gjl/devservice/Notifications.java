/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Notifications.java        
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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.push.gcmxmpp.SmackCcsClient;
import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Device.OSType;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.NotifyDeviceResponse;

// The Java class will be hosted at the URI path "/notifications"
@Path("/notifications")
public class Notifications {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Notifications.class);

	@Inject
	private DeviceRepository mDeviceRepository;

	private static final SmackCcsClient mGcmPushClient = new SmackCcsClient();

	static {
		try {
			mGcmPushClient.connect();
		} catch (XMPPException e) {
			LOGGER.error("Couldn't connect to Google Cloud Messaging", e);
		}
	}

	public Notifications() {

	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public DeviceProtoBuf.NotifyDeviceResponse notifyDevice(
			DeviceProtoBuf.NotifyDeviceRequest notifyDeviceRequest) {

		LOGGER.info(String.format(">notifyDevice [ %s ]",
				notifyDeviceRequest
				.getDeviceId()));
		
		DeviceProtoBuf.NotifyDeviceResponse.Builder builder = DeviceProtoBuf.NotifyDeviceResponse
				.newBuilder();

		Device device = mDeviceRepository.findDevice(notifyDeviceRequest
				.getDeviceId());

		if (device == null) {
			builder.setResponseCode(NotifyDeviceResponse.ErrorCode.DEVICE_NOT_FOUND);
			return builder.build();
		}

		// send message via Google Cloud Messaging if it's an Android device
		if (device.getOsType() == OSType.GOOGLE_ANDROID) {

			// Send a sample hello downstream message to a device.
			String toRegId = device.getPushToken();
			String messageId = mGcmPushClient.getRandomMessageId();
			Map<String, String> payload = new HashMap<String, String>();
			payload.put("Hello", "World");
			payload.put("CCS", "Dummy Message");
			payload.put("EmbeddedMessageId", messageId);
			String collapseKey = "sample";
			Long timeToLive = 10000L;
			Boolean delayWhileIdle = true;
			mGcmPushClient.send(SmackCcsClient
					.createJsonMessage(toRegId, messageId, payload,
							collapseKey, timeToLive, delayWhileIdle));

			builder.setResponseCode(NotifyDeviceResponse.ErrorCode.NONE);

		} else {
			builder.setResponseCode(NotifyDeviceResponse.ErrorCode.DEVICE_NOT_SUPPORTED);
		}

		return builder.build();

	}
}
