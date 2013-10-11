/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocationOnlyObservation.java        
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

public class LocationOnlyObservation extends Observation {

	public LocationOnlyObservation(
			ObservationProtoBuf.Observation observationProtoBuf) {
		super(observationProtoBuf);

		this.mType = ObservationType.TYPE_LOCATION_ONLY;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
	}

	public LocationOnlyObservation() {
		super();
		this.mType = ObservationType.TYPE_LOCATION_ONLY;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.LOCATION_ONLY);
	}

}
