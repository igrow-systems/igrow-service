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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgis.PGgeometry;

import com.argusat.gjl.model.Observation;
import com.argusat.gjl.observice.repository.ObservationRepository;

public class ObservationRepositoryPostGISImpl implements ObservationRepository,
		Closeable {

	private static final String URL = "jdbc:postgresql://localhost:5432/database";

	private static final String USER = "argusat-gjl-dev";

	private static final String PASSWORD = "argusat-gjl-dev";

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
		// ((org.postgresql.PGConnection) conn).addDataType("geometry",
		// Class.forName("org.postgis.PGgeometry"));
		// ((org.postgresql.PGConnection) conn).addDataType("box3d",
		// Class.forName("org.postgis.PGbox3d"));

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
	public void storeObservations(Observation[] observations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {

		try {
			mConnection.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
