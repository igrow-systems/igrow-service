/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)GnssChannelObservation.java        
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

public class GnssChannelObservation extends Observation {

	public int mPrn;

	private float mAzimuth;

	private float mElevation;

	private float mC0_N;

	public GnssChannelObservation() {
		super();
		this.mType = ObservationType.TYPE_GNSS_CHANNEL;
		this.mValues = new float[3];
	}
	
	public GnssChannelObservation(ObservationProtoBuf.Observation observationProtoBuf) {
		super(observationProtoBuf);
		this.mType = ObservationType.TYPE_GNSS_CHANNEL;
		this.mValues = new float[3];
	}
	
	public int getPrn() {
		return mPrn;
	}

	public void setPrn(int prn) {
		this.mPrn = prn;
	}

	public double getAzimuth() {
		return mAzimuth;
	}

	public void setAzimuth(float azimuth) {
		this.mAzimuth = azimuth;
		this.mValues[0] = azimuth;
	}

	public float getElevation() {
		return mElevation;
	}

	public void setElevation(float elevation) {
		this.mElevation = elevation;
		this.mValues[2] = elevation;
	}

	public float getC0_n() {
		return mC0_N;
	}

	public void setC0_n(float c0_n) {
		this.mC0_N = c0_n;
		this.mValues[2] = c0_n;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
