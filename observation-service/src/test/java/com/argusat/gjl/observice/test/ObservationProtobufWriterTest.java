/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationProtobufReaderTest.java        
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


package com.argusat.gjl.observice.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.rules.TemporaryFolder;

import com.argusat.gjl.model.Location;
import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.observice.Main;
import com.argusat.gjl.observice.ObservationProtobufReader;
import com.argusat.gjl.observice.ObservationProtobufWriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


public class ObservationProtobufWriterTest extends TestCase {

    private HttpServer httpServer;
    
    private WebResource r;

    public ObservationProtobufWriterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        //start the Grizzly2 web container 
        httpServer = Main.startServer();

        // create the client
        Client c = Client.create();
        r = c.resource(Main.BASE_URI);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        httpServer.stop();
    }

    public void testWriteTo() throws WebApplicationException, IOException {
    	
    	ObservationProtobufWriter writer = new ObservationProtobufWriter();
    	
    	TemporaryFolder tempFolder = new TemporaryFolder();
    	File entityFile = tempFolder.newFile("observation-location-only.bin");
    	
    	OutputStream entityStream = new FileOutputStream(entityFile);
    	
    	Location location = new Location();
    	location.setLatitude(137483L);
    	location.setLongitude(1237843L);
    	location.setAltitude(120.0f);
    	location.setHDOP(5.0f);
    	location.setVDOP(12.0f);
    	
    	Observation observation = Observation.newObservation(ObservationType.TYPE_LOCATION_ONLY);
    	observation.setDeviceId(007L);
    	observation.setLocation(location);
    	observation.setTimestamp(11172763L);
    	
    	assertTrue(observation.isValid());
    	
    	writer.writeTo(observation, null, null, null, null, null, entityStream);
    	
    	
    }
    
    public void testIsReadable() {
    	ObservationProtobufReader reader = new ObservationProtobufReader();
    	
    	boolean result = reader.isReadable(Observation.class, null, null, null);
    	assertTrue(result);
    }


}
