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

package com.igrow.locservice.session;

import com.igrow.model.Device;
import com.igrow.model.Location;
import com.igrow.protobuf.locator.LocatorSessionInfoProtoBuf.LocatorSessionInfo;
import com.igrow.protobuf.locator.LocatorSessionInfoProtoBuf.LocatorSessionInfo.Participant.ParticipantStatus;

public class Participant {

	public enum ParticipantState {
		CREATED, AWAITING_RESPONSE, FAILED_TO_NOTIFY, REFUSED, ACCEPTED, EXPIRED
	}

	private ParticipantState mCurrentState;

	private long mLastUpdateTimestamp;

	private Device mDevice;
	
	private float mAverageSignalStrength;

	protected Participant() {
		mCurrentState = ParticipantState.CREATED;
	}

	public static Participant create(Device device) {
		Participant participant = new Participant();
		participant.setDevice(device);
		return participant;
	}

	public ParticipantState getCurrentState() {
		return mCurrentState;
	}

	public void setCurrentState(ParticipantState currentState) {
		this.mCurrentState = currentState;
	}

	public String getDeviceId() {
		return mDevice.getDeviceId();
	}

	public long getLastUpdateTimestamp() {
		return mLastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
		this.mLastUpdateTimestamp = lastUpdateTimestamp;
	}

	public Device getDevice() {
		return mDevice;
	}

	protected void setDevice(Device mDevice) {
		this.mDevice = mDevice;
	}

	public float getAverageSignalStrength() {
		return mAverageSignalStrength;
	}

	public void setAverageSignalStrength(float averageSignalStrength) {
		this.mAverageSignalStrength = averageSignalStrength;
	}

	public LocatorSessionInfo.Participant getProtoBuf() {

		LocatorSessionInfo.Participant.Builder participantBuilder = LocatorSessionInfo.Participant
				.newBuilder();

		participantBuilder.setDeviceId(getDeviceId());

		Location lastKnownLocation = getDevice().getLastKnownLocation();
		if (lastKnownLocation != null) {
			participantBuilder.setLocation(lastKnownLocation
					.getLocationProtoBuf());
		}
		
		participantBuilder.setLastUpdateTimestamp(getLastUpdateTimestamp());

		switch (getCurrentState()) {
		case ACCEPTED:
			participantBuilder.setParticipantStatus(ParticipantStatus.ACTIVE);
			break;
		case AWAITING_RESPONSE:
			participantBuilder.setParticipantStatus(ParticipantStatus.NOTIFIED);
			break;
		case CREATED:
			participantBuilder.setParticipantStatus(ParticipantStatus.STOPPED);
			break;
		case EXPIRED:
			participantBuilder.setParticipantStatus(ParticipantStatus.STOPPED);
			break;
		case FAILED_TO_NOTIFY:
			participantBuilder.setParticipantStatus(ParticipantStatus.STOPPED);
			break;
		case REFUSED:
			participantBuilder.setParticipantStatus(ParticipantStatus.STOPPED);
			break;
		default:
			break;
		}
		
		participantBuilder.setAverageSignalStrength(getAverageSignalStrength());

		return participantBuilder.build();
	}
}
