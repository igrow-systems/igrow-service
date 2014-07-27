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

package com.argusat.gjl.observice.publisher.rabbitmq;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.observice.publisher.Publisher;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PublisherRabbitMQ implements Publisher, Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublisherRabbitMQ.class);

	private static final String EXCHANGE_NAME = "topic_observations";

	private static final String PROPERTIES_FILENAME = "observation-service.properties";

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

	static {
		try {
			InputStream entityStream = PublisherRabbitMQ.class
					.getResourceAsStream("/" + PROPERTIES_FILENAME);

			mProperties.load(entityStream);
			entityStream.close();

			mRabbitMQServer = mProperties.getProperty(RABBITMQ_SERVER_PROPERTY);
			mRabbitMQPort = Integer.parseInt(mProperties
					.getProperty(RABBITMQ_PORT_PROPERTY));
			mRabbitMQUser = mProperties.getProperty(RABBITMQ_USER_PROPERTY);
			mRabbitMQPassword = mProperties
					.getProperty(RABBITMQ_PASSWORD_PROPERTY);

		} catch (IOException e) {
			LOGGER.error("Failed to load properties", e);
		}
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
			//mChannel.queueDeclare(QUEUE_NAME, false, false, false, null);

			LOGGER.info("RabbitMQ publisher connected");
		}
	}

	@Override
	public void publish(Observation observation) throws IOException {

		if (observation.isValid()) {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			observation.getObservationProtoBuf().writeTo(oStream);
			mChannel.basicPublish(EXCHANGE_NAME, observation.getDeviceId(), null, oStream.toByteArray());
			LOGGER.info("Published observation: {}", observation.toString());
		}
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
