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

package com.argusat.gjl.observice.repository.postgis;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import org.postgis.PGgeometry;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.model.Observation.ObservationType;
import com.argusat.gjl.observice.repository.ObservationRepository;

public class ObservationRepositoryPostGISImpl implements ObservationRepository,
		Closeable {

	private static final Logger LOGGER = Logger
			.getLogger(ObservationRepositoryPostGISImpl.class.getSimpleName());

	private static final String URL = "jdbc:postgresql://localhost:5432/argusat-gjl-dev";

	private static final String USER = "argusat-gjl-dev";

	private static final String PASSWORD = "argusat-gjl-dev";

	private static final String DATABASE = "argusat-gjl-dev";

	private static final String GEOMETRY_TYPE = "POINTZ";

	private static final String INSERT_OBSERVATION_SQL = "insert into observations "
			+ "(location, obs_timestamp, device_id, hdop, vdop, obs_type, value0, "
			+ "value1, value2, value3, value4 ) VALUES"
			+ "(ST_SetSRID(ST_MakePoint(?,?,?), 4326) ,?,?,?,?,?,?,?,?,?,?)";

	private PreparedStatement mPreparedStatementInsertObservation;

	private Connection mConnection;

	public ObservationRepositoryPostGISImpl() throws ClassNotFoundException,
			SQLException {

		Class.forName("org.postgresql.Driver");
		mConnection = DriverManager.getConnection(URL, USER, PASSWORD);

		/*
		 * Add the geometry types to the connection. Note that you must cast the
		 * connection to the pgsql-specific connection implementation before
		 * calling the addDataType() method.PostGIS 2.1.1dev Manual 75 / 672
		 */
		// Currently the jar ships with a configuration file,
		// driverconfig.properties , which apparently registers
		// the types we're interested in so no need to do the
		// following.
		// ((org.postgresql.PGConnection) conn).addDataType("geometry",
		// Class.forName("org.postgis.PGgeometry"));
		// ((org.postgresql.PGConnection) conn).addDataType("box3d",
		// Class.forName("org.postgis.PGbox3d"));
		mPreparedStatementInsertObservation = mConnection
				.prepareStatement(INSERT_OBSERVATION_SQL);
	}

	@Override
	public void storeObservation(Observation observation) {

		/*
		 * Create a statement and execute a select query.
		 */
		Statement s = null;
		try {
			s = mConnection.createStatement();

			ResultSet r = s.executeQuery("select geom,id from ");
			while (r.next()) {
				/*
				 * Retrieve the geometry as an object then cast it to the
				 * geometry type. Print things out.
				 */
				PGgeometry geom = (PGgeometry) r.getObject(1);
				int id = r.getInt(2);
				System.out.println("Row " + id + ":");
				System.out.println(geom.toString());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void storeObservations(List<Observation> observations) {

		try {

			mConnection.setAutoCommit(false);

			for (Observation observation : observations) {

				mPreparedStatementInsertObservation.setInt(1,
						(int) (observation.getLocation().getLatitude() * 1e6));

				mPreparedStatementInsertObservation.setInt(2,
						(int) (observation.getLocation().getLongitude() * 1e6));

				mPreparedStatementInsertObservation.setInt(3,
						(int) (observation.getLocation().getAltitude() * 1e6));
				
				mPreparedStatementInsertObservation.setTimestamp(4,
						new Timestamp(observation.getTimestamp()));

				mPreparedStatementInsertObservation.setLong(5,
						observation.getDeviceId());

				mPreparedStatementInsertObservation.setFloat(6, observation
						.getLocation().getHDOP());

				mPreparedStatementInsertObservation.setFloat(7, observation
						.getLocation().getVDOP());

				ObservationType observationType = observation.getType();
				mPreparedStatementInsertObservation.setShort(8,
						(short) observationType.ordinal());

				float[] values = observation.getValues();
				if (values != null) {
					mPreparedStatementInsertObservation.setFloat(9, values[0]);
					mPreparedStatementInsertObservation.setFloat(10, values[1]);
					mPreparedStatementInsertObservation.setFloat(11, values[2]);
					mPreparedStatementInsertObservation.setFloat(12, values[3]);
					mPreparedStatementInsertObservation.setFloat(13, values[4]);
				} else {
					mPreparedStatementInsertObservation.setFloat(9, 0.0f);
					mPreparedStatementInsertObservation.setFloat(10, 0.0f);
					mPreparedStatementInsertObservation.setFloat(11, 0.0f);
					mPreparedStatementInsertObservation.setFloat(12, 0.0f);
					mPreparedStatementInsertObservation.setFloat(13, 0.0f);
				}

				int rowsAffected = mPreparedStatementInsertObservation
						.executeUpdate();
				mConnection.commit();

				assert (rowsAffected == 1);
			}

		} catch (SQLException e) {
			LOGGER.throwing(
					ObservationRepositoryPostGISImpl.class.getSimpleName(),
					"storeObservations", e);
		} finally {
			if (mConnection != null) {
				try {
					mConnection.setAutoCommit(true);
				} catch (SQLException e) {
					LOGGER.throwing(ObservationRepositoryPostGISImpl.class
							.getSimpleName(), "storeObservations", e);
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
				LOGGER.throwing(
						ObservationRepositoryPostGISImpl.class.getSimpleName(),
						"close", e);
			}
		}

		if (mPreparedStatementInsertObservation != null) {
			try {
				mPreparedStatementInsertObservation.close();
			} catch (SQLException e) {
				LOGGER.throwing(
						ObservationRepositoryPostGISImpl.class.getSimpleName(),
						"close", e);
			}
		}

	}
}
