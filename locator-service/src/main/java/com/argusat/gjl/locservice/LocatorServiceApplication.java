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


package com.argusat.gjl.locservice;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.argusat.gjl.locservice.session.LocatorSessionManager;
import com.argusat.gjl.locservice.session.LocatorSessionManagerFactory;
import com.argusat.gjl.subscriber.Subscriber;
import com.argusat.gjl.subscriber.SubscriberFactory;

@ApplicationPath("/")
public class LocatorServiceApplication extends ResourceConfig {

	@Inject
	private ServiceLocator mServiceLocator;
	
	public LocatorServiceApplication() {
		
		packages("com.argusat.gjl.locservice");
		
		register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(LocatorSessionManagerFactory.class).to(LocatorSessionManager.class);
            }
        });
		
		register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(SubscriberFactory.class).to(Subscriber.class);
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
