/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)DeviceRepositoryPostGISImpl.java        
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

package com.argusat.gjl.devservice.repository.postgis;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argusat.gjl.devservice.repository.DeviceRepository;
import com.argusat.gjl.devservice.repository.DeviceRepositoryException;
import com.argusat.gjl.model.Device;
import com.argusat.gjl.model.Location;

public class DeviceRepositoryPostGISImpl implements DeviceRepository, Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeviceRepositoryPostGISImpl.class);

	private static final String USER = "argusat-gjl-dev";

	private static final String PASSWORD = "argusat-gjl-dev";

	private static final String DATABASE = "argusat-gjl-dev";

	private static final String URL = "jdbc:postgresql://localhost:5432/"
			+ DATABASE;

	private static final String SELECT_DEVICE_SQL = "select "
			+ "d.device_id, d.os_type, d.os_version, d.push_token, "
			+ "d.manufacturer, d.model, d.product, d.device, "
			+ "d.last_known_location " + "from devices d WHERE d.device_id = ?";

	private static final String INSERT_DEVICE_SQL = "insert into devices "
			+ "(device_id, os_type, os_version, push_token, "
			+ "manufacturer, model, product, device, "
			+ "last_known_location) VALUES" + "(?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_DEVICE_SQL = "update devices "
			+ "SET os_version = ?, push_token = ?, manufacturer = ?, "
			+ "model = ?, product = ?, device = ?, "
			+ "last_known_location = ? " + "WHERE device_id = ?";

	private PreparedStatement mPreparedStatementSelectDevice;

	private PreparedStatement mPreparedStatementInsertDevice;

	private PreparedStatement mPreparedStatementUpdateDevice;

	private Connection mConnection;

	public DeviceRepositoryPostGISImpl() throws ClassNotFoundException,
			SQLException {

		try {
			Class.forName("org.postgresql.Driver");
			mConnection = DriverManager.getConnection(URL, USER, PASSWORD);

			mPreparedStatementSelectDevice = mConnection
					.prepareStatement(SELECT_DEVICE_SQL);

			mPreparedStatementInsertDevice = mConnection
					.prepareStatement(INSERT_DEVICE_SQL);

			mPreparedStatementUpdateDevice = mConnection
					.prepareStatement(UPDATE_DEVICE_SQL);

		} catch (Throwable t) {
			LOGGER.error("Unable to construct DeviceRepositoryPostGISImpl", t);
		}
	}

	@Override
	public void storeDevice(Device device) throws DeviceRepositoryException {

		if (device == null) {
			throw new DeviceRepositoryException("Device was null!");
		}
		
		if (!device.isValid()) {
			throw new DeviceRepositoryException("Invalid device!");
		}
		
		try {

			mConnection.setAutoCommit(false);

			mPreparedStatementSelectDevice.setString(1, device.getDeviceId());

			ResultSet resultSet = mPreparedStatementSelectDevice.executeQuery();

			int rowsAffected = 0;

			PGgeometry geometry = null;
			Location lastKnownLocation = device.getLastKnownLocation();
			if (lastKnownLocation != null) {
				Point point = new Point();
				point.dimension = 3;
				point.x = lastKnownLocation.getLongitude();
				point.y = lastKnownLocation.getLatitude();
				point.z = lastKnownLocation.getAltitude();
				point.srid = 4326;

				geometry = new PGgeometry(point);
			}

			if (resultSet.next()) {
				// update
				mPreparedStatementUpdateDevice.setString(1,
						device.getOsVersion());
				mPreparedStatementUpdateDevice.setString(2,
						device.getPushToken());
				mPreparedStatementUpdateDevice.setString(3,
						device.getManufacturer());
				mPreparedStatementUpdateDevice.setString(4, device.getModel());
				mPreparedStatementUpdateDevice
						.setString(5, device.getProduct());
				mPreparedStatementUpdateDevice.setString(6, device.getDevice());

				if (geometry != null) {
					mPreparedStatementUpdateDevice.setObject(7, geometry);
				} else {
					mPreparedStatementUpdateDevice.setNull(7, Types.NULL);
				}
				// where clause
				mPreparedStatementUpdateDevice.setString(8,
						device.getDeviceId());

				rowsAffected = mPreparedStatementUpdateDevice.executeUpdate();
			} else {
				// insert
				mPreparedStatementInsertDevice.setString(1,
						device.getDeviceId());

				mPreparedStatementInsertDevice.setString(2, device.getOsType()
						.name());

				mPreparedStatementInsertDevice.setString(3,
						device.getOsVersion());

				mPreparedStatementInsertDevice.setString(4,
						device.getPushToken());

				mPreparedStatementInsertDevice.setString(5,
						device.getManufacturer());

				mPreparedStatementInsertDevice.setString(6, device.getModel());

				mPreparedStatementInsertDevice
						.setString(7, device.getProduct());

				mPreparedStatementInsertDevice.setString(8, device.getDevice());

				if (geometry != null) {
					mPreparedStatementInsertDevice.setObject(9, geometry);
				} else {
					mPreparedStatementInsertDevice.setNull(9, Types.NULL);
				}

				rowsAffected = mPreparedStatementInsertDevice.executeUpdate();
			}

			mConnection.commit();

			assert (rowsAffected == 1);

		} catch (SQLException e) {
			LOGGER.error("storeDevice", e);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("storeDevice", e);
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

		if (mPreparedStatementSelectDevice != null) {
			try {
				mPreparedStatementSelectDevice.close();
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

		if (mPreparedStatementUpdateDevice != null) {
			try {
				mPreparedStatementUpdateDevice.close();
			} catch (SQLException e) {
				LOGGER.error("close", e);
			}
		}

	}

	@Override
	public Device findDevice(String deviceId) {

		Device device = null;
		try {
			
			mPreparedStatementSelectDevice.setString(1, deviceId);
			ResultSet resultSet = mPreparedStatementSelectDevice.executeQuery();

			if (!resultSet.next()) {
				return device;
			}

			device = new Device();
			device.setDeviceId(resultSet.getString(1));
			device.setOsType(Device.OSType.valueOf(resultSet.getString(2)));
			device.setOsVersion(resultSet.getString(3));
			device.setPushToken(resultSet.getString(4));
			device.setManufacturer(resultSet.getString(5));
			device.setModel(resultSet.getString(6));
			device.setProduct(resultSet.getString(7));
			device.setDevice(resultSet.getString(8));

			PGgeometry geomLocation = (PGgeometry) resultSet.getObject(9);
			if (geomLocation != null) {
				Point point = geomLocation.getGeometry().getFirstPoint();
				Location location = new Location();
				location.setLatitude((float) point.y);
				location.setLongitude((float) point.x);
				location.setAltitude((float) point.z);
				device.setLastKnownLocation(location);
			}

		} catch (SQLException e) {
			LOGGER.error("findDevice", e);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("findDevice", e);
				}
			}

		}
		return device;
	}
}
