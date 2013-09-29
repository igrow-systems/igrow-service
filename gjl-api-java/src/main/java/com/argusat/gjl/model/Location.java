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

	private LocationProtoBuf.Location locationProtoBuf;

	public float getLatitude() {
		return mLatitude;
	}

	public void setLatitude(float latitude) {
		this.mLatitude = latitude;
	}

	public float getLongitude() {
		return mLongitude;
	}

	public void setLongitude(float longitude) {
		this.mLongitude = longitude;
	}

	public float getAltitude() {
		return mAltitude;
	}

	public void setAltitude(float altitude) {
		this.mAltitude = altitude;
	}

	public float getHDOP() {
		return mHDOP;
	}

	public void setHDOP(float hdop) {
		this.mHDOP = hdop;
	}

	public float getVDOP() {
		return mVDOP;
	}

	public void setVDOP(float hdop) {
		this.mVDOP = hdop;
	}

	public LocationProtoBuf.Location getProtoBuf() {
		return locationProtoBuf;
	}
}
