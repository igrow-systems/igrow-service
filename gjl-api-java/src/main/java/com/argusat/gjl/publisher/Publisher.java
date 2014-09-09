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

package com.argusat.gjl.publisher;

import java.io.IOException;

import com.google.protobuf.AbstractMessage;

public interface Publisher {

	void connect() throws IOException;

	<T extends AbstractMessage> void publish(String topic, Class<T> clazz, T message) throws IOException;

	boolean isConnected();
}
