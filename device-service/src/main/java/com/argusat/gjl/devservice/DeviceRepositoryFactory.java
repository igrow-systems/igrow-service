/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceRepositoryFactory.java        
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

package com.argusat.gjl.devservice;

import java.io.IOException;
import java.sql.SQLException;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.postgis.DeviceRepositoryPostGISImpl;

public class DeviceRepositoryFactory implements Factory<DeviceRepository> {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(Devices.class);

	@Override
	public void dispose(DeviceRepository deviceRepository) {
		try {
			deviceRepository.close();
		} catch (IOException e) {
			LOGGER.error("Failed to close DeviceRepository", e);
		}
	}

	@Override
	public DeviceRepository provide() {

		DeviceRepository deviceRepository = null;
		try {
			deviceRepository = new DeviceRepositoryPostGISImpl();
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("Failed to construct DeviceRepository", e);
		}
		return deviceRepository;
	}

}
