/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationRepositoryProvider.java        
 *
 * Copyright (c) 2013 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.igrow.observice;

//import javax.ws.rs.ext.Provider;
//
//import com.sun.jersey.core.spi.component.ComponentContext;
//import com.sun.jersey.core.spi.component.ComponentScope;
//import com.sun.jersey.spi.inject.Inject;
//import com.sun.jersey.spi.inject.Injectable;
//import com.sun.jersey.spi.inject.InjectableProvider;
//
//@Provider
//public class ObservationRepositoryProvider implements
//		InjectableProvider<Inject, Type> {
//
//	@Override
//	public ComponentScope getScope() {
//		return ComponentScope.PerRequest;
//	}
//
//	@Override
//	public Injectable getInjectable(final ComponentContext arg0,
//			final ObservationRepository arg1, final Type arg2) {
//		if (Integer.class.equals(arg2)) {
//			return new Injectable<Integer>() {
//
//				@Override
//				public Integer getValue() {
//					return new Integer(99);
//				}
//			};
//		} else {
//			return null;
//		}
//	}
//}
