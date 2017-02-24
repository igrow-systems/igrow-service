/* -*- mode: java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObservationRepositoryPostGISImpl.java        
 *
 * Copyright (c) 2013 - 2014, 2017 Argusat Limited
 * 10 Underwood Road,  Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

package com.igrow.observice.repository.postgis;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igrow.model.EnvironmentalSensorObservation;
import com.igrow.model.Location;
import com.igrow.model.Observation;
import com.igrow.model.ObservationCollection;
import com.igrow.model.Observation.ModeType;
import com.igrow.model.Observation.ObservationType;
import com.igrow.observice.repository.ObservationRepository;

public class ObservationRepositoryPostGISImpl implements ObservationRepository,
		Closeable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ObservationRepositoryPostGISImpl.class);

	private static final String USER = "igrow-service-dev";

	private static final String PASSWORD = "igrow-service-dev";

	private static final String DATABASE = "igrow-service-dev";

	private static final String URL = "jdbc:postgresql://localhost:5432/"
			+ DATABASE;

	private static final String INSERT_OBSERVATION_SQL = "insert into observations "
			+ "(location, obs_timestamp, device_id, sensor_id, hdop, vdop, obs_type, obs_mode, "
			+ "value0, value1, value2, value3, value4 ) VALUES"
			// + "(ST_SetSRID(ST_MakePoint(?,?,?), 4326) ,?,?,?,?,?,?,?,?,?,?)";
			+ "(ST_GeomFromEWKB(?),?,?,?,?,?,?,?,?,?,?,?,?)";

	// private static final String SELECT_LOCAL_OBSERVATIONS_SQL =
	// "select distinct on (o.device_id) "
	private static final String SELECT_SPATIALLY_LOCAL_OBSERVATIONS_SQL = "select "
			+ " o.location, o.obs_timestamp, o.device_id, o.sensor_id, "
			+ " o.hdop, o.vdop, o.obs_type, o.obs_mode, "
			+ " o.value0, o.value1, o.value2, o.value3, o.value4"
			+ " from observations o"
			+ " where ST_DWithin(ST_GeomFromEWKB(?), o.location, ?) "
			+ " order by obs_timestamp desc" + " limit(?)";
	
	private static final String SELECT_TEMPORALLY_LOCAL_OBSERVATIONS_SQL = "select "
			+ " o.location, o.obs_timestamp, o.device_id, o.sensor_id, "
			+ " o.hdop, o.vdop, o.obs_type, o.obs_mode, "
			+ " o.value0, o.value1, o.value2, o.value3, o.value4"
			+ " from observations o"
			+ " where o.sensor_id = '?'"
			+ " and o.obs_timestamp >= ?"
			+ " and o.obs_timestamp <= ?"
			+ " order by obs_timestamp desc" + " limit(?)";

	private PreparedStatement mPreparedStatementInsertObservation;

	private PreparedStatement mPreparedStatementSelectSpatiallyLocalObservations;
	
	private PreparedStatement mPreparedStatementSelectTemporallyLocalObservations;

	private Connection mConnection;

	public ObservationRepositoryPostGISImpl() throws ClassNotFoundException,
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
			mPreparedStatementInsertObservation = mConnection
					.prepareStatement(INSERT_OBSERVATION_SQL);
			mPreparedStatementSelectSpatiallyLocalObservations = mConnection
					.prepareStatement(SELECT_SPATIALLY_LOCAL_OBSERVATIONS_SQL);
			mPreparedStatementSelectTemporallyLocalObservations = mConnection
					.prepareStatement(SELECT_TEMPORALLY_LOCAL_OBSERVATIONS_SQL);
			
		} catch (Throwable t) {
			LOGGER.error(
					"Unable to construct ObservationRepositoryPostGISImpl", t);
		}
	}

	@Override
	public void storeObservation(Observation observation) {

	}

	@Override
	public synchronized void storeObservations(List<Observation> observations) {

		try {

			mConnection.setAutoCommit(false);

			for (Observation observation : observations) {

				Point point = new Point();
				point.dimension = 3;
				point.x = observation.getLocation().getLongitude();
				point.y = observation.getLocation().getLatitude();
				point.z = observation.getLocation().getAltitude();
				point.srid = 4326;

				PGgeometry geometry = new PGgeometry(point);

				mPreparedStatementInsertObservation.setObject(1, geometry);

				// mPreparedStatementInsertObservation.setFloat(2, observation
				// .getLocation().getLongitude());

				// mPreparedStatementInsertObservation.setFloat(3, observation
				// .getLocation().getAltitude());

				mPreparedStatementInsertObservation.setTimestamp(2,
						new Timestamp(observation.getTimestamp()));

//				if (observation.getType() == ObservationType.TYPE_ENVIRONMENTAL_SENSOR) {
//				mPreparedStatementInsertObservation.setLong(4,
//						((EnvironmentalSensorObservation) observation).getPrn());
//			} else {
				mPreparedStatementInsertObservation.setNull(3, Types.NULL);
//			}
				
				mPreparedStatementInsertObservation.setString(4,
						observation.getSensorId());

				mPreparedStatementInsertObservation.setFloat(5, observation
						.getLocation().getHDOP());

				mPreparedStatementInsertObservation.setFloat(6, observation
						.getLocation().getVDOP());

				ObservationType observationType = observation.getType();
				mPreparedStatementInsertObservation.setString(7,
						observationType.name());

				ModeType mode = observation.getMode();
				mPreparedStatementInsertObservation.setString(8, mode.name());

				float[] values = observation.getValues();
				if (values != null) {
					for (int i = 0; i < 5; ++i) {
						if (i < values.length) {
							mPreparedStatementInsertObservation.setFloat(i + 9,
									values[i]);
						} else {
							mPreparedStatementInsertObservation.setNull(i + 9,
									Types.NULL);
						}
					}
				} else {
					mPreparedStatementInsertObservation.setNull(9, Types.NULL);
					mPreparedStatementInsertObservation.setNull(10, Types.NULL);
					mPreparedStatementInsertObservation.setNull(11, Types.NULL);
					mPreparedStatementInsertObservation.setNull(12, Types.NULL);
					mPreparedStatementInsertObservation.setNull(13, Types.NULL);
				}

				int rowsAffected = mPreparedStatementInsertObservation
						.executeUpdate();
				mConnection.commit();

				assert (rowsAffected == 1);
			}

		} catch (SQLException e) {
			LOGGER.error("storeObservations", e);
		} catch (Throwable t) {
			LOGGER.error("storeObservations", t);
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
	public synchronized void close() throws IOException {

		if (mConnection != null) {
			try {
				mConnection.close();
			} catch (SQLException e) {
				LOGGER.error("close", e);
			}
		}

		if (mPreparedStatementInsertObservation != null) {
			try {
				mPreparedStatementInsertObservation.close();
			} catch (SQLException e) {
				LOGGER.error("close", e);
			}
		}

	}

	@Override
	public synchronized List<Observation> findObservations(
			float latitude,
			float longitude,
			long radius,
			long limit) {

		ObservationCollection obsCollection = new ObservationCollection();
		try {

			mConnection.setAutoCommit(false);

			Point point = new Point();
			point.dimension = 3;
			point.x = longitude;
			point.y = latitude;
			point.z = 0.0d;
			point.srid = 4326;

			PGgeometry geometry = new PGgeometry(point);

			mPreparedStatementSelectSpatiallyLocalObservations.setObject(1, geometry);
			// TODO: Fix this hack. Learn about coordinate systems and
			// projections
			mPreparedStatementSelectSpatiallyLocalObservations.setFloat(2,
					(float) radius / 111128f);
			mPreparedStatementSelectSpatiallyLocalObservations.setLong(3, limit);

			LOGGER.debug(mPreparedStatementSelectSpatiallyLocalObservations.toString());

			ResultSet resultSet = mPreparedStatementSelectSpatiallyLocalObservations
					.executeQuery();

			if (!resultSet.isBeforeFirst()) {
				return null;
			}

			while (resultSet.next()) {

				ObservationType obsType = ObservationType.valueOf(resultSet
						.getString(7));

				Observation observation = Observation.newObservation(obsType);

				PGgeometry geomLocation = (PGgeometry) resultSet.getObject(1);
				point = geomLocation.getGeometry().getFirstPoint();
				float hdop = resultSet.getFloat(5);
				float vdop = resultSet.getFloat(6);

				Location location = new Location();
				location.setLatitude((float) point.y);
				location.setLongitude((float) point.x);
				location.setAltitude((float) point.z);
				location.setHDOP(hdop);
				location.setVDOP(vdop);
				observation.setLocation(location);

				observation.setTimestamp(resultSet.getTimestamp(2).getTime());
				observation.setSensorId(resultSet.getString(4));
				observation.setMode(ModeType.valueOf(resultSet.getString(8)));

//				if (observation.getType() == ObservationType.TYPE_ENVIRONMENTAL_SENSOR) {
//					((EnvironmentalSensorObservation) observation).setPrn(resultSet
//							.getInt(4));
//				}

				float[] values = new float[5];

				for (int i = 0; i < 5; ++i) {
					values[i] = resultSet.getFloat(i + 9);
				}
				observation.setValues(values);
				// TODO: The following ensures the protobuf gets constructed by
				// a call to buildPartial().
				// This needs attention so that a method named isValid() with no
				// implied
				// side effects actually has no side effects!
				if (observation.isValid()) {
					obsCollection.add(observation);
				} else {
					for (String error : observation.getObservationProtoBuf()
							.findInitializationErrors())
						LOGGER.error(error);
				}

				mConnection.commit();

			}

		} catch (SQLException e) {
			LOGGER.error("findObservations", e);
		} catch (Throwable t) {
			LOGGER.error("findObservations", t);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("findObservations", e);
				}
			}
		}
		return obsCollection.getObservations();
	}


	@Override
	public synchronized List<Observation> findObservations(
			final String sensorId,
			long timestampStart,
			long timestampEnd,
			long limit) {

		ObservationCollection obsCollection = new ObservationCollection();
		try {

			mConnection.setAutoCommit(false);

			mPreparedStatementSelectTemporallyLocalObservations.setString(1,
					sensorId);
			mPreparedStatementSelectTemporallyLocalObservations.setTimestamp(2,
					new Timestamp(timestampStart));
			mPreparedStatementSelectTemporallyLocalObservations.setTimestamp(3,
					new Timestamp(timestampEnd));
			mPreparedStatementSelectTemporallyLocalObservations.setLong(4,
					limit);

			LOGGER.debug(mPreparedStatementSelectTemporallyLocalObservations.toString());

			ResultSet resultSet = mPreparedStatementSelectTemporallyLocalObservations
					.executeQuery();

			if (!resultSet.isBeforeFirst()) {
				return null;
			}

			while (resultSet.next()) {

				ObservationType obsType = ObservationType.valueOf(resultSet
						.getString(7));

				Observation observation = Observation.newObservation(obsType);

				PGgeometry geomLocation = (PGgeometry) resultSet.getObject(1);
				Point point = new Point();
				point = geomLocation.getGeometry().getFirstPoint();
				float hdop = resultSet.getFloat(5);
				float vdop = resultSet.getFloat(6);

				Location location = new Location();
				location.setLatitude((float) point.y);
				location.setLongitude((float) point.x);
				location.setAltitude((float) point.z);
				location.setHDOP(hdop);
				location.setVDOP(vdop);
				observation.setLocation(location);

				observation.setTimestamp(resultSet.getTimestamp(2).getTime());
				observation.setSensorId(resultSet.getString(4));
				observation.setMode(ModeType.valueOf(resultSet.getString(8)));

//				if (observation.getType() == ObservationType.TYPE_ENVIRONMENTAL_SENSOR) {
//					((EnvironmentalSensorObservation) observation).setPrn(resultSet
//							.getInt(4));
//				}

				float[] values = new float[5];

				for (int i = 0; i < 5; ++i) {
					values[i] = resultSet.getFloat(i + 9);
				}
				observation.setValues(values);
				// TODO: The following ensures the protobuf gets constructed by
				// a call to buildPartial().
				// This needs attention so that a method named isValid() with no
				// implied
				// side effects actually has no side effects!
				if (observation.isValid()) {
					obsCollection.add(observation);
				} else {
					for (String error : observation.getObservationProtoBuf()
							.findInitializationErrors())
						LOGGER.error(error);
				}

				mConnection.commit();

			}

		} catch (SQLException e) {
			LOGGER.error("findObservations", e);
		} catch (Throwable t) {
			LOGGER.error("findObservations", t);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.error("findObservations", e);
				}
			}
		}
		return obsCollection.getObservations();
	}
}
