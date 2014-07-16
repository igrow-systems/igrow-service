/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Observation.java        
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

package com.argusat.gjl.model;

import com.argusat.gjl.service.observation.ObservationProtoBuf;

public abstract class Observation {

	public enum ObservationType {
		TYPE_LOCATION_ONLY, TYPE_ROTATION_VECTOR, TYPE_GNSS_CHANNEL
	}

	public enum ModeType {
		PASSIVE, ACTIVE
	}

	protected long mTimestamp;

	protected ModeType mMode;

	protected ObservationType mType;

	protected Location mLocation;
	
	protected String mDeviceId;

	protected float mValues[];

	protected boolean mDirty;

	protected boolean mValid;

	protected boolean mProtoBufValid;

	protected ObservationProtoBuf.Observation mObservationProtoBuf;

	protected ObservationProtoBuf.Observation.Builder mObservationProtoBufBuilder;

	protected Observation() {
		mDeviceId = null;
		mValues = null;
		mDirty = true;
		mProtoBufValid = false;
		mValid = false;
		mLocation = new Location();
		mObservationProtoBuf = null;
		mObservationProtoBufBuilder = ObservationProtoBuf.Observation
				.newBuilder();
	}

	protected Observation(ObservationProtoBuf.Observation observationProtoBuf) {

		assert (observationProtoBuf.isInitialized());
		
		mObservationProtoBuf = null;
		mObservationProtoBufBuilder = ObservationProtoBuf.Observation
				.newBuilder(observationProtoBuf);
		
		mDeviceId = observationProtoBuf.getDeviceId();
		mTimestamp = observationProtoBuf.getTimestamp();
		mLocation = new Location(observationProtoBuf.getLocation());
		
		switch (observationProtoBuf.getMode()) {
		case ACTIVE:
			mMode = ModeType.ACTIVE;
			break;
		case PASSIVE:
			mMode = ModeType.PASSIVE;
			break;
		default:
			// TODO: throw exception
			break;
		}
		mDirty = false;
		mValid = true;
	}

	// Consolidate this to use only the protobuf enums for message/object type
	public static Observation newObservation(final ObservationType type) {
		switch (type) {
		case TYPE_LOCATION_ONLY:
			return new LocationOnlyObservation();
		case TYPE_ROTATION_VECTOR:
			return new RotationVectorObservation();
		case TYPE_GNSS_CHANNEL:
			return new GnssChannelObservation();
		default:
			throw new IllegalArgumentException("Unknown Observation type!");
		}
	}

	public static Observation newObservation(ObservationProtoBuf.Observation observationProtoBuf) {

		ObservationProtoBuf.Observation.ObservationType type = observationProtoBuf
				.getType();
		switch (type) {
		case LOCATION_ONLY:
			return new LocationOnlyObservation(observationProtoBuf);
		case ROTATION_VECTOR:
			return new RotationVectorObservation(observationProtoBuf);
		case C0N_GNSS:
			return new GnssChannelObservation(observationProtoBuf);
		default:
			// TODO: throw exception
			return null;
		}
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long timestamp) {
		mTimestamp = timestamp;
		mObservationProtoBufBuilder.setTimestamp(timestamp);
		mDirty = true;
	}

	public ObservationType getType() {
		return mType;
	}

	public ModeType getMode() {
		return mMode;
	}

	public void setMode(ModeType mMode) {
		this.mMode = mMode;
		switch (mMode) {
		case ACTIVE:
			mObservationProtoBufBuilder
					.setMode(ObservationProtoBuf.Observation.ModeType.ACTIVE);
			break;
		case PASSIVE:
			mObservationProtoBufBuilder
					.setMode(ObservationProtoBuf.Observation.ModeType.PASSIVE);
			break;
		}
		mDirty = true;
	}

	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(Location location) {
		this.mLocation = location;
		mObservationProtoBufBuilder.setLocation(location.getLocationProtoBuf());
		mDirty = true;
	}
	
	public String getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(String deviceId) {
		mDeviceId = deviceId;
		mObservationProtoBufBuilder.setDeviceId(deviceId);
		mDirty = true;
	}

	public float[] getValues() {
		return mValues;
	}
	
	public void setValues(float[] values) {
		
	}

	protected void validate() {
		mObservationProtoBuf = mObservationProtoBufBuilder.buildPartial();
		mProtoBufValid = mObservationProtoBuf.isInitialized();
		mValid = mProtoBufValid;
		mDirty = false;
	}

	public final boolean isValid() {
		if (mDirty) {
			validate();
		}
		return mValid;
	}

	public ObservationProtoBuf.Observation getObservationProtoBuf() {
		return mObservationProtoBuf;
	}

}
