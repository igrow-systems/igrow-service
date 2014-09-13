/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Subscriber.java        
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

package com.argusat.gjl.subscriber;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import org.jvnet.hk2.annotations.Contract;

import com.argusat.gjl.subscriber.MessageHandler;

@Contract
public interface Subscriber<T> extends Closeable {

	void connect() throws IOException;
	
	void initialise(Properties properties);

	void registerMessageHandler(MessageHandler<T> handler);
	
	void unregisterMessageHandler(MessageHandler<T> handler);
	
	void subscribe(String topic) throws IOException;
	
	void unsubscribe(String topic) throws IOException;

}
