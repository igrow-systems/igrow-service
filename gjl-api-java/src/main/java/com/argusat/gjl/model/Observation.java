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
		TYPE_LOCATION_ONLY, TYPE_ROTATION_VECTOR, TYPE_GNSS_CHANNEL
	}

	public enum ModeType {
		PASSIVE, ACTIVE
	}

	protected long mTimestamp;

	protected long mDeviceId;

	protected ModeType mMode;

	protected ObservationType mType;

	protected Location mLocation;

	protected float mValues[];

	protected boolean mDirty;

	protected boolean mValid;

	protected boolean mProtoBufValid;

	private ObservationProtoBuf.Observation mObservationProtoBuf;

	protected ObservationProtoBuf.Observation.Builder mObservationProtoBufBuilder;

	public Observation() {
		mValues = null;
		mDirty = true;
		mProtoBufValid = false;
		mValid = false;
		mLocation = new Location();
		mObservationProtoBufBuilder = ObservationProtoBuf.Observation
				.newBuilder();
	}

	protected Observation(ObservationProtoBuf.Observation observationProtoBuf) {
		// mDeviceId = observationProtoBuf.getTimestamp();

		mObservationProtoBufBuilder = ObservationProtoBuf.Observation
				.newBuilder(observationProtoBuf);

		mTimestamp = observationProtoBuf.getTimestamp();
		mDeviceId = observationProtoBuf.getDeviceId();
		mLocation = new Location(observationProtoBuf.getLocation());
	}

	// Consolidate this to use only the protobuf enums for message/object type
	public static Observation newObservation(ObservationType type) {
		switch (type) {
		case TYPE_LOCATION_ONLY:
			return new LocationOnlyObservation();
		case TYPE_ROTATION_VECTOR:
			return new RotationVectorObservation();
		case TYPE_GNSS_CHANNEL:
			return new GnssChannelObservation();
		default:
			// TODO: throw exception
			return null;
		}
	}

	public static Observation newObservation(
			ObservationProtoBuf.Observation observationProtoBuf) {

		assert (observationProtoBuf.isInitialized());

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
		this.mTimestamp = timestamp;
		mObservationProtoBufBuilder.setTimestamp(timestamp);
		mDirty = true;
	}

	public long getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(long deviceId) {
		this.mDeviceId = deviceId;
		mObservationProtoBufBuilder.setDeviceId(deviceId);
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

	public float[] getValues() {
		return mValues;
	}

	protected void validate() {
		mObservationProtoBuf = mObservationProtoBufBuilder.buildPartial();
		mProtoBufValid = mObservationProtoBuf.isInitialized();
		mValid = mProtoBufValid && mLocation != null && mLocation.isValid();
		mDirty = false;
	}

	public boolean isValid() {
		if (mDirty) {
			validate();
		}
		return mValid;
	}

	public ObservationProtoBuf.Observation getObservationProtoBuf() {
		if (mDirty) {
			validate();
		}
		return mObservationProtoBuf;
	}

}
