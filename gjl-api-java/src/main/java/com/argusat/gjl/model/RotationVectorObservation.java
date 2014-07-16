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
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation.GeoMagneticObservation;

public class RotationVectorObservation extends Observation {

	protected GeoMagneticObservation mGeoMagneticObservationProtoBuf;

	protected GeoMagneticObservation.Builder mGeoMagneticObservationProtoBufBuilder;

	public RotationVectorObservation() {

		super();
		mType = ObservationType.TYPE_ROTATION_VECTOR;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.ROTATION_VECTOR);

		mGeoMagneticObservationProtoBuf = null;
		mGeoMagneticObservationProtoBufBuilder = GeoMagneticObservation
				.newBuilder();

		mValues = new float[5];

	}

	protected RotationVectorObservation(
			ObservationProtoBuf.Observation observationProtoBuf) {

		super(observationProtoBuf);

		mType = ObservationType.TYPE_ROTATION_VECTOR;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.ROTATION_VECTOR);

		assert (observationProtoBuf.hasGeoMagneticObservation());
		GeoMagneticObservation geoMagneticObservationProtoBuf = observationProtoBuf
				.getGeoMagneticObservation();

		mValues = new float[5];
		mValues[0] = geoMagneticObservationProtoBuf.getX();
		mValues[1] = geoMagneticObservationProtoBuf.getY();
		mValues[2] = geoMagneticObservationProtoBuf.getZ();
		mValues[3] = geoMagneticObservationProtoBuf.getSigma();
		mValues[4] = geoMagneticObservationProtoBuf.getError();

	}

	public void setX(float x) {
		mValues[0] = x;
		mGeoMagneticObservationProtoBufBuilder.setX(x);
		mDirty = true;
	}

	public void setY(float y) {
		mValues[1] = y;
		mGeoMagneticObservationProtoBufBuilder.setY(y);
		mDirty = true;
	}

	public void setZ(float z) {
		mValues[2] = z;
		mGeoMagneticObservationProtoBufBuilder.setZ(z);
		mDirty = true;
	}

	public void setSigma(float sigma) {
		mValues[3] = sigma;
		mGeoMagneticObservationProtoBufBuilder.setSigma(sigma);
		mDirty = true;
	}

	public void setAccuracy(float accuracy) {
		mValues[4] = accuracy;
		mGeoMagneticObservationProtoBufBuilder.setError(accuracy);
		mDirty = true;
	}

	@Override
	public void setValues(float[] values) {
		for (int i = 0; i < 5; ++i) {
			mValues[i] = values[i];
		}
		setX(mValues[0]);
		setY(mValues[1]);
		setZ(mValues[2]);
		setSigma(mValues[3]);
		setAccuracy(mValues[4]);
	}
	
	@Override
	protected void validate() {

		mGeoMagneticObservationProtoBuf = mGeoMagneticObservationProtoBufBuilder
				.buildPartial();
		mObservationProtoBufBuilder
				.setGeoMagneticObservation(mGeoMagneticObservationProtoBuf);
		boolean protoBufValid = mGeoMagneticObservationProtoBuf.isInitialized();

		if (protoBufValid) {
			super.validate();
		}

	}
}
