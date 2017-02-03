/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Publisher.java        
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

package com.igrow.publisher;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import com.google.protobuf.AbstractMessage;

public interface Publisher extends Closeable {

	void connect() throws IOException;

	<T extends AbstractMessage> void publish(String topic, Class<T> clazz, T message) throws IOException;

	boolean isConnected();
	
	void initialise(Properties properties);
}
