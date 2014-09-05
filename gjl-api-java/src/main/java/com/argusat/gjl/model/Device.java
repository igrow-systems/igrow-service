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

	private boolean mValid;

	private boolean mDirty;

	private boolean mProtoBufValid;

	private DeviceProtoBuf.Device.Builder mDeviceProtoBufBuilder;

	private DeviceProtoBuf.Device mDeviceProtoBuf;

	private String mDeviceId;

	private String mPushToken;

	private OSType mOsType;

	private String mOsVersion;

	private String mManufacturer;

	private String mModel;

	private String mProduct;

	private String mDevice;

	private Location mLastKnownLocation;

	public Device() {
		mDirty = true;
		mValid = false; // to be explicit
		mProtoBufValid = false;
		mDeviceProtoBufBuilder = DeviceProtoBuf.Device.newBuilder();
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
		modelDevice.setManufacturer(device.getManufacturer());
		modelDevice.setModel(device.getModel());
		modelDevice.setProduct(device.getProduct());
		modelDevice.setDevice(device.getDevice());

		return modelDevice;
	}

	public String getDeviceId() {
		return mDeviceId;
	}

	public void setDeviceId(String deviceId) {
		mDeviceId = deviceId;
		mDeviceProtoBufBuilder.setDeviceId(deviceId);
		mDirty = true;
	}

	public String getPushToken() {
		return mPushToken;
	}

	public void setPushToken(String pushToken) {
		mPushToken = pushToken;
		mDeviceProtoBufBuilder.setPushToken(pushToken);
		mDirty = true;
	}

	public OSType getOsType() {
		return mOsType;
	}

	public void setOsType(OSType osType) {
		mOsType = osType;
		switch (osType) {
		case MS_WINDOWS:
			mDeviceProtoBufBuilder
					.setOsType(DeviceProtoBuf.Device.OSType.MS_WINDOWS);
			break;
		case GOOGLE_ANDROID:
			mDeviceProtoBufBuilder
					.setOsType(DeviceProtoBuf.Device.OSType.GOOGLE_ANDROID);
			break;
		case APPLE_IOS:
			mDeviceProtoBufBuilder
					.setOsType(DeviceProtoBuf.Device.OSType.APPLE_IOS);
			break;
		}
		mDirty = true;
	}

	public String getOsVersion() {
		return mOsVersion;
	}

	public void setOsVersion(String osVersion) {
		mOsVersion = osVersion;
		mDeviceProtoBufBuilder.setOsVersion(osVersion);
		mDirty = true;
	}

	public Location getLastKnownLocation() {
		return mLastKnownLocation;
	}

	public void setLastKnownLocation(Location lastKnownLocation) {
		mLastKnownLocation = lastKnownLocation;
		mDeviceProtoBufBuilder.setLastKnownLocation(lastKnownLocation.getLocationProtoBuf());
		mDirty = true;
	}

	public String getDevice() {
		return mDevice;
	}

	public void setDevice(String device) {
		mDevice = device;
		mDeviceProtoBufBuilder.setDevice(device);
		mDirty = true;
	}

	public String getManufacturer() {
		return mManufacturer;
	}

	public void setManufacturer(String manufacturer) {
		mManufacturer = manufacturer;
		mDeviceProtoBufBuilder.setManufacturer(manufacturer);
		mDirty = true;
	}

	public String getModel() {
		return mModel;
	}

	public void setModel(String model) {
		mModel = model;
		mDeviceProtoBufBuilder.setModel(model);
		mDirty = true;
	}

	public String getProduct() {
		return mProduct;
	}

	public void setProduct(String product) {
		mProduct = product;
		mDeviceProtoBufBuilder.setProduct(product);
		mDirty = true;
	}

	protected void validate() {

		mDeviceProtoBuf = mDeviceProtoBufBuilder.buildPartial();
		mProtoBufValid = mDeviceProtoBuf.isInitialized();
		mValid = ((mDeviceId != null && !mDeviceId.isEmpty())
				&& (mOsType != null)
				&& (mOsVersion != null && !mOsVersion.isEmpty())
				&& (mPushToken != null && !mPushToken.isEmpty())
				&& (mManufacturer != null && !mManufacturer.isEmpty())
				&& (mModel != null && !mModel.isEmpty())
				&& (mProduct != null && !mProduct.isEmpty()) && (mDevice != null && !mDevice
				.isEmpty()));
		mDirty = false;

	}

	public boolean isValid() {
		if (mDirty) {
			validate();
		}
		return mValid;
	}

	public DeviceProtoBuf.Device getDeviceProtoBuf() {
		if (mDirty) {
			validate();
		}
		return mDeviceProtoBuf;
	}

}
