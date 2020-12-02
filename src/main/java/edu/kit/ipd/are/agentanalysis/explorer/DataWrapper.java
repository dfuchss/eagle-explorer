package edu.kit.ipd.are.agentanalysis.explorer;

import java.util.function.Function;

/**
 * A simple data wrapper class which defindes the data for a {@link NodeType}.
 *
 * @author Dominik Fuchss
 *
 * @param <T> the actual type of data
 */
public final class DataWrapper<T> {
	private T data;
	private Function<T, String> prettyString;
	private NodeType type;

	/**
	 * Create data wrapper by node type, data, and function to create a string
	 * representation of the data.
	 *
	 * @param type   the node type
	 * @param data   the data
	 * @param pretty the function which will be used to generate
	 *               {@link #toString()}.
	 */
	public DataWrapper(NodeType type, T data, Function<T, String> pretty) {
		this.type = type;
		this.data = data;
		this.prettyString = pretty;
	}

	/**
	 * Get the node type of this data wrapper.
	 *
	 * @return the node type
	 */
	public NodeType getType() {
		return this.type;
	}

	/**
	 * Get the data of this wrapper.
	 *
	 * @return the data
	 */
	public T getData() {
		return this.data;
	}

	@Override
	public String toString() {
		return this.prettyString.apply(this.data);
	}
}