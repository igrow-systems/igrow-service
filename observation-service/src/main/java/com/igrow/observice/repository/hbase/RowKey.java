/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)RowKey.java        
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

package com.igrow.observice.repository.hbase;

import com.igrow.model.Observation;

public class RowKey {

	private byte[] longestCommonPrefixName;
	
	public RowKey(Observation observation) {
		
		int lat = (int) (observation.getLocation().getLatitude() * 10E6);
		int lon = (int) (observation.getLocation().getLongitude() * 10E6);
		
		long timestamp = observation.getTimestamp();
		
		
	}
	
	byte[] getByteArray() {
		return null;
	}
	
}
