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
		TYPE_ROTATION_VECTOR,
		TYPE_GNSS_CHANNEL
	}
	
	protected long timestamp;
	
	protected ObservationType type;
	
	protected float values[];
	
	private ObservationProtoBuf.Observation observationProtoBuf;
	
	public Observation()
	{
		values = null;
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
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public ObservationType getType() {
		return type;
	}

	public void setType(ObservationType type) {
		this.type = type;
	}

	public float[] getValues() {
		return values;
	}

	public ObservationProtoBuf.Observation getObservationProtoBuf() {
		return observationProtoBuf;
	}
	

}
