package edu.clarkson.cs.mbg.opt;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.clientlib.csdp.BlockMatrix;
import edu.clarkson.cs.clientlib.csdp.CSDP;
import edu.clarkson.cs.clientlib.csdp.Constraint;
import edu.clarkson.cs.clientlib.csdp.MatrixBlock;
import edu.clarkson.cs.clientlib.csdp.SparseBlock;
import edu.clarkson.cs.clientlib.csdp.SparseMatrix;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoUtils;

public class SinglePointEstimator {

	private List<DistConstraint> constraints;

	public SinglePointEstimator() {
		super();
		this.constraints = new ArrayList<DistConstraint>();
	}

	public void addConstraint(DistConstraint constraint) {
		constraints.add(constraint);
	}

	public GeoPoint solve() {
		EstimatorContext context = analyze();

		MatrixBlock header = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.headerSize);
		MatrixBlock objective = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.distSlackSize);
		MatrixBlock lower = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.lowerSlackSize);
		MatrixBlock upper = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.upperSlackSize);

		BlockMatrix bm = new BlockMatrix(header, objective, lower, upper);

		List<Constraint> cons = new ArrayList<Constraint>();

		int distSlackCount = 0;
		int lowerSlackCount = 0;
		int upperSlackCount = 0;

		for (DistConstraint dc : constraints) {
			double x = dc.getFrom().longitude.doubleValue();
			double y = dc.getFrom().latitude.doubleValue();

			SparseBlock sparseHeader = new SparseBlock(1, context.headerSize);
			sparseHeader.fill(new int[] { 0, 0, 0, 1, 1, 2 }, new int[] { 0, 1,
					2, 1, 2, 2 },
					new double[] { x * x, x * y, -x, y * y, -y, 1 });

			if (dc.getObjective() != null) {
				SparseBlock slackForObj = new SparseBlock(2,
						context.distSlackSize);
				slackForObj.fill(
						new int[] { distSlackCount, distSlackCount + 1 },
						new int[] { distSlackCount, distSlackCount + 1 },
						new double[] { -1, 1 });

				objective.set(distSlackCount, distSlackCount, -1);
				objective.set(distSlackCount, distSlackCount, -1);

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForObj);
				Constraint distc = new Constraint(sm,
						GeoUtils.distanceToLat(dc.getObjective(),
								dc.getFrom().latitude).pow(2).doubleValue());
				cons.add(distc);

				distSlackCount += 2;
			}
			if (dc.getLowerbound() != null) {
				SparseBlock slackForLower = new SparseBlock(3,
						context.lowerSlackSize);
				slackForLower.fill(new int[] { lowerSlackCount },
						new int[] { lowerSlackCount }, new double[] { -1 });

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForLower);
				Constraint lowerc = new Constraint(sm, GeoUtils
						.distanceToLat(dc.getLowerbound(),
								dc.getFrom().latitude).pow(2).doubleValue());
				cons.add(lowerc);

				lowerSlackCount += 1;
			}
			if (dc.getUpperbound() != null) {
				SparseBlock slackForUpper = new SparseBlock(3,
						context.upperSlackSize);
				slackForUpper.fill(new int[] { upperSlackCount },
						new int[] { upperSlackCount }, new double[] { 1 });

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForUpper);
				Constraint upperc = new Constraint(sm, GeoUtils
						.distanceToLat(dc.getUpperbound(),
								dc.getFrom().latitude).pow(2).doubleValue());
				cons.add(upperc);

				upperSlackCount += 1;
			}
		}

		CSDP csdp = new CSDP();
		csdp.setC(bm);
		for (Constraint con : cons)
			csdp.addConstraint(con);
		csdp.solve();

		// Extract Result
		BlockMatrix result = csdp.getX();

		double x = result.getBlocks()[0].get(0, 1);
		double y = result.getBlocks()[0].get(0, 2);

		return new GeoPoint(y, x);
	}

	EstimatorContext analyze() {
		EstimatorContext context = new EstimatorContext();

		return context;
	}

	static class EstimatorContext {
		int matrixSize;
		int headerSize;
		int distSlackSize;
		int lowerSlackSize;
		int upperSlackSize;
	}
}
