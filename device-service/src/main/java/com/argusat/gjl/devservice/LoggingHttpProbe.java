/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LoggingHttpProbe.java        
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

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.ContentEncoding;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpHeader;
import org.glassfish.grizzly.http.HttpPacket;
import org.glassfish.grizzly.http.HttpProbe;
import org.glassfish.grizzly.http.TransferEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHttpProbe implements HttpProbe {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoggingHttpProbe.class);

	@Override
	public void onContentChunkParseEvent(Connection arg0, HttpContent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentChunkSerializeEvent(Connection arg0, HttpContent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentEncodingParseEvent(Connection arg0, HttpHeader arg1,
			Buffer buffer, ContentEncoding arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentEncodingSerializeEvent(Connection arg0,
			HttpHeader arg1, Buffer buffer, ContentEncoding arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataReceivedEvent(Connection connection, Buffer buffer) {
		LOGGER.debug(buffer.toStringContent());
	}

	@Override
	public void onDataSentEvent(Connection connection, Buffer buffer) {
		LOGGER.debug(buffer.toStringContent());
	}

	@Override
	public void onHeaderParseEvent(Connection connection, HttpHeader header, int arg2) {
		LOGGER.debug(header.toString());
	}

	@Override
	public void onHeaderSerializeEvent(Connection connection, HttpHeader arg1,
			Buffer arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransferEncodingParseEvent(Connection connection, HttpHeader arg1,
			Buffer arg2, TransferEncoding arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTransferEncodingSerializeEvent(Connection arg0,
			HttpHeader arg1, Buffer arg2, TransferEncoding arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContentEncodingParseResultEvent(Connection arg0,
			HttpHeader arg1, Buffer arg2, ContentEncoding arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContentEncodingSerializeResultEvent(Connection arg0,
			HttpHeader arg1, Buffer arg2, ContentEncoding arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorEvent(Connection arg0, HttpPacket arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

}
