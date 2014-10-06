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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

	private static final int CORE_POOL_SIZE = 5;

	private static final int AGGREGATION_DELAY = 5000; // 5s (5000ms)

	@Inject
	private DeviceRepository mDeviceRepository;

	@Inject
	private Subscriber<Observation> mSubscriber;

	private class ObservationRunnablePair {
		public Runnable runnable;
		public com.argusat.gjl.model.Observation observation;
	}

	private Map<String, ObservationRunnablePair> mLatestObservations;

	private ScheduledExecutorService mScheduledExecutorService;

	private class UpdateLastKnownLocationRunnable implements Runnable {

		private final String mDeviceId;

		public UpdateLastKnownLocationRunnable(String deviceId) {
			mDeviceId = deviceId;
		}

		@Override
		public void run() {

			final com.argusat.gjl.model.Observation observation = mLatestObservations
					.get(mDeviceId).observation;

			LOGGER.info(String.format(
					"Updating last known location of device [ %s ] to %s",
					mDeviceId, observation.getLocation().toString()));

			final Device device = mDeviceRepository.findDevice(observation
					.getDeviceId());

			assert (device != null);
			device.setLastKnownLocation(observation.getLocation());

			try {
				mDeviceRepository.storeDevice(device);
			} catch (DeviceRepositoryException e) {
				LOGGER.error("Couldn't store device", e);
			}
			// remove the mapping so any subsequent update
			// for this device causes a new runnable to be scheduled
			mLatestObservations.remove(mDeviceId);
		}

	}

	private static class SingletonHelper {
		private static final ObservationListener INSTANCE = new ObservationListener();
	}

	public ObservationListener() {
		mLatestObservations = new ConcurrentHashMap<String, ObservationRunnablePair>();
		mScheduledExecutorService = Executors
				.newScheduledThreadPool(CORE_POOL_SIZE);
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
		if (observation.getType() != com.argusat.gjl.model.Observation.ObservationType.TYPE_LOCATION_ONLY) {
			// only interested in location updates
			return;
		}
		
		final String deviceId = observation.getDeviceId();
		LOGGER.debug(deviceId);

		ObservationRunnablePair observationRunnablePair = mLatestObservations
				.get(deviceId);
		if (observationRunnablePair == null) {

			// If we haven't seen an update for this device
			// create the runnable, schedule it and store
			// it along with the actual observation
			final Runnable updateLastKnownLocationRunnable = new UpdateLastKnownLocationRunnable(
					deviceId);
			observationRunnablePair = new ObservationRunnablePair();
			observationRunnablePair.observation = observation;
			observationRunnablePair.runnable = updateLastKnownLocationRunnable;

			mScheduledExecutorService.schedule(updateLastKnownLocationRunnable,
					AGGREGATION_DELAY, TimeUnit.MILLISECONDS);
		} else {
			// otherwise just update the observation
			observationRunnablePair.observation = observation;
		}
		mLatestObservations.put(deviceId, observationRunnablePair);

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
