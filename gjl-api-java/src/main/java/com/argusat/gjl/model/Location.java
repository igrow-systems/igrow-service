/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Location.java        
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

import com.argusat.gjl.service.observation.LocationProtoBuf;

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
	}
	
	public Location(LocationProtoBuf.Location location) {
		assert(location.isInitialized());
		mLocationProtoBuf = location;
		mLocationProtoBufBuilder = LocationProtoBuf.Location.newBuilder(location);
	}
	
	public float getLatitude() {
		return mLatitude;
	}

	public void setLatitude(float latitude) {
		this.mLatitude = latitude;
		mLocationProtoBufBuilder.setLatitude((int)latitude);
		mDirty = true;
	}

	public float getLongitude() {
		return mLongitude;
	}

	public void setLongitude(float longitude) {
		this.mLongitude = longitude;
		mLocationProtoBufBuilder.setLongitude((int)longitude);
		mDirty = true;
	}

	public float getAltitude() {
		return mAltitude;
	}

	public void setAltitude(float altitude) {
		this.mAltitude = altitude;
		mLocationProtoBufBuilder.setAltitude((int)altitude);
		mDirty = true;
	}

	public float getHDOP() {
		return mHDOP;
	}

	public void setHDOP(float hdop) {
		this.mHDOP = hdop;
		mLocationProtoBufBuilder.setHdop((int)hdop);
		mDirty = true;
	}

	public float getVDOP() {
		return mVDOP;
	}

	public void setVDOP(float vdop) {
		this.mVDOP = vdop;
		mLocationProtoBufBuilder.setVdop((int)vdop);
		mDirty = true;
	}
	
	protected void validate() {

		mLocationProtoBuf = mLocationProtoBufBuilder.buildPartial();
		mProtoBufValid = mLocationProtoBuf.isInitialized();
		mValid =  mProtoBufValid;
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
	
}
