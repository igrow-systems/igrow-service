/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ThrowableMapper.java        
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

package com.igrow.observice;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(ThrowableMapper.class);
	
	public ThrowableMapper() {
		
	}

	@Override
	public Response toResponse(Throwable t) {
		
		LOGGER.error("Caught: ", t);
		
		return Response.status(500).
			      entity(t.getMessage()).
			      type("text/plain").
			      build();
	}

}
