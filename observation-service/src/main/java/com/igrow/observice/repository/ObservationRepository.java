/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationRepository.java        
 *
 * Copyright (c) 2013 -2014, 2017 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.igrow.observice.repository;

import java.util.List;

import com.igrow.model.Observation;

public interface ObservationRepository {

	void storeObservation(Observation observation);

	void storeObservations(List<Observation> observations);

	List<Observation> findObservations(
			float latitude,
			float longitude,
			long radius,
			long limit);
	
	List<Observation> findObservations(
			final String sensorId,
			long timestampStart,
			long timestampEnd,
			long limit);

}
