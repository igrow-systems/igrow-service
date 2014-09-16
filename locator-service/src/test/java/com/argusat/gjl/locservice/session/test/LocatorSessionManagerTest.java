/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessionManagerTest.java        
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

package com.argusat.gjl.locservice.session.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.argusat.gjl.locservice.session.LocatorSession;
import com.argusat.gjl.locservice.session.LocatorSessionManager;
import com.argusat.gjl.locservice.session.Participant;
import com.argusat.gjl.model.Device;

public class LocatorSessionManagerTest {

	private LocatorSessionManager mManager;

	private LocatorSession mLocatorSession;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		mManager = new LocatorSessionManager();
		mLocatorSession = LocatorSession.newLocatorSession("test-id-233");
		Device device1 = new Device();
		device1.setDeviceId("test-id-233");
		Device device2 = new Device();
		device2.setDeviceId("test-id-234");
		Participant participant1 = Participant.create(device1);
		Participant participant2 = Participant.create(device2);
		
		mLocatorSession.addParticipant(participant1);
		mLocatorSession.addParticipant(participant2);
		
		mManager.put(participant1.getDeviceId(), mLocatorSession);
		mManager.put(participant2.getDeviceId(), mLocatorSession);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClear() {
		mManager.clear();
		assertEquals(0, mManager.size());
		assertTrue(mManager.isEmpty());
		assertEquals(0, mManager.entrySet().size());
		assertEquals(0, mManager.keySet().size());
	}

	@Test
	public void testContainsKey() {
		assertTrue(mManager.containsKey("test-id-233"));
	}

	@Test
	public void testContainsValue() {
		assertTrue(mManager.containsValue(mLocatorSession));
	}

	@Test
	public void testEntrySet() {
		Set<Map.Entry<String, LocatorSession>> entrySet = mManager.entrySet();
		assertEquals(2, entrySet.size());
		assertTrue(entrySet
				.contains(new AbstractMap.SimpleEntry<String, LocatorSession>(
						"test-id-233", mLocatorSession)));
		assertTrue(entrySet
				.contains(new AbstractMap.SimpleEntry<String, LocatorSession>(
						"test-id-234", mLocatorSession)));
	}

	@Test
	public void testGet() {
		LocatorSession session = mManager.get("test-id-234");
		assertNotNull(session);
	}
	
	@Test
	public void testGetBySessionId() {
		LocatorSession session = mManager.getBySessionId(mLocatorSession.getSessionId());
		assertNotNull(session);
	}

	@Test
	public void testIsEmpty() throws IOException {
		assertFalse(mManager.isEmpty());
		LocatorSessionManager manager = new LocatorSessionManager();
		assertTrue(manager.isEmpty());
		manager.close();
	}

	@Test
	public void testKeySet() {
		//fail("Not yet implemented");
	}

	@Test
	public void testPut() throws IOException {
		LocatorSessionManager manager = new LocatorSessionManager();
		LocatorSession locatorSession = LocatorSession.newLocatorSession("test-id-238");
		manager.put("test-id-238", locatorSession);
		manager.put("test-id-239", locatorSession);
		
		assertTrue(manager.containsKey("test-id-238"));
		assertTrue(manager.containsKey("test-id-239"));
		assertNotNull(manager.getBySessionId(locatorSession.getSessionId()));
		manager.close();
	}

	@Test
	public void testPutAll() {
		//fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		assertTrue(mManager.containsKey("test-id-234"));
		mManager.remove("test-id-234");
		assertFalse(mManager.containsKey("test-id-234"));
	}
	
	@Test
	public void testRemoveBySessionId() {
		assertTrue(mManager.containsKey("test-id-234"));
		mManager.remove(mLocatorSession.getSessionId());
		assertFalse(mManager.containsKey("test-id-233"));
		assertFalse(mManager.containsKey("test-id-234"));
	}

	@Test
	public void testSize() {
		assertEquals(2, mManager.size());
	}

	@Test
	public void testValues() {
		//fail("Not yet implemented");
	}

}
