package edu.kit.ipd.eagle.explorer;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.kit.ipd.eagle.port.hypothesis.IHypothesesSelection;
import edu.kit.ipd.eagle.port.hypothesis.IHypothesesSet;
import edu.kit.ipd.eagle.port.xplore.IExplorationResult;
import edu.kit.ipd.eagle.port.xplore.layer.ILayerEntry;

/**
 * Defines a tree builder to generate {@link JTree JTrees} by
 * {@link IExplorationResult IExploratioResults}.
 *
 * @author Dominik Fuchss
 *
 */
public final class TreeBuilder {

	private TreeBuilder() {
		throw new IllegalAccessError();
	}

	/**
	 * Create a new tree.
	 *
	 * @param treePanel the panel to add the tree. The panel will be cleared before
	 *                  adding the tree.
	 * @param result    the exploration result which will be represented by the tree
	 * @return the created tree
	 */
	public static JTree buildTree(JPanel treePanel, IExplorationResult result) {
		var step = result.getExplorationRoot();
		var root = TreeBuilder.createTree(step, null);

		treePanel.removeAll();
		JTree tree = new JTree(root);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		treePanel.add(tree, gbc);
		treePanel.setPreferredSize(tree.getPreferredSize());
		return tree;
	}

	private static DefaultMutableTreeNode createTree(ILayerEntry step, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new DataWrapper<>(NodeType.LAYER_ENTRY, step, es -> "Layer Entry " + es.getId()));
		TreeBuilder.addChildren(node, step);
		if (parent != null) {
			parent.add(node);

		}
		return node;
	}

	private static void addChildren(DefaultMutableTreeNode node, ILayerEntry step) {
		if (step.getSelectionsFromBefore() != null && !step.getSelectionsFromBefore().isEmpty()) {
			DefaultMutableTreeNode selections = new DefaultMutableTreeNode("Selections");
			node.add(selections);
			for (var selection : step.getSelectionsFromBefore()) {
				TreeBuilder.createLeaves(selections, selection);
			}
		}
		if (step.getHypotheses() != null && !step.getHypotheses().isEmpty()) {
			DefaultMutableTreeNode hypotheses = new DefaultMutableTreeNode("Hypotheses");
			node.add(hypotheses);
			for (var hypos : step.getHypotheses()) {
				TreeBuilder.createLeaves(hypotheses, hypos);
			}
		}
		if (step.getChildren() != null && !step.getChildren().isEmpty()) {
			DefaultMutableTreeNode children = new DefaultMutableTreeNode("Children");
			node.add(children);
			for (var child : step.getChildren()) {
				TreeBuilder.createTree(child, children);
			}
		}

	}

	private static void createLeaves(DefaultMutableTreeNode node, IHypothesesSelection selection) {
		DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new DataWrapper<>(NodeType.SELECTION, selection, hs -> "Hypotheses Selection for " + hs.getAllHypotheses().getShortInfo()));
		node.add(leaf);
	}

	private static void createLeaves(DefaultMutableTreeNode node, IHypothesesSet hypos) {
		DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new DataWrapper<>(NodeType.HYPOTHESES_SET, hypos, IHypothesesSet::getShortInfo));
		node.add(leaf);
	}

}
