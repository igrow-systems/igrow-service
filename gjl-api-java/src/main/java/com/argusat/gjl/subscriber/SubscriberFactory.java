/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)SubscriberFactory.java        
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

package com.argusat.gjl.subscriber;

import java.io.IOException;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriberFactory implements Factory<Subscriber> {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(SubscriberFactory.class);

	@Override
	public void dispose(Subscriber subscriber) {
		try {
			subscriber.close();
		} catch (IOException e) {
			LOGGER.error("Failed to close Subscriber", e);
		}
	}

	@Override
	public Subscriber provide() {

		Subscriber subscriber = null;

		subscriber = new SubscriberRabbitMQ();

		return subscriber;
	}

}
