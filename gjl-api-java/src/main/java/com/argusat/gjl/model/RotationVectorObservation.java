/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)RotationVectorObservation.java        
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


public class RotationVectorObservation extends Observation {

	public RotationVectorObservation() {
		
		super();
		
		this.mType = ObservationType.TYPE_ROTATION_VECTOR;
	}
	
	protected RotationVectorObservation(ObservationProtoBuf.Observation observationProtoBuf) {
		
		super(observationProtoBuf);
		
		this.mType = ObservationType.TYPE_ROTATION_VECTOR;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
