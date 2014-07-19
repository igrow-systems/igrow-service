/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Participant.java        
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

public class Participant {

	public enum ParticipantState { 
		CREATED,
		AWAITING_RESPONSE,
		REFUSED,
		ACCEPTED,
		EXPIRED
	}
	
	private ParticipantState mCurrentState;
	
	private String mDeviceId;
	
	private long mLastUpdateTimestamp;

	public ParticipantState getmCurrentState() {
		return mCurrentState;
	}

	public void setCurrentState(ParticipantState currentState) {
		this.mCurrentState = currentState;
	}

	public String getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(String deviceId) {
		this.mDeviceId = deviceId;
	}

	public long getLastUpdateTimestamp() {
		return mLastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
		this.mLastUpdateTimestamp = lastUpdateTimestamp;
	}
	
	
}
