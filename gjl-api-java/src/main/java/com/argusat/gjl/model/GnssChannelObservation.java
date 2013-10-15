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
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation.C0NObservation;

public class GnssChannelObservation extends Observation {

	private int mPrn;

	private float mAzimuth;

	private float mElevation;

	private float mC0_N;

	private C0NObservation mC0NObservationProtoBuf;

	private C0NObservation.Builder mC0NObservationBuilder;

	private C0NObservation.ReceiverChannel mReceiverChannelProtoBuf;

	private C0NObservation.ReceiverChannel.Builder mReceiverChannelBuilder;

	public GnssChannelObservation() {
		super();
		mType = ObservationType.TYPE_GNSS_CHANNEL;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.C0N_GNSS);
		mValues = new float[3];
		mC0NObservationProtoBuf = null;
		mC0NObservationBuilder = ObservationProtoBuf.Observation.C0NObservation
				.newBuilder();
		mReceiverChannelProtoBuf = null;
		mReceiverChannelBuilder = C0NObservation.ReceiverChannel.newBuilder();
	}

	public GnssChannelObservation(
			ObservationProtoBuf.Observation observationProtoBuf) {
		super(observationProtoBuf);
		mType = ObservationType.TYPE_GNSS_CHANNEL;
		mObservationProtoBufBuilder
				.setType(ObservationProtoBuf.Observation.ObservationType.C0N_GNSS);
		mValues = new float[3];
		mC0NObservationProtoBuf = null;
		mC0NObservationBuilder = ObservationProtoBuf.Observation.C0NObservation
				.newBuilder();
		mReceiverChannelProtoBuf = null;
		mReceiverChannelBuilder = C0NObservation.ReceiverChannel.newBuilder();
	}

	private void initialise() {

	}

	public int getPrn() {
		return mPrn;
	}

	public void setPrn(int prn) {
		mPrn = prn;
		mReceiverChannelBuilder.setPrn(prn);
		mDirty = true;
	}

	public double getAzimuth() {
		return mAzimuth;
	}

	public void setAzimuth(float azimuth) {
		this.mAzimuth = azimuth;
		this.mValues[0] = azimuth;
		mReceiverChannelBuilder.setAzimuth(azimuth);
		mDirty = true;
	}

	public float getElevation() {
		return mElevation;
	}

	public void setElevation(float elevation) {
		this.mElevation = elevation;
		this.mValues[1] = elevation;
		mReceiverChannelBuilder.setElevation(elevation);
		mDirty = true;
	}

	public float getC0_n() {
		return mC0_N;
	}

	public void setC0_n(float c0_n) {
		this.mC0_N = c0_n;
		this.mValues[2] = c0_n;
		mReceiverChannelBuilder.setC0N(c0_n);
		mDirty = true;
	}

	@Override
	protected void validate() {

		mReceiverChannelProtoBuf = mReceiverChannelBuilder.buildPartial();
		mC0NObservationBuilder.addChannels(mReceiverChannelProtoBuf);
		// mC0NObservationBuilder.
		mC0NObservationProtoBuf = mC0NObservationBuilder.buildPartial();
		mObservationProtoBufBuilder.setC0Nobservation(mC0NObservationProtoBuf);
		boolean protoBufValid = mC0NObservationProtoBuf.isInitialized();

		if (protoBufValid) {
			super.validate();
		}

	}

}
