/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationCollection.java        
 *
 * Copyright (c) 2013 - 2014 Argusat Limited
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

import java.util.ArrayList;
import java.util.List;

public class ObservationCollection {

	protected List<Observation> mObservations;

	public ObservationCollection() {
		mObservations = new ArrayList<Observation>();
	}

	public void add(Observation observation) {
		mObservations.add(observation);
	}

	public List<Observation> getObservations() {
		return mObservations;
	}
	
	public void setObservations(List<Observation> obs) {
		mObservations = obs;
	}

}
