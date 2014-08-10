package edu.clarkson.cs.mbg.opt;

import java.math.BigDecimal;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class SDPMain {

	public static void main(String[] args) throws Exception {
		System.loadLibrary("jcsdp");

		SinglePointEstimator spe = new SinglePointEstimator();

		double[][] points = new double[][] { new double[] { 2, 2, 8, 7, 25 },
				new double[] { 6, 2, 8, 6, 25 },
				new double[] { 8, 6, 20, 18, 25 },
				new double[] { 3, 7, 10, 8, 25 } };

		for (double[] point : points) {
			DistConstraint dc = new DistConstraint(new GeoPoint(point[1],
					point[0]), new BigDecimal(point[2]), new BigDecimal(
					point[4]), new BigDecimal(point[3]));
			spe.addConstraint(dc);
		}

		GeoPoint result = spe.solve();
		System.out.println(result);
	}
}
