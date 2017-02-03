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

package com.igrow.model;

import com.igrow.protobuf.observation.ObservationProtoBuf;

public abstract class Observation {

	public enum ObservationType {
		TYPE_LOCATION_ONLY, TYPE_ENVIRONMENTAL_SENSOR
	}

	public enum ModeType {
		PASSIVE, ACTIVE
	}

	protected long mTimestamp;

	protected ModeType mMode;

	protected ObservationType mType;

	protected Location mLocation;
	
	protected String mSensorId;

	protected float mValues[];

	protected boolean mDirty;

	protected boolean mValid;

	protected boolean mProtoBufValid;

	protected ObservationProtoBuf.Observation mObservationProtoBuf;

	protected ObservationProtoBuf.Observation.Builder mObservationProtoBufBuilder;

	protected Observation() {
		mSensorId = null;
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
		
		mObservationProtoBuf = observationProtoBuf;
		mObservationProtoBufBuilder = ObservationProtoBuf.Observation
				.newBuilder(observationProtoBuf);
		
		mSensorId = observationProtoBuf.getSensorId();
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
		case TYPE_ENVIRONMENTAL_SENSOR:
			return new EnvironmentalSensorObservation();
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
		case ENVIRONMENTAL_SENSOR:
			return new EnvironmentalSensorObservation(observationProtoBuf);
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
	
	public String getSensorId() {
		return mSensorId;
	}

	public void setSensorId(String sensorId) {
		mSensorId = sensorId;
		mObservationProtoBufBuilder.setSensorId(sensorId);
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
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		sb.append(mTimestamp);
		sb.append(" ");
		sb.append(mLocation);
		sb.append(" ");
		sb.append(mSensorId);
		sb.append(" ]");
		
		return sb.toString();
		
		
	}

}
