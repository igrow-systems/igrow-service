/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)SubscriberRabbitMQ.java        
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

package com.igrow.subscriber;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.AbstractMessage;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Service
public abstract class SubscriberRabbitMQ<T extends AbstractMessage> implements
		Subscriber<T>, Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubscriberRabbitMQ.class);

	private static final String EXCHANGE_NAME = "topic_observations";

	//private static final String QUEUE_NAME = "observations";

	private static final String OBSERVATIONS_CONSUMER_TAG = "observationsConsumerTag";

	public static final String PROPERTIES_FILENAME = "subscriber.properties";

	private Properties mProperties;

	private static final String RABBITMQ_SERVER_PROPERTY = "subscriber.rabbitmq.host";
	private static final String RABBITMQ_PORT_PROPERTY = "subscriber.rabbitmq.port";
	private static final String RABBITMQ_USER_PROPERTY = "subscriber.rabbitmq.user";
	private static final String RABBITMQ_PASSWORD_PROPERTY = "subscriber.rabbitmq.password";

	public static String mRabbitMQServer;
	public static int mRabbitMQPort;
	private static String mRabbitMQUser;
	private static String mRabbitMQPassword;

	private Connection mConnection;

	private Channel mChannel;

	private MessageHandler<T> mMessageHandler;
	
	private String mQueueName;
	
	private boolean mInitialised = false;

	public SubscriberRabbitMQ() {

	}

	public void initialise(Properties properties) {

		mProperties = properties;

		mRabbitMQServer = mProperties.getProperty(RABBITMQ_SERVER_PROPERTY);
		mRabbitMQPort = Integer.parseInt(mProperties
				.getProperty(RABBITMQ_PORT_PROPERTY));
		mRabbitMQUser = mProperties.getProperty(RABBITMQ_USER_PROPERTY);
		mRabbitMQPassword = mProperties.getProperty(RABBITMQ_PASSWORD_PROPERTY);
		mInitialised = true;
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
			//mChannel.queueDeclare(QUEUE_NAME, false, true, true, null);
			mQueueName = mChannel.queueDeclare().getQueue();

			LOGGER.info("RabbitMQ subscriber connected");

			boolean autoAck = false;
			mChannel.basicConsume(mQueueName, autoAck,
					OBSERVATIONS_CONSUMER_TAG, new DefaultConsumer(mChannel) {
						@Override
						public void handleDelivery(String consumerTag,
								Envelope envelope, BasicProperties properties,
								byte[] body) throws IOException {

							String routingKey = envelope.getRoutingKey();
							String contentType = properties.getContentType();
							long deliveryTag = envelope.getDeliveryTag();

							ByteArrayInputStream bais = new ByteArrayInputStream(
									body);

							T protoBuf = deserialiseMessage(bais);

							LOGGER.debug("Received message {}",
									protoBuf.toString());

							// (process the message components here ...)
							mChannel.basicAck(deliveryTag, false);

							if (mMessageHandler != null) {
								mMessageHandler.handleMessage(protoBuf);
							}
						}
					});
		}
	}

	@Override
	public void subscribe(String topic) throws IOException {

		mChannel.queueBind(mQueueName, EXCHANGE_NAME, topic, null);
		LOGGER.info("Subscribed to " + EXCHANGE_NAME + " topic [ {} ]", topic);
	}

	@Override
	public void unsubscribe(String topic) throws IOException {

		mChannel.queueUnbind(mQueueName, EXCHANGE_NAME, topic, null);
		LOGGER.info("Unsubscribed from " + EXCHANGE_NAME + " topic [ {} ]",
				topic);
	}

	@Override
	public void close() throws IOException {

		if (mChannel != null) {
			try {
				mChannel.basicCancel(OBSERVATIONS_CONSUMER_TAG);
			} catch (IOException e) {

			}
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
		LOGGER.info("RabbitMQ subscriber disconnected");
	}

	@Override
	public void registerMessageHandler(MessageHandler<T> handler) {
		mMessageHandler = handler;
	}

	@Override
	public void unregisterMessageHandler(MessageHandler<T> handler) {
		mMessageHandler = null;
	}

	abstract T deserialiseMessage(ByteArrayInputStream bais);

	public boolean isInitialised() {
		return mInitialised;
	}

}
