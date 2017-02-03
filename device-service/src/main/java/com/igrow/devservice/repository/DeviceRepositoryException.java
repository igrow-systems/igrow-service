/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceRepositoryException.java        
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

package com.igrow.devservice.repository;

public class DeviceRepositoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceRepositoryException() {
		
	}

	public DeviceRepositoryException(String message) {
		super(message);
		
	}

	public DeviceRepositoryException(Throwable cause) {
		super(cause);
		
	}

	public DeviceRepositoryException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DeviceRepositoryException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
