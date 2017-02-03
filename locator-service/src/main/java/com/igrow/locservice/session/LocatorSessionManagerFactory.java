/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessionManagerFactory.java        
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

import org.glassfish.hk2.api.Factory;

public class LocatorSessionManagerFactory implements
		Factory<LocatorSessionManager> {

	//private static final transient Logger LOGGER = LoggerFactory
	//		.getLogger(LocatorSessionManagerFactory.class);

	@Override
	public void dispose(LocatorSessionManager locatorSessionManager) {
		/*
		 * try { locatorSessionManager.close(); } catch (IOException e) {
		 * LOGGER.error("Failed to close DeviceRepository", e); }
		 */
	}

	@Override
	public LocatorSessionManager provide() {

		LocatorSessionManager locatorSessionManager = null;
		// try {
		locatorSessionManager = new LocatorSessionManager();
		// } catch (ClassNotFoundException | SQLException e) {
		// LOGGER.error("Failed to construct DeviceRepository", e);
		// }
		return locatorSessionManager;
	}

}
