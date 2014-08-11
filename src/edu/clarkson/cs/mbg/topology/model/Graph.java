package edu.clarkson.cs.mbg.topology.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Graph {

	private Set<Node> nodes;

	private Map<Node, Map<Node, List<Path>>> paths;

	private Map<Node, Map<Node, List<Path>>> backwards;

	private Set<Node> entrance;

	public Graph() {
		super();
		this.nodes = new HashSet<Node>();
		this.paths = new HashMap<Node, Map<Node, List<Path>>>();
		this.backwards = new HashMap<Node, Map<Node, List<Path>>>();
		this.entrance = new HashSet<Node>();
	}

	public void addPath(Path path) {
		if (!nodes.contains(path.getFrom())) {
			entrance.add(path.getFrom());
		}
		if (entrance.contains(path.getTo())) {
			entrance.remove(path.getTo());
		}

		nodes.add(path.getFrom());
		nodes.add(path.getTo());

		if (!paths.containsKey(path.getFrom())) {
			Map<Node, List<Path>> value = new HashMap<Node, List<Path>>();
			paths.put(path.getFrom(), value);
		}
		if (!paths.get(path.getFrom()).containsKey(path.getTo())) {
			paths.get(path.getFrom()).put(path.getTo(), new ArrayList<Path>());
		}
		paths.get(path.getFrom()).get(path.getTo()).add(path);

		if (!backwards.containsKey(path.getTo())) {
			backwards.put(path.getTo(), new HashMap<Node, List<Path>>());
		}
		if (!backwards.get(path.getTo()).containsKey(path.getFrom())) {
			backwards.get(path.getTo()).put(path.getFrom(),
					new ArrayList<Path>());
		}
		backwards.get(path.getTo()).get(path.getFrom()).add(path);
	}

	public void merge(Node remain, Node remove) {
		if (!nodes.contains(remain) || !nodes.contains(remove)) {
			return;
		}
		// Remove all the paths between remain and remove
		paths.get(remain).remove(remove);
		paths.get(remove).remove(remain);
		backwards.get(remain).remove(remove);
		backwards.get(remove).remove(remain);

		// Remove remove
		nodes.remove(remove);
		entrance.remove(remove);

		// These are the paths with remove as from， change them to remain
		Map<Node, List<Path>> f = paths.remove(remove);
		for (Entry<Node, List<Path>> entry : f.entrySet()) {
			for (Path p : entry.getValue()) {
				backwards.get(p.getTo()).remove(remove);
				addPath(new Path(remain, p.getTo(), p.getValue()));
			}
		}
		// These are the paths with remove as to
		Map<Node, List<Path>> b = backwards.remove(remove);
		for (Entry<Node, List<Path>> entry : b.entrySet()) {
			for (Path p : entry.getValue()) {
				paths.get(p.getFrom()).remove(remove);
				addPath(new Path(p.getFrom(), remain, p.getValue()));
			}
		}
	}

	public void merge(Graph another) {
		for (Entry<Node, Map<Node, List<Path>>> fromEntry : another.paths
				.entrySet()) {
			for (Entry<Node, List<Path>> toEntry : fromEntry.getValue()
					.entrySet()) {
				for (Path path : toEntry.getValue())
					addPath(path);
			}
		}
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public Map<Node, Map<Node, List<Path>>> getPaths() {
		return paths;
	}

	public Set<Node> getEntrance() {
		return entrance;
	}

}
