/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Observation.java        
 *
 * Copyright (c) 2013 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.argusat.gjl.model;

import com.argusat.gjl.service.observation.ObservationProtoBuf;

public abstract class Observation {
	
	public enum ObservationType {
		TYPE_LOCATION_ONLY,
		TYPE_ROTATION_VECTOR,
		TYPE_GNSS_CHANNEL
	}
	
	protected long mTimestamp;
	
	protected long mDeviceId;
	
	protected ObservationType mType;
	
	protected Location mLocation;

	protected float mValues[];
	
	private ObservationProtoBuf.Observation mObservationProtoBuf;
	
	public Observation()
	{
		mValues = null;
	}
	
	public static Observation newObservation(ObservationType type) {
		switch (type) {
		case TYPE_ROTATION_VECTOR:
			return new RotationVectorObservation();
		case TYPE_GNSS_CHANNEL:
			return new GnssChannelObservation();
		default:
			// TODO: throw exception
			return null;
		}
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long timestamp) {
		this.mTimestamp = timestamp;
	}

	public long getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(long deviceId) {
		this.mDeviceId = deviceId;
	}

	public ObservationType getType() {
		return mType;
	}

	public void setType(ObservationType type) {
		this.mType = type;
	}
	
	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(Location location) {
		this.mLocation = location;
	}

	public float[] getValues() {
		return mValues;
	}

	public ObservationProtoBuf.Observation getObservationProtoBuf() {
		return mObservationProtoBuf;
	}
	

}
