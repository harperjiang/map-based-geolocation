package edu.clarkson.cs.mbg.topology.model;

import org.apache.commons.lang3.Validate;

public class Path {

	private Node from;

	private Node to;

	private Object value;

	public Path(Node from, Node to, Object value) {
		Validate.isTrue(!from.equals(to));
		this.from = from;
		this.to = to;
		this.value = value;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}

	public Object getValue() {
		return value;
	}

	public void setFrom(Node from) {
		this.from = from;
	}

	public void setTo(Node to) {
		this.to = to;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Path))
			return false;
		Path other = (Path) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
