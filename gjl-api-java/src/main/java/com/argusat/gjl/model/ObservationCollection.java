/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationCollection.java        
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

import java.util.ArrayList;
import java.util.List;

public class ObservationCollection {

	protected List<Observation> mObservations;

	protected String mDeviceId;

	public ObservationCollection() {
		mObservations = new ArrayList<Observation>();
	}

	public String getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(String deviceId) {
		mDeviceId = deviceId;
	}

	public void add(Observation observation) {
		mObservations.add(observation);
	}

	public List<Observation> getObservations() {
		return mObservations;
	}

}
