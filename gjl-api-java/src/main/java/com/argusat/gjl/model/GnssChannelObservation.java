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

public class GnssChannelObservation extends Observation {

	public int prn;

	public float azimuth;

	public float elevation;

	public float c0_n;

	public GnssChannelObservation() {
		this.mType = ObservationType.TYPE_GNSS_CHANNEL;
		this.mValues = new float[3];
	}
	
	public int getPrn() {
		return prn;
	}

	public void setPrn(int prn) {
		this.prn = prn;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
		this.mValues[0] = azimuth;
	}

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
		this.mValues[2] = elevation;
	}

	public float getC0_n() {
		return c0_n;
	}

	public void setC0_n(float c0_n) {
		this.c0_n = c0_n;
		this.mValues[2] = c0_n;
	}
	
	

}
