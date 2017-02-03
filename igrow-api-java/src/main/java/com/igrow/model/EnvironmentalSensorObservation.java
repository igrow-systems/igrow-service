/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocationOnlyObservation.java        
 *
 * Copyright (c) 2017 Argusat Limited
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

public class EnvironmentalSensorObservation extends Observation {

	public EnvironmentalSensorObservation(
			ObservationProtoBuf.Observation observationProtoBuf) {
		super(observationProtoBuf);

		mType = ObservationType.TYPE_LOCATION_ONLY;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
	}

	public EnvironmentalSensorObservation() {
		super();
		mType = ObservationType.TYPE_LOCATION_ONLY;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
	}

	@Override
	public String toString() {

		return super.toString();

	}
}
