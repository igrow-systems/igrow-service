/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessions.java        
 *
 * Copyright (c) 2013 - 2014 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.argusat.gjl.locservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.Main;
import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufWriter;
import com.argusat.gjl.locservice.session.LocatorSession;
import com.argusat.gjl.locservice.session.LocatorSessionManager;
import com.argusat.gjl.locservice.subscriber.Subscriber;
import com.argusat.gjl.locservice.subscriber.rabbitmq.SubscriberRabbitMQ;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.service.device.DeviceProtoBuf;
import com.argusat.gjl.service.device.DeviceProtoBuf.FindLocalDevicesRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.FindLocalDevicesResponse;
import com.argusat.gjl.service.device.DeviceProtoBuf.NotifyDeviceRequest;
import com.argusat.gjl.service.device.DeviceProtoBuf.NotifyDeviceResponse;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse.ErrorCode;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

// The Java class will be hosted at the URI path "/locatorsessions"
@Path("/locatorsessions")
public class LocatorSessions {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessions.class);

	private LocatorSessionManager mLocatorSessionManager = null;

	private Subscriber mSubscriber = null;

	private Client mHttpClient = null;

	private ClientConfig mClientConfig;

	static {

	}

	public LocatorSessions() {

		mLocatorSessionManager = new LocatorSessionManager();
		mSubscriber = new SubscriberRabbitMQ();

		mClientConfig = new DefaultClientConfig();
		mClientConfig.getClasses().add(NotifyDeviceRequestProtobufReader.class);
		mClientConfig.getClasses().add(NotifyDeviceRequestProtobufWriter.class);
		mClientConfig.getClasses()
				.add(NotifyDeviceResponseProtobufReader.class);
		mClientConfig.getClasses()
				.add(NotifyDeviceResponseProtobufWriter.class);
		mClientConfig.getClasses().add(
				FindLocalDevicesRequestProtobufReader.class);
		mClientConfig.getClasses().add(
				FindLocalDevicesRequestProtobufWriter.class);
		mClientConfig.getClasses().add(
				FindLocalDevicesResponseProtobufReader.class);
		mClientConfig.getClasses().add(
				FindLocalDevicesResponseProtobufWriter.class);

		mHttpClient = Client.create(mClientConfig);
	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public BeginLocatorSessionResponse postLocatorSession(
			BeginLocatorSessionRequest beginLocatorSessionRequest) {

		BeginLocatorSessionResponse.Builder responseBuilder = BeginLocatorSessionResponse
				.newBuilder();
		String deviceId = beginLocatorSessionRequest.getDeviceId();

		if (mLocatorSessionManager.containsKey(deviceId)) {
			responseBuilder.setResponseCode(ErrorCode.SESSION_ALREADY_STARTED);
			responseBuilder
					.setResponseMessage("Session already started for device [ "
							+ deviceId + " ]");
			return responseBuilder.build();
		}

		FindLocalDevicesRequest.Builder requestBuilder = FindLocalDevicesRequest
				.newBuilder();
		requestBuilder.setCentre(beginLocatorSessionRequest.getLocation());
		requestBuilder.setRadius(1000L);
		requestBuilder.setLimit(5);

		WebResource r = mHttpClient.resource(Main.BASE_URI);
		WebResource wr = r.path("devices/local");
		FindLocalDevicesResponse response = wr.type("application/octet-stream")
				.post(DeviceProtoBuf.FindLocalDevicesResponse.class,
						requestBuilder.build());

		if (FindLocalDevicesResponse.ErrorCode.NONE == response
				.getResponseCode()) {

			LocatorSession locatorSession = LocatorSession
					.newLocatorSession(deviceId);

			for (DeviceProtoBuf.Device deviceProtoBuf : response
					.getDevicesList()) {
				// for each of the device we find, notify each one and
				// add it to the LocatorSession
				if (deviceProtoBuf.getDeviceId().equals(deviceId)) {
					// if the device we are doing this on behalf of
					// is this one in the returned list then don't
					// notify as we were the initiator
					continue;
				}

				NotifyDeviceRequest.Builder notifyDeviceBuilder = DeviceProtoBuf.NotifyDeviceRequest
						.newBuilder();
				notifyDeviceBuilder.setDeviceId(deviceProtoBuf.getDeviceId());
				notifyDeviceBuilder
						.setMessage("{\"msg_type\":\"begin_locator_session_request\" }");

				WebResource notificationsWr = r.path("notifications");
				NotifyDeviceResponse notifyDeviceResponse = notificationsWr
						.type("application/octet-stream").post(
								DeviceProtoBuf.NotifyDeviceResponse.class,
								notifyDeviceBuilder.build());

				if (notifyDeviceResponse.getResponseCode() != NotifyDeviceResponse.ErrorCode.NONE) {
					LOGGER.error(String.format(
							"Failed to notify device [ %s ] reponse was: %s ",
							deviceProtoBuf.getDeviceId(),
							notifyDeviceResponse.toString()));
				}

				locatorSession.addParticipant(Device.newDevice(deviceProtoBuf));

			}

			mLocatorSessionManager.put(deviceId, locatorSession);
		}

		if (FindLocalDevicesResponse.ErrorCode.NO_LOCAL_DEVICES == response
				.getResponseCode()) {
			responseBuilder
					.setResponseCode(ErrorCode.INSUFFICIENT_LOCAL_CANDIDATES);
			return responseBuilder.build();
		}

		responseBuilder.setResponseCode(ErrorCode.NONE);
		return responseBuilder.build();
	}

	// The Java method will process HTTP DELETE requests
	@DELETE
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public EndLocatorSessionResponse deleteLocatorSession(
			EndLocatorSessionRequest endLocatorSessionRequest) {

		return null;
	}

}
