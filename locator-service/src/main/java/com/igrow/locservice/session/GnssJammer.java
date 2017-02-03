/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)GnssJammer.java        
 *
 * Copyright (c) 2014 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.igrow.locservice.session;

import com.igrow.model.Location;
import com.igrow.protobuf.locator.LocatorSessionInfoProtoBuf.LocatorSessionInfo;

public class GnssJammer {

	private Location mLocation;

	public GnssJammer() {

	}

	public LocatorSessionInfo.GnssJammer getProtoBuf() {

		LocatorSessionInfo.GnssJammer.Builder builder = LocatorSessionInfo.GnssJammer
				.newBuilder();

		builder.setLocation(mLocation.getLocationProtoBuf());
		
		return builder.build();
	}

	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(Location mLocation) {
		this.mLocation = mLocation;
	}

}
