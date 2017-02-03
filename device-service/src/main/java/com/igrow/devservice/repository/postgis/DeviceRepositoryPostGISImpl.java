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

package com.igrow.devservice.repository.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Vector;

import org.jvnet.hk2.annotations.Service;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.devservice.repository.DeviceRepository;
import com.igrow.devservice.repository.DeviceRepositoryException;
import com.igrow.model.Device;
import com.igrow.model.Location;

@Service
public class DeviceRepositoryPostGISImpl implements DeviceRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeviceRepositoryPostGISImpl.class);

	private static final String USER = "igrow-service-dev";

	private static final String PASSWORD = "igrow-service-dev";

	private static final String DATABASE = "igrow-service-dev";

	private static final String URL = "jdbc:postgresql://localhost:5432/"
			+ DATABASE;

	private static final String SELECT_DEVICE_SQL = "select "
			+ "d.device_id, d.os_type, d.os_version, d.push_token, "
			+ "d.manufacturer, d.model, d.product, d.device, "
			+ "d.last_known_location " + "from devices d WHERE d.device_id = ?";

	// TODO: There must be a better way to write the following
	// by setting a variable for the passed in as the
	// centre point/location/geometry object instead of setting
	// two query parameters.
	private static final String SELECT_LOCAL_DEVICES_SQL = "select "
			+ "d.device_id, d.os_type, d.os_version, d.push_token, "
			+ "d.manufacturer, d.model, d.product, d.device, "
			+ "d.last_known_location "
			+ "from devices d WHERE ST_DWithin(ST_GeomFromEWKB(?), d.last_known_location, ?) "
			+ "order by ST_Distance(ST_GeomFromEWKB(?),d.last_known_location) asc "
			+ "limit(?)";

	private static final String INSERT_DEVICE_SQL = "insert into devices "
			+ "(device_id, os_type, os_version, push_token, "
			+ "manufacturer, model, product, device, "
			+ "last_known_location) VALUES" + "(?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_DEVICE_SQL = "update devices "
			+ "SET os_version = ?, push_token = ?, manufacturer = ?, "
			+ "model = ?, product = ?, device = ?, "
			+ "last_known_location = ? " + "WHERE device_id = ?";

	private PreparedStatement mPreparedStatementSelectDevice;

	private PreparedStatement mPreparedStatementSelectLocalDevices;

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

			mPreparedStatementSelectLocalDevices = mConnection
					.prepareStatement(SELECT_LOCAL_DEVICES_SQL);

			mPreparedStatementInsertDevice = mConnection
					.prepareStatement(INSERT_DEVICE_SQL);

			mPreparedStatementUpdateDevice = mConnection
					.prepareStatement(UPDATE_DEVICE_SQL);

		} catch (Throwable t) {
			LOGGER.error("Unable to construct DeviceRepositoryPostGISImpl", t);
		}
	}

	@Override
	public synchronized void storeDevice(Device device) throws DeviceRepositoryException {

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
	public synchronized void close() throws IOException {

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
	public synchronized Device findDevice(String deviceId) {

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

	@Override
	public synchronized List<Device> findLocalDevices(double latitude, double longitude,
			long radius, long limit) {

		List<Device> devices = null;
		try {

			mConnection.setAutoCommit(false);

			Point centrePoint = new Point();
			centrePoint.dimension = 3;
			centrePoint.x = longitude;
			centrePoint.y = latitude;
			centrePoint.z = 0.0d;
			centrePoint.srid = 4326;

			PGgeometry geometry = new PGgeometry(centrePoint);

			mPreparedStatementSelectLocalDevices.setObject(1, geometry);
			// TODO: Fix this hack. Learn about coordinate systems and
			// projections
			mPreparedStatementSelectLocalDevices.setFloat(2,
					(float) radius / 111128f);
			mPreparedStatementSelectLocalDevices.setObject(3, geometry);
			mPreparedStatementSelectLocalDevices.setLong(4, limit);
			
			LOGGER.debug(mPreparedStatementSelectLocalDevices.toString());
			
			ResultSet resultSet = mPreparedStatementSelectLocalDevices
					.executeQuery();

			if (!resultSet.isBeforeFirst()) {
				return null;
			}

			// presize the vector to limit or Integer.MAX_VALUE, whichever is
			// smaller
			// as I don't think it's possible to obtain the resultSet size
			// without
			// iterating over it
			devices = new Vector<Device>((int) Math.min(limit,
					Integer.MAX_VALUE));

			while (resultSet.next()) {

				Device device = new Device();

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
					location.setHDOP(0.0f);  // need these to be set
					location.setVDOP(0.0f);  // in order to produce valid protobuf
					device.setLastKnownLocation(location);
				}

				if (device.isValid()) {
					devices.add(device);
				} else {
					for (String error : device.getDeviceProtoBuf()
							.findInitializationErrors())
						LOGGER.error(error);
				}

				mConnection.commit();

			}

		} catch (SQLException e) {
			LOGGER.error("findLocalDevices", e);
		} catch (Throwable t) {
			LOGGER.error("findLocalDevices", t);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("findLocalDevices", e);
				}
			}
		}
		
		if (devices != null && devices.size() == 0) {
			devices = null;
		}
		
		return devices;
	}
}
