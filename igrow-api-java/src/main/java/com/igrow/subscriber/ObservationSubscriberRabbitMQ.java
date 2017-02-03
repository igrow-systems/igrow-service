/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationSubscriberRabbitMQ.java        
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

package com.igrow.subscriber;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.protobuf.observation.ObservationProtoBuf.Observation;

public class ObservationSubscriberRabbitMQ extends
		SubscriberRabbitMQ<Observation> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ObservationSubscriberRabbitMQ.class);

	@Override
	Observation deserialiseMessage(ByteArrayInputStream bais) {
		Observation protoBuf = null;
		try {
			protoBuf = Observation.newBuilder().mergeFrom(bais).build();
		} catch (IOException e) {
			LOGGER.error("Failed to parse Observation protocol buffer", e);
		}
		return protoBuf;
	}

}
