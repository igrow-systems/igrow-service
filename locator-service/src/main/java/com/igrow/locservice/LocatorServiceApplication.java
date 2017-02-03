/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorServiceApplication.java        
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

package com.igrow.locservice;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.igrow.protobuf.observation.ObservationProtoBuf.Observation;
import com.igrow.locservice.session.LocatorSessionManager;
import com.igrow.publisher.Publisher;
import com.igrow.publisher.PublisherRabbitMQ;
import com.igrow.subscriber.ObservationSubscriberRabbitMQ;
import com.igrow.subscriber.Subscriber;

@ApplicationPath("/")
public class LocatorServiceApplication extends ResourceConfig {

	@Inject
	private ServiceLocator mServiceLocator;

	public LocatorServiceApplication() {

		packages("com.igrow.locservice");

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(LocatorSessionManager.class).to(
						LocatorSessionManager.class).in(Singleton.class);
			}
		});

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(ObservationSubscriberRabbitMQ.class).to(
						new TypeLiteral<Subscriber<Observation>>() {
						}).in(Singleton.class);
			}
		});

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(PublisherRabbitMQ.class).to(Publisher.class).in(
						Singleton.class);
			}
		});

	}

	public LocatorServiceApplication(Set<Class<?>> classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public LocatorServiceApplication(Class<?>... classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public LocatorServiceApplication(ResourceConfig original) {
		super(original);
		// TODO Auto-generated constructor stub
	}

	/*
	 * TODO: mLocator.shutdown() in preDestroy()
	 */
	public ServiceLocator getServiceLocator() {
		return mServiceLocator;
	}

}
