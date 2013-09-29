package com.argusat.gjl;

import com.argusat.gjl.service.observation.LocationProtoBuf.Location;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation;
import com.argusat.gjl.service.observation.ObservationProtoBuf.Observation.C0NObservation;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static void main(String[] args) {
		System.out.println("Hello World!");

		Location location1 = com.argusat.gjl.service.observation.LocationProtoBuf.Location.newBuilder().setLatitude(1234354)
				.setLongitude(-787632).build();
		C0NObservation C0NObservation1 = C0NObservation.newBuilder().build();

		Observation observation = Observation.newBuilder()
				.setLocation(location1).setC0Nobservation(C0NObservation1)
				.build();

	}
}
