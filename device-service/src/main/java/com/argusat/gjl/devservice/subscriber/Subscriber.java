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

package com.argusat.gjl.devservice.subscriber;

import java.io.IOException;

import com.argusat.gjl.devservice.subscriber.MessageHandler;

public interface Subscriber {

	public void connect() throws IOException;

	public void registerMessageHandler(MessageHandler handler);
	
	public void unregisterMessageHandler();
	
	public void subscribe(String topic) throws IOException;
	
	public void unsubscribe(String topic) throws IOException;

}
