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

package com.argusat.gjl.devservice;

import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;

import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.http.HttpCodecFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
		return UriBuilder.fromUri("http://0.0.0.0/").port(getPort(9998))
				.build();
	}

	public static final URI BASE_URI = getBaseURI();

	public static HttpServer startServer() throws IOException {
		final Map<String, String> initParams = new HashMap<String, String>();

		initParams.put("com.sun.jersey.config.property.packages",
				"com.argusat.gjl.observice");

		LOGGER.info("Starting grizzly2...");
		return GrizzlyWebContainerFactory.create(BASE_URI, initParams);
	}

	public static void main(String[] args) throws IOException {
		// Grizzly 2 initialization
		final HttpServer httpServer = startServer();

		if (Boolean.valueOf(System.getProperty(
				"com.argusat.gjl.observice.debug", "false"))) {

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
				httpServer.stop();
			}
		}, "shutdownHook"));

		try {
			//httpServer.start();
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
