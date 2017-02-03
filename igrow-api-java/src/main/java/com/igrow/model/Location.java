/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Location.java        
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.igrow.protobuf.observation.LocationProtoBuf;

public class Location {

	private float mLatitude;

	private float mLongitude;

	private float mAltitude;

	private float mHDOP;

	private float mVDOP;

	private boolean mDirty;

	private boolean mValid;

	private boolean mProtoBufValid;

	private LocationProtoBuf.Location mLocationProtoBuf;

	private LocationProtoBuf.Location.Builder mLocationProtoBufBuilder;

	public Location() {
		mValid = false;
		mProtoBufValid = false;
		mDirty = true;
		mLocationProtoBufBuilder = LocationProtoBuf.Location.newBuilder();
		mLatitude = 0.0f;
		mLongitude = 0.0f;
		mAltitude = 0.0f;
		mHDOP = 0.0f;
		mVDOP = 0.0f;
	}

	public Location(LocationProtoBuf.Location location) {
		assert (location.isInitialized());
		mLocationProtoBuf = location;
		mLocationProtoBufBuilder = LocationProtoBuf.Location
				.newBuilder(location);
		mLatitude = (float) location.getLatitude() / (float) 1e6;
		mLongitude = (float) location.getLongitude() / (float) 1e6;
		mAltitude = (float) location.getAltitude() / (float) 1e6;
		mHDOP = location.getHdop();
		mVDOP = location.getVdop();
	}

	public float getLatitude() {
		return mLatitude;
	}

	public void setLatitude(float latitude) {
		this.mLatitude = latitude;
		mLocationProtoBufBuilder.setLatitude((int) (latitude * 1e6));
		mDirty = true;
	}

	public float getLongitude() {
		return mLongitude;
	}

	public void setLongitude(float longitude) {
		this.mLongitude = longitude;
		mLocationProtoBufBuilder.setLongitude((int) (longitude * 1e6));
		mDirty = true;
	}

	public float getAltitude() {
		return mAltitude;
	}

	public void setAltitude(float altitude) {
		this.mAltitude = altitude;
		mLocationProtoBufBuilder.setAltitude((int) (altitude * 1e6));
		mDirty = true;
	}

	public float getHDOP() {
		return mHDOP;
	}

	public void setHDOP(float hdop) {
		this.mHDOP = hdop;
		mLocationProtoBufBuilder.setHdop((int) hdop);
		mDirty = true;
	}

	public float getVDOP() {
		return mVDOP;
	}

	public void setVDOP(float vdop) {
		this.mVDOP = vdop;
		mLocationProtoBufBuilder.setVdop((int) vdop);
		mDirty = true;
	}

	protected void validate() {

		mLocationProtoBuf = mLocationProtoBufBuilder.buildPartial();
		mProtoBufValid = mLocationProtoBuf.isInitialized();
		mValid = mProtoBufValid;
		mDirty = false;

	}

	public boolean isValid() {
		if (mDirty) {
			validate();
		}
		return mValid;
	}

	public LocationProtoBuf.Location getLocationProtoBuf() {
		if (mDirty) {
			validate();
		}
		return mLocationProtoBuf;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("( ");
		sb.append(mLatitude);
		sb.append(", ");
		sb.append(mLongitude);
		sb.append(", ");
		sb.append(mAltitude);
		sb.append(" ( ");
		sb.append(mHDOP);
		sb.append(", ");
		sb.append(mVDOP);
		sb.append(" ))");

		return sb.toString();

	}

	public int hashCode() {
		return new HashCodeBuilder(15, 39)
				.
				// if deriving: appendSuper(super.hashCode()).
				append(mLatitude).append(mLongitude).append(mAltitude)
				.append(mHDOP).append(mVDOP).toHashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Location)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		Location rhs = (Location) obj;
		return new EqualsBuilder()
				.
				// if deriving: appendSuper(super.equals(obj)).
				append(mLatitude, rhs.mLatitude)
				.append(mLongitude, rhs.mLongitude)
				.append(mAltitude, rhs.mAltitude).append(mHDOP, rhs.mHDOP)
				.append(mVDOP, rhs.mVDOP).isEquals();
	}

}
