/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessionManger.java        
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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.ObservationListener;
import com.argusat.gjl.publisher.Publisher;
import com.argusat.gjl.publisher.PublisherRabbitMQ;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.subscriber.MessageHandler;
import com.argusat.gjl.subscriber.Subscriber;
import com.argusat.gjl.subscriber.SubscriberRabbitMQ;

public class LocatorSessionManager implements Map<String, LocatorSession>,
		MessageHandler<Observation>, Closeable {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessionManager.class);

	private final Map<String, LocatorSession> deviceIdMap = new ConcurrentHashMap<String, LocatorSession>();
	
	private final Map<UUID, LocatorSession> sessionIdMap = new ConcurrentHashMap<UUID, LocatorSession>();

	@Inject
	private Subscriber<Observation> mSubscriber;

	@Inject
	private Publisher mPublisher;

	public LocatorSessionManager() {

	}

	@PostConstruct
	public void initialise() {
		Properties properties = new Properties();
		InputStream entityStream = ObservationListener.class
				.getResourceAsStream("/"
						+ SubscriberRabbitMQ.PROPERTIES_FILENAME);

		if (entityStream == null) {
			LOGGER.error(String.format("Couldn't open properties file: %s",
					SubscriberRabbitMQ.PROPERTIES_FILENAME));
		}

		try {

			properties.load(entityStream);
			entityStream.close();

			mSubscriber.initialise(properties);

			//mSubscriber.registerMessageHandler(this);
			mSubscriber.connect();
			// mSubscriber.subscribe("observation.*");

		} catch (IOException e) {
			LOGGER.error("Subscriber couldn't connect to broker", e);
		}

		entityStream = PublisherRabbitMQ.class.getResourceAsStream("/"
				+ PublisherRabbitMQ.PROPERTIES_FILENAME);

		properties = new Properties();
		try {
			properties.load(entityStream);
			entityStream.close();
			mPublisher.initialise(properties);
			mPublisher.connect();
		} catch (IOException e) {
			LOGGER.error("Failed to initialise/connect Publisher", e);
		}
	}

	@Override
	@PreDestroy
	public void close() throws IOException {

		LOGGER.info("Closing ObservationsListener");

		if (mSubscriber != null) {
			mSubscriber.unregisterMessageHandler(this);
			mSubscriber.close();
		}
		
		if (mPublisher != null) {
			mPublisher.close();
		}

	}

	@Override
	public void clear() {
		deviceIdMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return deviceIdMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return deviceIdMap.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, LocatorSession>> entrySet() {
		return deviceIdMap.entrySet();
	}

	@Override
	public LocatorSession get(Object key) {
		return deviceIdMap.get(key);
	}
	
	public LocatorSession getBySessionId(UUID sessionId) {
		return sessionIdMap.get(sessionId);
	}

	@Override
	public boolean isEmpty() {
		return deviceIdMap.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return deviceIdMap.keySet();
	}

	@Override
	public LocatorSession put(String key, LocatorSession value) {
		sessionIdMap.put(value.getSessionId(), value);
		return deviceIdMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends LocatorSession> m) {
		// should really be doing this with *unique* locator sessions
		// but it won't do any harm.
		for (LocatorSession locatorSession : m.values()) {
			sessionIdMap.put(locatorSession.getSessionId(), locatorSession);
		}
		deviceIdMap.putAll(m);
	}

	@Override
	public LocatorSession remove(Object key) {
		return deviceIdMap.remove(key);
	}
	
	public LocatorSession remove(UUID sessionId) {
		final LocatorSession locatorSession = sessionIdMap.remove(sessionId);
		for (String deviceId : locatorSession.getParticipants().keySet()) {
			remove(deviceId);
		}
		return locatorSession;
	}

	@Override
	public int size() {
		return deviceIdMap.size();
	}

	@Override
	public Collection<LocatorSession> values() {
		return deviceIdMap.values();
	}

	@Override
	public void handleMessage(Observation observation) {

		String deviceId = observation.getDeviceId();
		if (deviceIdMap.containsKey(deviceId)) {

			LocatorSession session = deviceIdMap.get(deviceId);
			Participant participant = session.getParticipants().get(deviceId);
			// participant.s

		} else {
			LOGGER.warn(String.format(
					"Couldn't find session for device [ %s ]", deviceId));
		}

	}

}
