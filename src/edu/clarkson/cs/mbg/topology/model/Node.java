package edu.clarkson.cs.mbg.topology.model;

/**
 * @author harper
 * 
 */
public class Node {

	String label;

	Object context;

	public Node(String label) {
		super();
		this.label = label;
	}

	public Node(String label, Object context) {
		this(label);
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public <T> T getContext() {
		return (T) context;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

}
