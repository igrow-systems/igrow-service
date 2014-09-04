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

package com.argusat.gjl.locservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.locservice.session.LocatorSession;
import com.argusat.gjl.locservice.session.LocatorSessionManger;
import com.argusat.gjl.locservice.subscriber.Subscriber;
import com.argusat.gjl.locservice.subscriber.rabbitmq.SubscriberRabbitMQ;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse.ErrorCode;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionResponse;

// The Java class will be hosted at the URI path "/locatorsessions"
@Path("/locatorsessions")
public class LocatorSessions {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocatorSessions.class);

	private static LocatorSessionManger mLocatorSessionManager = null;
	
	private static Subscriber mSubscriber = null;

	static {
		mLocatorSessionManager = new LocatorSessionManger();
		mSubscriber = new SubscriberRabbitMQ();
	}

	public LocatorSessions() {

	}

	// The Java method will process HTTP POST requests
	@POST
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public BeginLocatorSessionResponse postLocatorSession(
			BeginLocatorSessionRequest beginLocatorSessionRequest) {

		
		
		BeginLocatorSessionResponse.Builder responseBuilder = BeginLocatorSessionResponse.newBuilder();
		String deviceId = beginLocatorSessionRequest.getDeviceId();
		
		if (mLocatorSessionManager.containsKey(deviceId)) {
			responseBuilder.setResponseCode(ErrorCode.SESSION_ALREADY_STARTED);
			responseBuilder.setResponseMessage("Session already started for device [ " + deviceId + " ]");
			return responseBuilder.build();
		}
		
		
		
		LocatorSession locatorSession = LocatorSession.newLocatorSession(deviceId);
		
		mLocatorSessionManager.put(deviceId, locatorSession);
		
		responseBuilder.setResponseCode(ErrorCode.NONE);
		
		return responseBuilder.build();
	}

	// The Java method will process HTTP DELETE requests
	@DELETE
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Produces("application/octet-stream")
	// The Java method will produce content identified by the MIME Media
	// type "application/octet-stream"
	@Consumes("application/octet-stream")
	public EndLocatorSessionResponse deleteLocatorSession(
			EndLocatorSessionRequest endLocatorSessionRequest) {

		return null;
	}

}
