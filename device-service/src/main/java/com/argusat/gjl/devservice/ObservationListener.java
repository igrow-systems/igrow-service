/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationsListener.java        
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.DeviceRepositoryException;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.subscriber.MessageHandler;
import com.argusat.gjl.subscriber.Subscriber;

@Service
public class ObservationListener implements MessageHandler, Closeable {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(ObservationListener.class);

	@Inject
	private DeviceRepository mDeviceRepository;

	@Inject
	private Subscriber mSubscriber;

	public ObservationListener() {
		
	}

	@Override
	public void handleMessage(Observation observation) {

		Device device = mDeviceRepository.findDevice(observation.getDeviceId());

		device.setLastKnownLocation(observation.getLocation());

		try {
			mDeviceRepository.storeDevice(device);
		} catch (DeviceRepositoryException e) {
			LOGGER.error("Couldn't store device", e);
		}

	}

	@Override
	public void close() throws IOException {

		LOGGER.info("Closing ObservationsListener");

		if (mSubscriber != null) {
			mSubscriber.unsubscribe("*");
			mSubscriber.unregisterMessageHandler();
			mSubscriber.close();
		}

	}

	@PostConstruct
	protected void connectSubscriber() {
		try {
			mSubscriber.registerMessageHandler(this);
			mSubscriber.connect();
			mSubscriber.subscribe("*");
		} catch (IOException e) {
			LOGGER.error("Subscriber couldn't connect to broker", e);
		}
	}

}
