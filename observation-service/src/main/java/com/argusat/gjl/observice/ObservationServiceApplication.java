/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationServiceApplication.java        
 *
 * Copyright (c) 2014, 2017 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.argusat.gjl.observice;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.argusat.gjl.observice.repository.ObservationRepository;
import com.argusat.gjl.observice.repository.postgis.ObservationRepositoryPostGISImpl;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.subscriber.ObservationSubscriberRabbitMQ;
import com.argusat.gjl.subscriber.Subscriber;

@ApplicationPath("/")
public class ObservationServiceApplication extends ResourceConfig {

	@Inject
	private ServiceLocator mServiceLocator;

	public ObservationServiceApplication() {

		packages("com.argusat.gjl.observice");

		// register(new AbstractBinder() {
		// @Override
		// protected void configure() {
		// bindFactory(DeviceRepositoryFactory.class).to(
		// DeviceRepository.class);
		// }
		// });

		// register(new AbstractBinder() {
		// @Override
		// protected void configure() {
		// bindFactory(SubscriberFactory.class).to(Subscriber<Observation>.class);
		// }
		// });

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(ObservationRepositoryPostGISImpl.class).to(
						ObservationRepository.class).in(Singleton.class);
			}
		});

	}

	public ObservationServiceApplication(Set<Class<?>> classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public ObservationServiceApplication(Class<?>... classes) {
		super(classes);
		// TODO Auto-generated constructor stub
	}

	public ObservationServiceApplication(ResourceConfig original) {
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
