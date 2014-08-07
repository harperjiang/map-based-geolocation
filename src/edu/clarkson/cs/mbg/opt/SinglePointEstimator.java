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
import edu.clarkson.cs.mbg.geo.GeoRange;

public class SinglePointEstimator {

	private List<DistConstraint> constraints;

	private GeoRange range;

	public SinglePointEstimator() {
		super();
		this.constraints = new ArrayList<DistConstraint>();
	}

	public void addConstraint(DistConstraint constraint) {
		constraints.add(constraint);
	}

	public GeoRange getRange() {
		return range;
	}

	public void setRange(GeoRange range) {
		this.range = range;
	}

	public GeoPoint solve() {
		EstimatorContext context = analyze();

		MatrixBlock headerb = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.headerSize);
		MatrixBlock objectiveb = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.distSlackSize);
		for (int i = 0; i < context.distSlackSize; i++) {
			objectiveb.set(i, i, -1);
		}
		MatrixBlock lowerb = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.lowerSlackSize);
		MatrixBlock upperb = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.upperSlackSize);
		MatrixBlock rangeb = new MatrixBlock(MatrixBlock.TYPE_MATRIX,
				context.rangeSlackSize);

		BlockMatrix bm = new BlockMatrix(headerb, objectiveb, lowerb, upperb, rangeb);

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
				SparseBlock slackForObj = new SparseBlock(
						context.distBlockIndex, context.distSlackSize);
				slackForObj.fill(
						new int[] { distSlackCount, distSlackCount + 1 },
						new int[] { distSlackCount, distSlackCount + 1 },
						new double[] { -1, 1 });

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForObj);

				Constraint distc = new Constraint(sm, dc.getObjective()
						.doubleValue());
				cons.add(distc);

				distSlackCount += 2;
			}
			if (dc.getLowerbound() != null) {
				SparseBlock slackForLower = new SparseBlock(
						context.lowerBlockIndex, context.lowerSlackSize);
				slackForLower.fill(new int[] { lowerSlackCount },
						new int[] { lowerSlackCount }, new double[] { -1 });

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForLower);
				Constraint lowerc = new Constraint(sm, dc.getLowerbound()
						.doubleValue());
				cons.add(lowerc);

				lowerSlackCount += 1;
			}
			if (dc.getUpperbound() != null) {
				SparseBlock slackForUpper = new SparseBlock(
						context.upperBlockIndex, context.upperSlackSize);
				slackForUpper.fill(new int[] { upperSlackCount },
						new int[] { upperSlackCount }, new double[] { 1 });

				SparseMatrix sm = new SparseMatrix(context.matrixSize,
						sparseHeader, slackForUpper);
				Constraint upperc = new Constraint(sm, dc.getUpperbound()
						.doubleValue());
				cons.add(upperc);

				upperSlackCount += 1;
			}
		}

		if (range != null) {
			double xupper = range.start.longitude.add(range.size.longRange)
					.doubleValue();
			double xlower = range.start.longitude.doubleValue();
			double yupper = range.start.latitude.add(range.size.latRange)
					.doubleValue();
			double ylower = range.start.latitude.doubleValue();

			SparseBlock rangeHeaderX = new SparseBlock(1, 3);
			rangeHeaderX.fill(new int[] { 0 }, new int[] { 2 },
					new double[] { 1 });

			SparseBlock rangeHeaderY = new SparseBlock(1, 3);
			rangeHeaderY.fill(new int[] { 1 }, new int[] { 2 },
					new double[] { 1 });

			SparseBlock upperXTail = new SparseBlock(context.rangeBlockIndex, 4);
			upperXTail.fill(new int[] { 0 }, new int[] { 0 },
					new double[] { 1 });
			SparseBlock lowerXTail = new SparseBlock(context.rangeBlockIndex, 4);
			lowerXTail.fill(new int[] { 1 }, new int[] { 1 },
					new double[] { -1 });
			SparseBlock upperYTail = new SparseBlock(context.rangeBlockIndex, 4);
			upperYTail.fill(new int[] { 2 }, new int[] { 2 },
					new double[] { 1 });
			SparseBlock lowerYTail = new SparseBlock(context.rangeBlockIndex, 4);
			lowerYTail.fill(new int[] { 3 }, new int[] { 3 },
					new double[] { -1 });

			cons.add(new Constraint(new SparseMatrix(context.matrixSize,
					rangeHeaderX, upperXTail), xupper * 2));
			cons.add(new Constraint(new SparseMatrix(context.matrixSize,
					rangeHeaderX, lowerXTail), xlower * 2));
			cons.add(new Constraint(new SparseMatrix(context.matrixSize,
					rangeHeaderY, upperYTail), yupper * 2));
			cons.add(new Constraint(new SparseMatrix(context.matrixSize,
					rangeHeaderY, lowerYTail), ylower * 2));
		}

		CSDP csdp = new CSDP();
		csdp.setC(bm);
		for (Constraint con : cons)
			csdp.addConstraint(con);
		csdp.solve();

		// Extract Result
		BlockMatrix result = csdp.getX();

		double x = result.getBlocks()[0].get(0, 2);
		double y = result.getBlocks()[0].get(1, 2);

		return new GeoPoint(y, x);
	}

	EstimatorContext analyze() {
		EstimatorContext context = new EstimatorContext();

		context.headerSize = 3;
		context.matrixSize += context.headerSize;
		for (DistConstraint dc : constraints) {
			if (dc.getObjective() != null) {
				context.distSlackSize += 2;
			}
			if (dc.getUpperbound() != null) {
				context.upperSlackSize++;
			}
			if (dc.getLowerbound() != null) {
				context.lowerSlackSize++;
			}
		}
		context.matrixSize += context.distSlackSize + context.upperSlackSize
				+ context.lowerSlackSize;

		if (range != null) {
			context.rangeSlackSize = 4;
			context.matrixSize += 4;
		}
		int blockIndex = 2;
		if (context.distSlackSize != 0) {
			context.distBlockIndex = blockIndex;
			blockIndex++;
		}
		if (context.lowerSlackSize != 0) {
			context.lowerBlockIndex = blockIndex;
			blockIndex++;
		}
		if (context.upperSlackSize != 0) {
			context.upperBlockIndex = blockIndex;
			blockIndex++;
		}
		if (range != null) {
			context.rangeBlockIndex = blockIndex;
			blockIndex++;
		}

		return context;
	}

	static class EstimatorContext {
		int matrixSize = 0;
		int headerSize = 0;
		int distSlackSize = 0;
		int distBlockIndex = 0;
		int lowerSlackSize = 0;
		int lowerBlockIndex = 0;
		int upperSlackSize = 0;
		int upperBlockIndex = 0;
		int rangeSlackSize = 0;
		int rangeBlockIndex = 0;
	}
}
