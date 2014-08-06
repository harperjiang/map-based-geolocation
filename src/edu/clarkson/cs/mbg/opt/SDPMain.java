package edu.clarkson.cs.mbg.opt;

import edu.clarkson.cs.clientlib.csdp.BlockMatrix;
import edu.clarkson.cs.clientlib.csdp.CSDP;
import edu.clarkson.cs.clientlib.csdp.Constraint;
import edu.clarkson.cs.clientlib.csdp.MatrixBlock;
import edu.clarkson.cs.clientlib.csdp.SparseBlock;
import edu.clarkson.cs.clientlib.csdp.SparseMatrix;

public class SDPMain {

	public static void main(String[] args) throws Exception {
		System.loadLibrary("jcsdp");

		CSDP csdp = new CSDP();

		int size = 19;

		// MatrixBlock c = new MatrixBlock(MatrixBlock.TYPE_MATRIX, 19);
		MatrixBlock c1 = new MatrixBlock(MatrixBlock.TYPE_MATRIX, 3);

		MatrixBlock c2 = new MatrixBlock(MatrixBlock.TYPE_MATRIX, 8);
		MatrixBlock c3 = new MatrixBlock(MatrixBlock.TYPE_MATRIX, 4);
		MatrixBlock c4 = new MatrixBlock(MatrixBlock.TYPE_MATRIX, 4);
		// c2.fill(new double[] { -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
		// 0, 0, 0 });

		for (int i = 0; i < 8; i++) {
			c2.set(i, i, -1);
		}

		BlockMatrix param = new BlockMatrix(c1, c2, c3, c4);

		csdp.setC(param);

		double[][] points = new double[][] { new double[] { 2, 2, 8, 7, 25 },
				new double[] { 6, 2, 8, 6, 25 },
				new double[] { 8, 6, 20, 18, 25 },
				new double[] { 3, 7, 10, 8, 25 } };

		// Point measured distance
		for (int i = 0; i < points.length; i++) {
			double[] point = points[i];
			// Constraint 1
			SparseBlock sbheader = new SparseBlock(1, 3);
			sbheader.fill(new int[] { 0, 0, 0, 1, 1, 2 }, new int[] { 0, 1, 2,
					1, 2, 2 }, new double[] { point[0] * point[0],
					point[0] * point[1], -point[0], point[1] * point[1],
					-point[1], 1 });

			// SparseBlock sbd = sbheader.append(
			// new int[] { 3 + i * 2, 4 + i * 2 }, new int[] { 3 + i * 2,
			// 4 + i * 2 }, new double[] { -1, 1 });

			// SparseBlock sbl = sbheader.append(new int[] { 11 + i },
			// new int[] { 11 + i }, new double[] { -1 });
			//
			// SparseBlock sbu = sbheader.append(new int[] { 15 + i },
			// new int[] { 15 + i }, new double[] { 1 });

			SparseBlock sbdtail = new SparseBlock(2, 8);
			sbdtail.fill(new int[] { 2 * i, 2 * i + 1 }, new int[] {
					2 * i, 2 * i + 1 }, new double[] { -1, 1 });

			SparseBlock sbltail = new SparseBlock(3, 4);
			sbltail.fill(new int[] { i }, new int[] { i }, new double[] { -1 });

			SparseBlock sbutail = new SparseBlock(4, 4);
			sbutail.fill(new int[] { i }, new int[] { i }, new double[] { 1 });

			// SparseMatrix distSm = new SparseMatrix(size, sbd);
			SparseMatrix distSm = new SparseMatrix(size, sbheader, sbdtail);
			Constraint dist = new Constraint(distSm, point[2]);
			csdp.addConstraint(dist);

			// SparseMatrix lowerSm = new SparseMatrix(size, sbl);
			SparseMatrix lowerSm = new SparseMatrix(size, sbheader, sbltail);
			Constraint lower = new Constraint(lowerSm, point[3]);
			csdp.addConstraint(lower);

			// SparseMatrix upperSm = new SparseMatrix(size, sbu);
			SparseMatrix upperSm = new SparseMatrix(size, sbheader, sbutail);
			Constraint upper = new Constraint(upperSm, point[4]);
			csdp.addConstraint(upper);
		}

		// The following 3 constraints says Z starts with I
		SparseBlock sb41 = new SparseBlock(1, 3);
		sb41.fill(new int[] { 0 }, new int[] { 0 }, new double[] { 1 });
		SparseMatrix sm4 = new SparseMatrix(size, sb41);
		Constraint con4 = new Constraint(sm4, 1);
		csdp.addConstraint(con4);

		SparseBlock sb51 = new SparseBlock(1, 3);
		sb51.fill(new int[] { 1 }, new int[] { 1 }, new double[] { 1 });
		SparseMatrix sm5 = new SparseMatrix(size, sb51);
		Constraint c5 = new Constraint(sm5, 1);
		csdp.addConstraint(c5);

		SparseBlock sb61 = new SparseBlock(1, 3);
		sb61.fill(new int[] { 0, 0, 1 }, new int[] { 0, 1, 1 }, new double[] {
				1, 1, 1 });
		SparseMatrix sm6 = new SparseMatrix(size, sb61);
		Constraint c6 = new Constraint(sm6, 2);
		csdp.addConstraint(c6);

		csdp.solve();

		System.out.println(csdp.getX());
		System.out.println(csdp.getPrimalObjective());
	}
}
