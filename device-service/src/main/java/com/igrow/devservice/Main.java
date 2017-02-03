/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Main.java        
 *
 * Copyright (c) 2013 - 2014 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.igrow.devservice;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.http.HttpCodecFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static ServiceLocator mServiceLocator;

	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		String httpPort = System.getProperty("jersey.test.port");
		if (null != httpPort) {
			try {
				return Integer.parseInt(httpPort);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://0.0.0.0/").port(getPort(9997))
				.build();
	}

	public static final URI BASE_URI = getBaseURI();

	public static HttpServer startServer() throws IOException {
		// final Map<String, String> initParams = new HashMap<String, String>();
		// ResourceConfig resourceConfig = new
		// DefaultResourceConfig(DeviceServiceApplication.class);
		// initParams.put("jersey.config.server.provider.packages",


		ResourceConfig resourceConfig = new DeviceServiceApplication();

		LOGGER.info("Starting grizzly2...");
		return GrizzlyHttpServerFactory.createHttpServer(BASE_URI,
				resourceConfig, mServiceLocator);
	}

	public static void main(String[] args) throws IOException {
		// Grizzly 2 initialization

		mServiceLocator = ServiceLocatorFactory.getInstance().create(
				"non-Jersey types");

		final HttpServer httpServer = startServer();

		if (Boolean.valueOf(System.getProperty(
				"com.igrow.devservice.debug", "false"))) {

			LOGGER.info("Enabling debug output");

			final FilterChain filterChain = httpServer.getListener("grizzly")
					.getFilterChain();
			HttpCodecFilter codecFilter = (HttpCodecFilter) filterChain
					.get(filterChain.indexOfType(HttpCodecFilter.class));
			codecFilter.getMonitoringConfig().addProbes(new LoggingHttpProbe());
		}

		// register shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Stopping server..");
				
				httpServer.shutdownNow();
			}
		}, "shutdownHook"));

		try {
			// httpServer.start();
			LOGGER.info(String.format(
					"Jersey app started with WADL available at "
							+ "%sapplication.wadl.", BASE_URI));
			Thread.currentThread().join();
		} catch (Exception e) {
			LOGGER.error(
					"There was an error while starting Grizzly HTTP server.", e);
		}
	}
}
