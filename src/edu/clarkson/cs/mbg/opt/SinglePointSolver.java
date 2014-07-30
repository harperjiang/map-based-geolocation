package edu.clarkson.cs.mbg.opt;

import ilog.concert.IloException;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class SinglePointSolver {

	private List<DistanceConstraint> constraints;

	public SinglePointSolver() {
		super();
		this.constraints = new ArrayList<DistanceConstraint>();
	}

	public void addConstraint(DistanceConstraint constraint) {
		constraints.add(constraint);
	}

	public GeoPoint solve() {
		GeoPoint result = null;
		try {
			IloCplex cplex = new IloCplex();
			IloRange[] row = new IloRange[constraints.size()];
			IloNumVar[] var = applyConstraints(cplex, row);

			if (cplex.solve()) {
				double[] x = cplex.getValues(var);
				result = new GeoPoint(x[0], x[1]);
			}
			cplex.end();
		} catch (IloException e) {
			throw new RuntimeException("Concert exception caught", e);
		}
		return result;
	}

	protected IloNumVar[] applyConstraints(IloMPModeler model, IloRange row[])
			throws IloException {
		IloNumVar[] x = model.numVarArray(2, new double[] { 0, 0 },
				new double[] { Double.MAX_VALUE, Double.MAX_VALUE });

		double[] linearFactor = new double[] { 0, 0 };

		for (int i = 0; i < row.length; i++) {
			DistanceConstraint dc = constraints.get(i);
			Double linearx = dc.getFrom().latitude.doubleValue();
			Double lineary = dc.getFrom().longitude.doubleValue();
			row[i] = model.addLe(model.sum(model.prod(x[0], x[0]), model.prod(
					x[1], x[1]), model.scalProd(new double[] { -linearx * 2,
					-lineary * 2 }, x)), dc.getDistance().doubleValue()
					- linearx * linearx - lineary * lineary);
			linearFactor[0] -= linearx * 2;
			linearFactor[1] -= lineary * 2;
		}

		// Minimize error
		model.add(model.minimize(model.sum(model.prod(row.length, x[0], x[0]),
				model.prod(row.length, x[1], x[1]),
				model.scalProd(x, linearFactor))));

		return x;
	}
}
