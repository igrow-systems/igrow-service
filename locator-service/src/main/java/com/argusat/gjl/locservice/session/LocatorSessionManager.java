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
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.ObservationListener;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.subscriber.MessageHandler;
import com.argusat.gjl.subscriber.Subscriber;
import com.argusat.gjl.subscriber.SubscriberRabbitMQ;

public class LocatorSessionManager implements Map<String, LocatorSession>,
		MessageHandler<Observation>, Closeable {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessionManager.class);

	private final Map<String, LocatorSession> locatorSessions = new ConcurrentHashMap<String, LocatorSession>();

	@Inject
	private Subscriber<Observation> mSubscriber;

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

			mSubscriber.registerMessageHandler(this);
			mSubscriber.connect();
			mSubscriber.subscribe("observation.*");

		} catch (IOException e) {
			LOGGER.error("Subscriber couldn't connect to broker", e);
		}
	}

	@Override
	@PreDestroy
	public void close() throws IOException {

		LOGGER.info("Closing ObservationsListener");

		if (mSubscriber != null) {
			mSubscriber.unsubscribe("observation.*");
			mSubscriber.unregisterMessageHandler(this);
			mSubscriber.close();
		}

	}

	@Override
	public void clear() {
		locatorSessions.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return locatorSessions.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return locatorSessions.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, LocatorSession>> entrySet() {
		return locatorSessions.entrySet();
	}

	@Override
	public LocatorSession get(Object key) {
		return locatorSessions.get(key);
	}

	@Override
	public boolean isEmpty() {
		return locatorSessions.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return locatorSessions.keySet();
	}

	@Override
	public LocatorSession put(String key, LocatorSession value) {
		return locatorSessions.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends LocatorSession> m) {
		locatorSessions.putAll(m);
	}

	@Override
	public LocatorSession remove(Object key) {
		return locatorSessions.remove(key);
	}

	@Override
	public int size() {
		return locatorSessions.size();
	}

	@Override
	public Collection<LocatorSession> values() {
		return locatorSessions.values();
	}

	@Override
	public void handleMessage(Observation observation) {

		String deviceId = observation.getDeviceId();
		if (locatorSessions.containsKey(deviceId)) {

			LocatorSession session = locatorSessions.get(deviceId);
			Participant participant = session.getParticipants().get(deviceId);
			//participant.s
			
		} else {
			LOGGER.warn(String.format(
					"Couldn't find session for device [ %s ]", deviceId));
		}

	}

}
