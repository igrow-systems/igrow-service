/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationListener.java        
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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.glassfish.hk2.api.Immediate;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.DeviceRepositoryException;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.subscriber.MessageHandler;
import com.argusat.gjl.subscriber.Subscriber;
import com.argusat.gjl.subscriber.SubscriberRabbitMQ;

@Immediate
@Service
public class ObservationListener implements MessageHandler<Observation>,
		Closeable {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(ObservationListener.class);

	@Inject
	private DeviceRepository mDeviceRepository;

	@Inject
	private Subscriber<Observation> mSubscriber;

	private static class SingletonHelper {
		private static final ObservationListener INSTANCE = new ObservationListener();
	}

	public ObservationListener() {

	}

	public static ObservationListener getInstance() {
		return SingletonHelper.INSTANCE;
	}

	@Override
	public void handleMessage(Observation observationProtoBuf) {

		assert (observationProtoBuf != null);

		com.argusat.gjl.model.Observation observation = com.argusat.gjl.model.Observation
				.newObservation(observationProtoBuf);

		assert (observation != null);

		Device device = mDeviceRepository.findDevice(observation.getDeviceId());

		assert (device != null);
		device.setLastKnownLocation(observation.getLocation());

		try {
			mDeviceRepository.storeDevice(device);
		} catch (DeviceRepositoryException e) {
			LOGGER.error("Couldn't store device", e);
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

	@PostConstruct
	protected void connectSubscriber() {

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

}
