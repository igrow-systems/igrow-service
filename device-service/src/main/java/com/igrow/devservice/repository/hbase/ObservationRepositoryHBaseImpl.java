/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationRepositoryHBaseImpl.java        
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

package com.igrow.devservice.repository.hbase;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.devservice.repository.DeviceRepository;
import com.igrow.model.Device;
import com.igrow.model.Observation;

public class ObservationRepositoryHBaseImpl implements DeviceRepository,
		Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ObservationRepositoryHBaseImpl.class);

	private HTableInterface mObservationsTable;

	private static final byte[] TABLE_NAME = Bytes.toBytes("observations");

	private static final byte[] LOCATION_COLUMN_FAMILY = Bytes.toBytes("loc");

	private static final byte[] LOCATION_LATITUDE_COLUMN = Bytes.toBytes("lat");

	private static final byte[] LOCATION_LONGITUDE_COLUMN = Bytes
			.toBytes("lon");

	private static final byte[] LOCATION_ALTITUDE_COLUMN = Bytes.toBytes("alt");

	private static final byte[] LOCATION_HDOP_COLUMN = Bytes.toBytes("hdop");

	private static final byte[] LOCATION_VDOP_COLUMN = Bytes.toBytes("vdop");

	private final Index mIndex; 
	
	public ObservationRepositoryHBaseImpl() throws IOException {

		mIndex = new Index();
		
		Configuration myConf = HBaseConfiguration.create();

		mObservationsTable = new HTable(myConf, TABLE_NAME);

	}

	@Override
	public void storeDevice(Device device) {

		try {
			RowKey rowKey = new RowKey(device);
			Bucket bucket = mIndex.fetchBucket(rowKey);
			bucket.insert(rowKey, device);

			mObservationsTable.put(makePut(device));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	private static Put makePut(Device observation) {
		Put put = new Put(Bytes.toBytes(4));
//		put.add(LOCATION_COLUMN_FAMILY, LOCATION_LATITUDE_COLUMN,
//				Bytes.toBytes(observation.getLocation().getLatitude()));
//		put.add(LOCATION_COLUMN_FAMILY, LOCATION_LONGITUDE_COLUMN,
//				Bytes.toBytes(observation.getLocation().getLongitude()));
//		put.add(LOCATION_COLUMN_FAMILY, LOCATION_ALTITUDE_COLUMN,
//				Bytes.toBytes(observation.getLocation().getAltitude()));
//		put.add(LOCATION_COLUMN_FAMILY, LOCATION_HDOP_COLUMN,
//				Bytes.toBytes(observation.getLocation().getHDOP()));
//		put.add(LOCATION_COLUMN_FAMILY, LOCATION_VDOP_COLUMN,
//				Bytes.toBytes(observation.getLocation().getVDOP()));

		return put;
	}

	@SuppressWarnings("unused")
	private static Get makeGet(Observation observation) {
		//Get get = new Get(Bytes.toBytes(observation.getDeviceId()));
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Device findDevice(String deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Device> findLocalDevices(double latitude, double longitude,
			long radius, long limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
