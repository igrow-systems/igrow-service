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

package com.igrow.locservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.protobuf.device.DeviceProtoBuf;
import com.igrow.protobuf.device.DeviceProtoBuf.FindLocalDevicesRequest;
import com.igrow.protobuf.device.DeviceProtoBuf.FindLocalDevicesResponse;
import com.igrow.protobuf.device.DeviceProtoBuf.NotifyDeviceRequest;
import com.igrow.protobuf.device.DeviceProtoBuf.NotifyDeviceResponse;
import com.igrow.protobuf.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.igrow.protobuf.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.igrow.protobuf.locator.LocatorProtoBuf.BeginLocatorSessionResponse.ErrorCode;
import com.igrow.PushMessaging;
import com.igrow.devservice.providers.FindLocalDevicesRequestProtobufReader;
import com.igrow.devservice.providers.FindLocalDevicesRequestProtobufWriter;
import com.igrow.devservice.providers.FindLocalDevicesResponseProtobufReader;
import com.igrow.devservice.providers.FindLocalDevicesResponseProtobufWriter;
import com.igrow.devservice.providers.NotifyDeviceRequestProtobufReader;
import com.igrow.devservice.providers.NotifyDeviceRequestProtobufWriter;
import com.igrow.devservice.providers.NotifyDeviceResponseProtobufReader;
import com.igrow.devservice.providers.NotifyDeviceResponseProtobufWriter;
import com.igrow.locservice.session.LocatorSession;
import com.igrow.locservice.session.LocatorSessionManager;
import com.igrow.locservice.session.Participant;
import com.igrow.locservice.session.LocatorSession.SessionStatus;
import com.igrow.model.Device;
import com.igrow.protobuf.locator.LocatorProtoBuf.EndLocatorSessionResponse;
import com.igrow.protobuf.locator.LocatorProtoBuf.JoinLocatorSessionRequest;
import com.igrow.protobuf.locator.LocatorProtoBuf.JoinLocatorSessionResponse;

// The Java class will be hosted at the URI path "/locatorsessions"
@Path("/locatorsessions")
public class LocatorSessions {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessions.class);

	@Inject
	private LocatorSessionManager mLocatorSessionManager = null;

	@Inject
	private ServiceLocator mServiceLocator;

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
			// an arbitrary member of any session, not just an initiator as
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
		requestBuilder.setRadius(5000L);
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

		LOGGER.info(response.toString());

		if (FindLocalDevicesResponse.ErrorCode.NONE == response
				.getResponseCode()) {

			// I expect there is a 'better' or cleaner
			// way of doing this with HK2 using qualifiers
			// or something similar
			LocatorSession locatorSession = LocatorSession
					.newLocatorSession(deviceId);
			// inject the dependencies and postConstruct
			try {
				mServiceLocator.inject(locatorSession);
				mServiceLocator.postConstruct(locatorSession);
			} catch (Exception e) {
				LOGGER.error("Failed to inject dependencies ", e);
				// can't do anything to recover at this point
				responseBuilder
						.setResponseCode(BeginLocatorSessionResponse.ErrorCode.INTERNAL_ERROR);
				responseBuilder.setResponseMessage(e.toString());
				return responseBuilder.build();
			}

			for (DeviceProtoBuf.Device deviceProtoBuf : response
					.getDevicesList()) {

				Participant participant = Participant.create(Device
						.newDevice(deviceProtoBuf));
				locatorSession.addParticipant(participant);
				mLocatorSessionManager.put(participant.getDeviceId(),
						locatorSession);

				LOGGER.info(String.format(
						"Added device [ %s ] to session [ %s ]", participant
								.getDeviceId(), locatorSession.getSessionId()
								.toString()));

				if (deviceProtoBuf.getDeviceId().equals(deviceId)) {
					// if the device we are doing this on behalf of
					// is this one in the returned list then don't
					// notify as we were the initiator
					continue;
				}

				NotifyDeviceRequest.Builder notifyDeviceBuilder = DeviceProtoBuf.NotifyDeviceRequest
						.newBuilder();
				notifyDeviceBuilder.setDeviceId(deviceProtoBuf.getDeviceId());
				notifyDeviceBuilder.setMessage(String.format(
						"{\"msg_type\":\"%s\",\"session_id\":\"%s\" }",
						// .format("{msg_type:begin_locator_session_request,session_id:%s }",
						PushMessaging.MESSAGE_TYPE_REQUEST_NEW_SESSION,
						locatorSession.getSessionId().toString()));

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

			responseBuilder.setSessionId(locatorSession.getSessionId()
					.toString());
			responseBuilder.setResponseCode(ErrorCode.NONE);

		}

		if (FindLocalDevicesResponse.ErrorCode.NO_LOCAL_DEVICES == response
				.getResponseCode()) {
			responseBuilder
					.setResponseCode(ErrorCode.INSUFFICIENT_LOCAL_CANDIDATES);
		}

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

		JoinLocatorSessionResponse.Builder builder = JoinLocatorSessionResponse
				.newBuilder();
		builder.setResponseCode(JoinLocatorSessionResponse.ErrorCode.NONE);

		return builder.build();
	}

	// The Java method will process HTTP DELETE requests
	@DELETE
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	// @Consumes("application/octet-stream")
	@Path("/{session_id}")
	public EndLocatorSessionResponse deleteLocatorSession(
	/* EndLocatorSessionRequest endLocatorSessionRequest, */
	@PathParam("session_id") String sessionId) {

		EndLocatorSessionResponse.Builder responseBuilder = EndLocatorSessionResponse
				.newBuilder();
		try {
			// final String deviceId = endLocatorSessionRequest.getDeviceId();

			LOGGER.info(String.format(">endLocatorSession [ %s ] ", sessionId));

			final UUID sessionUuid = UUID.fromString(sessionId);
			final LocatorSession session = mLocatorSessionManager
					.getBySessionId(sessionUuid);

			if (session == null) {
				responseBuilder
						.setResponseCode(EndLocatorSessionResponse.ErrorCode.SESSION_INVALID);
			} else {

				WebTarget r = mClient.target(UriBuilder
						.fromUri(
								String.format("http://%s/", mProperties
										.getProperty("service.device.host")))
						.port(Integer.parseInt(mProperties
								.getProperty("service.device.port"))).build());

				for (Participant participant : session.getParticipants()
						.values()) {

					final String deviceId = participant.getDeviceId();

					if (session.getInitiatorDeviceId().equals(deviceId)) {
						// don't notify the originator of the request
						continue;
					}

					NotifyDeviceRequest.Builder notifyDeviceBuilder = DeviceProtoBuf.NotifyDeviceRequest
							.newBuilder();
					notifyDeviceBuilder.setDeviceId(participant.getDeviceId());
					notifyDeviceBuilder.setMessage(String.format(
							"{\"msg_type\":\"%s\",\"session_id\":\"%s\" }",
							// .format("{msg_type:begin_locator_session_request,session_id:%s }",
							PushMessaging.MESSAGE_TYPE_REQUEST_END_SESSION,
							session.getSessionId().toString()));

					WebTarget notificationsWr = r.path("notifications");
					NotifyDeviceResponse notifyDeviceResponse = notificationsWr
							.request("application/octet-stream").post(
									Entity.entity(notifyDeviceBuilder.build(),
											"application/octet-stream"),
									NotifyDeviceResponse.class);

					if (notifyDeviceResponse.getResponseCode() != NotifyDeviceResponse.ErrorCode.NONE) {

						LOGGER.error(String
								.format("Failed to notify device [ %s ] reponse was: %s ",
										deviceId,
										notifyDeviceResponse.toString()));
					}

				}

				LocatorSession locatorSession = mLocatorSessionManager
						.remove(sessionUuid);
				locatorSession.setSessionState(SessionStatus.STOPPED);

				responseBuilder
						.setResponseCode(EndLocatorSessionResponse.ErrorCode.NONE);
			}
		} catch (Exception e) {
			responseBuilder
					.setResponseCode(EndLocatorSessionResponse.ErrorCode.INTERNAL_ERROR);
			responseBuilder.setResponseMessage(e.toString());
		}

		return responseBuilder.build();
	}
}
