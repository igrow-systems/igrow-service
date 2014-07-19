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

import com.argusat.gjl.locservice.session.LocatorSessionCache;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.BeginLocatorSessionResponse;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionRequest;
import com.argusat.gjl.service.locator.LocatorProtoBuf.EndLocatorSessionResponse;

// The Java class will be hosted at the URI path "/locatorsessions"
@Path("/locatorsessions")
public class LocatorSessions {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocatorSessions.class);

	private static LocatorSessionCache mLocatorSessionCache = null;

	static {
			mLocatorSessionCache = new LocatorSessionCache();
		
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

		return null;
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
