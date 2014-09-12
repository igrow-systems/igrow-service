/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationListenerFactory.java        
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

import java.io.IOException;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObservationListenerFactory implements Factory<ObservationListener> {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(ObservationListenerFactory.class);

	@Override
	public void dispose(ObservationListener observationListener) {
		try {
			observationListener.close();
		} catch (IOException e) {
			LOGGER.error("Failed to close ObservationListener", e);
		}
	}

	@Override
	public ObservationListener provide() {

		ObservationListener observationListener = null;
		
		observationListener = ObservationListener.getInstance();
		
		return observationListener;
	}

}
