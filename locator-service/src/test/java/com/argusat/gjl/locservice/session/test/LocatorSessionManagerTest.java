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
		mManager.put("test-id-233", mLocatorSession);
		mManager.put("test-id-234", mLocatorSession);
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
	public void testIsEmpty() {
		assertFalse(mManager.isEmpty());
		LocatorSessionManager manager = new LocatorSessionManager();
		assertTrue(manager.isEmpty());
	}

	@Test
	public void testKeySet() {
		fail("Not yet implemented");
	}

	@Test
	public void testPut() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		assertTrue(mManager.containsKey("test-id-234"));
		mManager.remove("test-id-234");
		assertFalse(mManager.containsKey("test-id-234"));
	}

	@Test
	public void testSize() {
		assertEquals(2, mManager.size());
	}

	@Test
	public void testValues() {
		fail("Not yet implemented");
	}

}
