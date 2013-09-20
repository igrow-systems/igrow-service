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

import com.argusat.gjl.service.observation.ObservationProtoBuf;

public class Location {

	private float latitude;
	
	private float longitude;
	
	private float altitude;
	
	private float error;
	
	private ObservationProtoBuf.Observation.Location locationProtoBuf;
	
	public float getLatitude() {
		return latitude;
	}


	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}


	public float getLongitude() {
		return longitude;
	}


	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}


	public float getAltitude() {
		return altitude;
	}


	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}


	public float getError() {
		return error;
	}


	public void setError(float error) {
		this.error = error;
	}
	
	public ObservationProtoBuf.Observation.Location getProtoBuf() {
		return locationProtoBuf;
	}
}
