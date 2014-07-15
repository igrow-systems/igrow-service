/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)Device.java        
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

package com.argusat.gjl.model;

import com.argusat.gjl.service.device.DeviceProtoBuf;

public class Device {

	public enum OSType {
		MS_WINDOWS, GOOGLE_ANDROID, APPLE_IOS
	}
	
	private boolean mIsValid;
	
	private boolean mIsDirty;
	
	private String mDeviceId;
	
	private String mPushToken;
	
	private OSType mOsType;
	
	private String mOsVersion;

	public Device() {
		mIsDirty = true;
		mIsValid = false; // to be explicit
	}
	
	public static Device newDevice(DeviceProtoBuf.Device device) {
		
		Device modelDevice = new Device();
		
		modelDevice.setDeviceId(device.getDeviceId());
		switch (device.getOsType()) {
		case MS_WINDOWS:
			modelDevice.setOsType(OSType.MS_WINDOWS);
			break;
		case GOOGLE_ANDROID:
			modelDevice.setOsType(OSType.GOOGLE_ANDROID);
			break;
		case APPLE_IOS:
			modelDevice.setOsType(OSType.APPLE_IOS);
			break;
		}
		modelDevice.setOsVersion(device.getOsVersion());
		modelDevice.setPushToken(device.getPushToken());
		
		return modelDevice;
	}
	
	public String getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(String deviceId) {
		mDeviceId = deviceId;
		mIsDirty = true;
	}

	public String getPushToken() {
		return mPushToken;
	}

	public void setPushToken(String pushToken) {
		mPushToken = pushToken;
		mIsDirty = true;
	}

	public OSType getOsType() {
		return mOsType;
	}

	public void setOsType(OSType osType) {
		mOsType = osType;
		mIsDirty = true;
	}

	public String getOsVersion() {
		return mOsVersion;
	}

	public void setOsVersion(String OSVersion) {
		mOsVersion = OSVersion;
		mIsDirty = true;
	}

	public boolean isValid() {
		if (mIsDirty) {
			mIsValid = ((mDeviceId != null && !mDeviceId.isEmpty()) 
					&& (mOsType != null)
					&& (mOsVersion != null && !mOsVersion.isEmpty())
					&& (mPushToken != null && !mPushToken.isEmpty()));
			mIsDirty = false;
		}
		return mIsValid;
	}
	
}
