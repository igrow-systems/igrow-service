/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSession.java        
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


package com.argusat.gjl.locservice.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.argusat.gjl.model.Device;

public class LocatorSession {

	private UUID mSessionId;
	
	private final Map<String, Device> mParticipants;
	
	private final String mInitiatorDeviceId;

	protected LocatorSession(String deviceId) {
		mInitiatorDeviceId = deviceId;
		mParticipants = new HashMap<String, Device>();
	}
	
	public static LocatorSession newLocatorSession(String deviceId) {
		LocatorSession locatorSession = new LocatorSession(deviceId);
		locatorSession.newSessionId();
		return locatorSession;
	}
	
	protected void newSessionId() {
		mSessionId = UUID.randomUUID();
	}
	
}
