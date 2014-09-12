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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufReader;
import com.argusat.gjl.devservice.providers.FindLocalDevicesResponseProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceRequestProtobufWriter;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufReader;
import com.argusat.gjl.devservice.providers.NotifyDeviceResponseProtobufWriter;
import com.argusat.gjl.locservice.session.LocatorSession;
import com.argusat.gjl.locservice.session.LocatorSession.SessionStatus;
import com.argusat.gjl.locservice.session.LocatorSessionManager;
import com.argusat.gjl.locservice.session.Participant;
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
import com.argusat.gjl.service.locator.LocatorProtoBuf.JoinLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.JoinLocatorSessionResponse;
import com.argusat.gjl.subscriber.Subscriber;

// The Java class will be hosted at the URI path "/locatorsessions"
@Path("/locatorsessions")
public class LocatorSessions {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessions.class);

	@Inject
	private LocatorSessionManager mLocatorSessionManager = null;

	@Inject
	private Subscriber mSubscriber;

	private Client mClient = null;

	private ClientConfig mClientConfig;

	private static final String PROPERTIES_FILENAME = "locator-service.properties";

	private final Properties mProperties = new Properties();

	static {

	}

	public LocatorSessions() throws IOException {

		InputStream entityStream = LocatorSessions.class
				.getResourceAsStream("/" + PROPERTIES_FILENAME);

		mProperties.load(entityStream);
		entityStream.close();

		mLocatorSessionManager = new LocatorSessionManager();

		mClientConfig = new ClientConfig();
		mClientConfig.register(NotifyDeviceRequestProtobufReader.class);
		mClientConfig.register(NotifyDeviceRequestProtobufWriter.class);
		mClientConfig.register(NotifyDeviceResponseProtobufReader.class);
		mClientConfig.register(NotifyDeviceResponseProtobufWriter.class);
		mClientConfig.register(FindLocalDevicesRequestProtobufReader.class);
		mClientConfig.register(FindLocalDevicesRequestProtobufWriter.class);
		mClientConfig.register(FindLocalDevicesResponseProtobufReader.class);
		mClientConfig.register(FindLocalDevicesResponseProtobufWriter.class);

		mClient = ClientBuilder.newClient(mClientConfig);
	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public BeginLocatorSessionResponse beginLocatorSession(
			BeginLocatorSessionRequest beginLocatorSessionRequest) {

		BeginLocatorSessionResponse.Builder responseBuilder = BeginLocatorSessionResponse
				.newBuilder();
		String deviceId = beginLocatorSessionRequest.getDeviceId();

		LOGGER.info(String.format(">beginLocatorSession [ %s ]", deviceId));

		if (mLocatorSessionManager.containsKey(deviceId)) {
			// actually this will happen if the device is already
			// a member of any session, not just an initiator as
			// implied
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

		WebTarget r = mClient
				.target(UriBuilder
						.fromUri(
								String.format("http://%s/", mProperties
										.getProperty("service.device.host")))
						.port(Integer.parseInt(mProperties
								.getProperty("service.device.port"))).build());
		WebTarget wr = r.path("devices/local");
		FindLocalDevicesResponse response = wr.request(
				"application/octet-stream").post(
				Entity.entity(requestBuilder.build(),
						"application/octet-stream"),
				FindLocalDevicesResponse.class);

		if (FindLocalDevicesResponse.ErrorCode.NONE == response
				.getResponseCode()) {

			LocatorSession locatorSession = LocatorSession
					.newLocatorSession(deviceId);

			for (DeviceProtoBuf.Device deviceProtoBuf : response
					.getDevicesList()) {

				Participant participant = Participant.create(Device
						.newDevice(deviceProtoBuf));
				locatorSession.addParticipant(participant);
				mLocatorSessionManager.put(participant.getDeviceId(),
						locatorSession);

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
						.setMessage(String
								.format("{\"msg_type\":\"begin_locator_session_request\",\"session_id\":\"%s\" }",
								// .format("{msg_type:begin_locator_session_request,session_id:%s }",
										locatorSession.getSessionId()
												.toString()));

				WebTarget notificationsWr = r.path("notifications");
				NotifyDeviceResponse notifyDeviceResponse = notificationsWr
						.request("application/octet-stream").post(
								Entity.entity(notifyDeviceBuilder.build(),
										"application/octet-stream"),
								NotifyDeviceResponse.class);

				if (notifyDeviceResponse.getResponseCode() != NotifyDeviceResponse.ErrorCode.NONE) {

					LOGGER.error(String.format(
							"Failed to notify device [ %s ] reponse was: %s ",
							deviceProtoBuf.getDeviceId(),
							notifyDeviceResponse.toString()));
				}

			}

			locatorSession.setSessionState(SessionStatus.RUNNING);

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

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	@Path("join")
	public JoinLocatorSessionResponse joinLocatorSession(
			JoinLocatorSessionRequest joinLocatorSessionRequest) {

		return null;
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
