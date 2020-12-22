package edu.kit.ipd.eagle.explorer;

import edu.kit.ipd.eagle.port.xplore.IExplorationResult;
import edu.kit.ipd.eagle.port.xplore.layer.ILayerEntry;

/**
 * The different node types of the exploration tree in a
 * {@link IExplorationResult}.
 *
 * @author Dominik Fuchss
 *
 */
public enum NodeType {
	/**
	 * Defines a set of hypotheses.
	 *
	 * @see ILayerEntry#getHypotheses()
	 */
	HYPOTHESES_SET,
	/**
	 * Defines a selection of hypotheses.
	 *
	 * @see ILayerEntry#getSelectionsFromBefore()
	 */
	SELECTION,
	/**
	 * Defines a {@link ILayerEntry} itself.
	 */
	LAYER_ENTRY
}
