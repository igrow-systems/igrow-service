/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessionInfoSubscriberRabbitMQ.java        
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.service.locator.LocatorSessionInfoProtoBuf.LocatorSessionInfo;

public class LocatorSessionInfoSubscriberRabbitMQ extends
		SubscriberRabbitMQ<LocatorSessionInfo> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LocatorSessionInfoSubscriberRabbitMQ.class);

	@Override
	LocatorSessionInfo deserialiseMessage(ByteArrayInputStream bais) {
		LocatorSessionInfo protoBuf = null;
		try {
			protoBuf = LocatorSessionInfo.newBuilder().mergeFrom(bais).build();
		} catch (IOException e) {
			LOGGER.error("Failed to parse LocatorSessionInfo protocol buffer", e);
		}
		return protoBuf;
	}

}
