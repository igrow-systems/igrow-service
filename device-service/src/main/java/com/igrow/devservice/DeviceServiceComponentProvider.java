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

package com.igrow.devservice;

import java.util.Set;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.spi.ComponentProvider;

public class DeviceServiceComponentProvider implements ComponentProvider {

	@SuppressWarnings("unused")
	private ServiceLocator mServiceLocator;
	
	public DeviceServiceComponentProvider() {
		
	}

	@Override
	public boolean bind(Class<?> arg0, Set<Class<?>> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void done() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(ServiceLocator serviceLocator) {
		mServiceLocator = serviceLocator;
	}

}
