/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceRepository.java        
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

package com.argusat.gjl.devservice.repository;

import java.util.List;

import com.argusat.gjl.model.Device;

public interface DeviceRepository {

	void storeDevice(Device device) throws DeviceRepositoryException;

	Device findDevice(String deviceId);

	List<Device> findLocalDevices(double latitude, double longitude,
			long radius, long limit);
}
