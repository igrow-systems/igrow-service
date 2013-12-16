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

package com.argusat.gjl.devservice.repository.postgis;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.model.Device;

public class DeviceRepositoryPostGISImpl implements DeviceRepository, Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeviceRepositoryPostGISImpl.class);

	private static final String URL = "jdbc:postgresql://localhost:5432/argusat-gjl-dev";

	private static final String USER = "argusat-gjl-dev";

	private static final String PASSWORD = "argusat-gjl-dev";

	private static final String DATABASE = "argusat-gjl-dev";

	private static final String GEOMETRY_TYPE = "POINTZ";

	private static final String SELECT_DEVICE_SQL = "select from devices "
			+ "(location, obs_timestamp, device_id, sensor_id, hdop, vdop, obs_type, value0, "
			+ "value1, value2, value3, value4 ) VALUES"
			// + "(ST_SetSRID(ST_MakePoint(?,?,?), 4326) ,?,?,?,?,?,?,?,?,?,?)";
			+ "(ST_GeomFromEWKB(?),?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String INSERT_DEVICE_SQL = "insert into devices "
			+ "(location, obs_timestamp, device_id, sensor_id, hdop, vdop, obs_type, value0, "
			+ "value1, value2, value3, value4 ) VALUES"
			// + "(ST_SetSRID(ST_MakePoint(?,?,?), 4326) ,?,?,?,?,?,?,?,?,?,?)";
			+ "(ST_GeomFromEWKB(?),?,?,?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_DEVICE_SQL = "update devices "
			+ "(location, obs_timestamp, device_id, sensor_id, hdop, vdop, obs_type, value0, "
			+ "value1, value2, value3, value4 ) VALUES"
			// + "(ST_SetSRID(ST_MakePoint(?,?,?), 4326) ,?,?,?,?,?,?,?,?,?,?)";
			+ "(ST_GeomFromEWKB(?),?,?,?,?,?,?,?,?,?,?,?)";

	private PreparedStatement mPreparedStatementInsertDevice;

	private Connection mConnection;

	public DeviceRepositoryPostGISImpl() throws ClassNotFoundException,
			SQLException {

		try {
			Class.forName("org.postgresql.Driver");
			mConnection = DriverManager.getConnection(URL, USER, PASSWORD);

			/*
			 * Add the geometry types to the connection. Note that you must cast
			 * the connection to the pgsql-specific connection implementation
			 * before calling the addDataType() method.PostGIS 2.1.1dev Manual
			 * 75 / 672
			 */
			// included from driverconfig.properties :
			// datatype.geometry=org.postgis.PGgeometry
			// datatype.box3d=org.postgis.PGbox3d
			// datatype.box2d=org.postgis.PGbox2d
			//
			// ((org.postgresql.PGConnection)
			// mConnection).addDataType("geometry",
			// Class.forName("org.postgis.PGgeometryLW"));
			((org.postgresql.PGConnection) mConnection).addDataType("geometry",
					org.postgis.PGgeometry.class);
			// ((org.postgresql.PGConnection) conn).addDataType("box3d",
			// Class.forName("org.postgis.PGbox3d"));
			mPreparedStatementInsertDevice = mConnection
					.prepareStatement(INSERT_DEVICE_SQL);
		} catch (Throwable t) {
			LOGGER.error(
					"Unable to construct ObservationRepositoryPostGISImpl", t);
		}
	}

	@Override
	public void storeDevice(Device device) {

		try {

			mConnection.setAutoCommit(false);

			// mPreparedStatementInsertDevice.setObject(1, geometry);
			//
			// // mPreparedStatementInsertObservation.setFloat(2, observation
			// // .getLocation().getLongitude());
			//
			// // mPreparedStatementInsertObservation.setFloat(3, observation
			// // .getLocation().getAltitude());
			//
			// mPreparedStatementInsertDevice.setTimestamp(2,
			// new Timestamp(device.getTimestamp()));
			//
			// mPreparedStatementInsertDevice.setString(3,
			// device.getDeviceId());
			//
			// if (device.getType() == ObservationType.TYPE_GNSS_CHANNEL) {
			// mPreparedStatementInsertDevice.setLong(4,
			// ((GnssChannelObservation) device).getPrn());
			// } else {
			// mPreparedStatementInsertDevice.setNull(4, Types.NULL);
			// }
			//
			// mPreparedStatementInsertDevice.setFloat(5, device
			// .getLocation().getHDOP());
			//
			// mPreparedStatementInsertDevice.setFloat(6, device
			// .getLocation().getVDOP());
			//
			// ObservationType observationType = device.getType();
			// mPreparedStatementInsertDevice.setShort(7,
			// (short) observationType.ordinal());
			//
			// float[] values = device.getValues();
			// if (values != null) {
			// for (int i = 0; i < 5; ++i) {
			// if (i < values.length) {
			// mPreparedStatementInsertDevice.setFloat(i + 8,
			// values[i]);
			// } else {
			// mPreparedStatementInsertDevice.setNull(i + 8,
			// Types.NULL);
			// }
			// }
			// } else {
			// mPreparedStatementInsertDevice.setNull(8, Types.NULL);
			// mPreparedStatementInsertDevice.setNull(9, Types.NULL);
			// mPreparedStatementInsertDevice.setNull(10, Types.NULL);
			// mPreparedStatementInsertDevice.setNull(11, Types.NULL);
			// mPreparedStatementInsertDevice.setNull(12, Types.NULL);
			// }

			int rowsAffected = mPreparedStatementInsertDevice.executeUpdate();
			mConnection.commit();

			assert (rowsAffected == 1);

		} catch (SQLException e) {
			LOGGER.error("storeObservations", e);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("storeObservations", e);
				}
			}
		}

	}

	@Override
	public void close() throws IOException {

		if (mConnection != null) {
			try {
				mConnection.close();
			} catch (SQLException e) {
				LOGGER.error("close", e);
			}
		}

		if (mPreparedStatementInsertDevice != null) {
			try {
				mPreparedStatementInsertDevice.close();
			} catch (SQLException e) {
				LOGGER.error("close", e);
			}
		}

	}

	@Override
	public Device findDevice(String deviceId) {
		
		
		
	}
}
