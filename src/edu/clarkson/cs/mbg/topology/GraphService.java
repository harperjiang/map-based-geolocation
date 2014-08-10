package edu.clarkson.cs.mbg.topology;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.mbg.topology.model.Graph;
import edu.clarkson.cs.mbg.topology.model.Node;
import edu.clarkson.cs.mbg.topology.model.Path;

public class GraphService {

	public void heavyduty(Graph graph) {
		for (Entry<Node, Map<Node, List<Path>>> fromEntry : graph.getPaths()
				.entrySet()) {
			for (Entry<Node, List<Path>> toEntry : fromEntry.getValue()
					.entrySet()) {
				if (toEntry.getValue().size() > 1) {
					System.out.println(MessageFormat.format(
							"From:{0},to:{1},path count {2}", fromEntry
									.getKey(), toEntry.getKey(), toEntry
									.getValue().size()));
				}
			}
		}

	}

}
