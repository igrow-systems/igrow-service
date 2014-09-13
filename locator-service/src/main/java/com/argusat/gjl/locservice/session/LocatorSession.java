/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSession.java        
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

package com.argusat.gjl.locservice.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.publisher.Publisher;
import com.argusat.gjl.service.locator.LocatorSessionInfoProtoBuf.LocatorSessionInfo;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.subscriber.Subscriber;

public class LocatorSession {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocatorSession.class);

	public enum SessionStatus {
		CREATED, RUNNING, STOPPED
	}

	private UUID mSessionId;

	private SessionStatus mSessionState;

	private final String mInitiatorDeviceId;

	private final Map<String, Participant> mParticipants;

	private final Vector<GnssJammer> mGnssJammers;

	private final Timer mTimer;

	@Inject
	private Subscriber<Observation> mSubscriber;

	@Inject
	private Publisher mPublisher;

	private final class CalculateAndPublishTask extends TimerTask {

		@Override
		public void run() {

			calculate();

			if (mPublisher != null && mPublisher.isConnected()) {
				try {
					mPublisher.publish(String.format("session.%s", mSessionId),
							LocatorSessionInfo.class,
							getLocatorSessionInfoProtoBuf());
				} catch (Exception e) {
					LOGGER.error("Failed to publish LocatorSessionInfo", e);
				}
			}

		}

	}

	protected LocatorSession(String deviceId) {
		mSessionState = SessionStatus.CREATED;
		mInitiatorDeviceId = deviceId;
		mParticipants = new HashMap<String, Participant>();
		mGnssJammers = new Vector<GnssJammer>(1);
		mTimer = new Timer();
	}

	public static LocatorSession newLocatorSession(String deviceId) {
		LocatorSession locatorSession = new LocatorSession(deviceId);
		locatorSession.newSessionId();
		return locatorSession;
	}

	public SessionStatus getSessionState() {
		return mSessionState;
	}

	// TODO: this class is an ideal candidate for
	// using the GoF State pattern
	public void setSessionState(SessionStatus sessionState) {

		LOGGER.info(String.format(
				"LocatorSession [ %s ] state [ %s ] - > [ %s ]", mSessionId,
				mSessionState, sessionState));

		if (sessionState == SessionStatus.RUNNING
				&& mSessionState != SessionStatus.RUNNING) {
			mTimer.scheduleAtFixedRate(new CalculateAndPublishTask(), 5000L,
					5000L);
		}
		if (sessionState == SessionStatus.STOPPED
				&& mSessionState != SessionStatus.STOPPED) {
			if (mSubscriber != null) {
				for (Participant participant : mParticipants.values()) {
					final String topic = String.format("observation.%s",
							participant.getDeviceId());
					try {
						mSubscriber.unsubscribe(topic);
					} catch (IOException e) {
						LOGGER.error(
								String.format(
										"Session [ %s ] failed to unsubscribe from topic [ %s ]",
										mSessionId, topic), e);
					}
				}
			}

			if (mTimer != null) {
				mTimer.cancel();
			}
		}
		this.mSessionState = sessionState;
	}

	protected void newSessionId() {
		mSessionId = UUID.randomUUID();
	}

	public void addParticipant(Participant participant) {

		mParticipants.put(participant.getDeviceId(), participant);

		if (mSubscriber != null) {
			final String topic = String.format("observation.%s",
					participant.getDeviceId());
			try {
				mSubscriber.subscribe(topic);
			} catch (IOException e) {
				LOGGER.error(String.format(
						"Couldn't subscribe to topic [ %s ]", topic), e);
			}
		}
	}

	public UUID getSessionId() {
		return mSessionId;
	}

	public Map<String, Participant> getParticipants() {
		return mParticipants;
	}

	public Vector<GnssJammer> getGnssJammers() {
		return mGnssJammers;
	}

	public String getInitiatorDeviceId() {
		return mInitiatorDeviceId;
	}

	protected void calculate() {

		final Location meanCentre = new Location();

		double sumlat = 0.0d;
		double sumlon = 0.0d;
		int n = 0;
		for (Participant participant : mParticipants.values()) {

			Location lastKnownLocation = participant.getDevice()
					.getLastKnownLocation();
			if (lastKnownLocation != null) {

				sumlat += lastKnownLocation.getLatitude();
				sumlon += lastKnownLocation.getLongitude();
				++n;
			}
		}
		meanCentre.setLatitude((float) (sumlat / (double) n));
		meanCentre.setLongitude((float) (sumlon / (double) n));
		meanCentre.setAltitude(0.0f);
		meanCentre.setHDOP(15.0f);
		meanCentre.setVDOP(0.0f);

		// store the results
		mGnssJammers.clear();
		GnssJammer jammer = new GnssJammer();

		jammer.setLocation(meanCentre);
		mGnssJammers.add(jammer);
	}

	protected LocatorSessionInfo getLocatorSessionInfoProtoBuf() {

		LocatorSessionInfo.Builder builder = LocatorSessionInfo.newBuilder();
		builder.setSessionId(mSessionId.toString());

		switch (mSessionState) {
		case CREATED:
			builder.setSessionStatus(LocatorSessionInfo.SessionStatus.CREATED);
			break;
		case RUNNING:
			builder.setSessionStatus(LocatorSessionInfo.SessionStatus.RUNNING);
			break;
		case STOPPED:
			builder.setSessionStatus(LocatorSessionInfo.SessionStatus.STOPPED);
			break;
		default:
			break;
		}

		Vector<LocatorSessionInfo.Participant> participantProtoBufs = new Vector<LocatorSessionInfo.Participant>(
				mParticipants.size());
		for (Participant participant : mParticipants.values()) {

			participantProtoBufs.add(participant.getProtoBuf());

		}
		builder.addAllParticipant(participantProtoBufs);

		Vector<LocatorSessionInfo.GnssJammer> gnssJammerProtoBufs = new Vector<LocatorSessionInfo.GnssJammer>(
				mGnssJammers.size());
		for (GnssJammer jammer : mGnssJammers) {

			gnssJammerProtoBufs.add(jammer.getProtoBuf());

		}
		builder.addAllJammer(gnssJammerProtoBufs);

		return builder.build();

	}
}
