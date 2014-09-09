/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)LocatorSessionManger.java        
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

package com.argusat.gjl.locservice.session;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocatorSessionManager implements Map<String, LocatorSession> {

	private final Map<String, LocatorSession> locatorSessions = new ConcurrentHashMap<String, LocatorSession>();

	public LocatorSessionManager() {

	}

	@Override
	public void clear() {
		locatorSessions.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return locatorSessions.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return locatorSessions.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, LocatorSession>> entrySet() {
		return locatorSessions.entrySet();
	}

	@Override
	public LocatorSession get(Object key) {
		return locatorSessions.get(key);
	}

	@Override
	public boolean isEmpty() {
		return locatorSessions.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return locatorSessions.keySet();
	}

	@Override
	public LocatorSession put(String key, LocatorSession value) {
		return locatorSessions.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends LocatorSession> m) {
		locatorSessions.putAll(m);
	}

	@Override
	public LocatorSession remove(Object key) {
		return locatorSessions.remove(key);
	}

	@Override
	public int size() {
		return locatorSessions.size();
	}

	@Override
	public Collection<LocatorSession> values() {
		return locatorSessions.values();
	}

}
