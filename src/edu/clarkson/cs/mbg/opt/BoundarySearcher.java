package edu.clarkson.cs.mbg.opt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoundarySearcher<T> {

	private Set<T> searched;

	private Behavior<T> behavior;

	private List<T> boundary;

	private List<T> toExpand;

	public BoundarySearcher(Behavior<T> behavior) {
		this.behavior = behavior;
		this.searched = new HashSet<T>();
		this.boundary = new ArrayList<T>();
	}

	public void setStartPoint(T start) {
		this.boundary.add(start);
		this.toExpand.add(start);
	}

	public List<T> getBoundary() {
		return boundary;
	}

	public void search() {
		while (!toExpand.isEmpty()) {
			T next = toExpand.remove(0);
			List<T> following = behavior.expand(this, next);
			List<T> newsearch = new ArrayList<T>();
			List<T> passed = new ArrayList<T>();

			searched.add(next);

			for (T test : following) {
				if (searched.contains(test))
					continue;
				newsearch.add(test);
				if (behavior.test(test)) {
					passed.add(test);
				}
				searched.add(test);
			}
			// All new points passed test
			if (passed.size() == newsearch.size()) {
				boundary.remove(next);
			}
			boundary.addAll(passed);
			toExpand.addAll(passed);
		}
	}

	public static interface Behavior<T> {

		public List<T> expand(BoundarySearcher<T> searcher, T current);

		public boolean test(T point);
	}
}
