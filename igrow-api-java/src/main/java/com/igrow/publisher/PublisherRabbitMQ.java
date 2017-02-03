/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)PublisherRabbitMQ.java        
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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.AbstractMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PublisherRabbitMQ implements Publisher, Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublisherRabbitMQ.class);

	private static final String EXCHANGE_NAME = "topic_observations";

	public static final String PROPERTIES_FILENAME = "publisher.properties";

	private static Properties mProperties = new Properties();

	private static final String RABBITMQ_SERVER_PROPERTY = "publisher.rabbitmq.host";
	private static final String RABBITMQ_PORT_PROPERTY = "publisher.rabbitmq.port";
	private static final String RABBITMQ_USER_PROPERTY = "publisher.rabbitmq.user";
	private static final String RABBITMQ_PASSWORD_PROPERTY = "publisher.rabbitmq.password";

	public static String mRabbitMQServer;
	public static int mRabbitMQPort;
	private static String mRabbitMQUser;
	private static String mRabbitMQPassword;

	private Connection mConnection;

	private Channel mChannel;
	
	public PublisherRabbitMQ() {
		
	}
	
	public void initialise(Properties properties) {
		
		mProperties = properties;
		
		mRabbitMQServer = mProperties.getProperty(RABBITMQ_SERVER_PROPERTY);
		mRabbitMQPort = Integer.parseInt(mProperties
				.getProperty(RABBITMQ_PORT_PROPERTY));
		mRabbitMQUser = mProperties.getProperty(RABBITMQ_USER_PROPERTY);
		mRabbitMQPassword = mProperties
				.getProperty(RABBITMQ_PASSWORD_PROPERTY);
	}

	@Override
	public void connect() throws IOException {

		if (mConnection == null || !mConnection.isOpen()) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(mRabbitMQServer);
			factory.setPort(mRabbitMQPort);
			factory.setUsername(mRabbitMQUser);
			factory.setPassword(mRabbitMQPassword);

			mConnection = factory.newConnection();
			mChannel = mConnection.createChannel();

			mChannel.exchangeDeclare(EXCHANGE_NAME, "topic");
			// mChannel.queueDeclare(QUEUE_NAME, false, false, false, null);

			LOGGER.info("RabbitMQ publisher connected");
		}
	}

	@Override
	public <T extends AbstractMessage> void publish(String topic,
			Class<T> clazz, T message) throws IOException {

		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		message.writeTo(oStream);
		mChannel.basicPublish(EXCHANGE_NAME, topic, null, oStream.toByteArray());
		LOGGER.debug("Published observation: {}", message.toString());

	}

	@Override
	public void close() throws IOException {

		if (mChannel != null) {
			try {
				mChannel.close();
			} catch (IOException e) {

			}
		}
		if (mConnection != null) {
			try {
				mConnection.close();
			} catch (IOException e) {

			}
		}
		LOGGER.info("RabbitMQ publisher disconnected");
	}

	@Override
	public boolean isConnected() {
		return (mConnection != null && mConnection.isOpen());
	}

}
