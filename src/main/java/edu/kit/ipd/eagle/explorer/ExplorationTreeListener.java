package edu.kit.ipd.eagle.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.kit.ipd.eagle.port.hypothesis.IHypothesesSelection;
import edu.kit.ipd.eagle.port.hypothesis.IHypothesesSet;
import edu.kit.ipd.eagle.port.xplore.layer.ILayerEntry;

/**
 * Realizes the {@link TreeSelectionListener} which will process the data that will be shown by selecting elements of
 * the exploration tree.
 *
 * @author Dominik Fuchss
 *
 */
public class ExplorationTreeListener implements TreeSelectionListener {

	private JPanel dataPanel;
	private JPanel treePanel;

	private JTree tree;
	private JLabel titleForData;

	/**
	 * Create the tree selection listener. This will register the listener to the provided tree.
	 *
	 * @param tree         the tree to listen on.
	 * @param treePanel    the panel of the tree
	 * @param dataPanel    the panel to provide the data for the selected element of the tree
	 * @param titleForData the title label to provide the name or other information of the selected element
	 */
	public ExplorationTreeListener(JTree tree, JPanel treePanel, JPanel dataPanel, JLabel titleForData) {
		tree.addTreeSelectionListener(this);
		this.tree = tree;
		this.treePanel = treePanel;
		this.dataPanel = dataPanel;
		this.titleForData = titleForData;
		this.renderDefault();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		this.treePanel.setPreferredSize(this.tree.getPreferredSize());

		this.dataPanel.removeAll();

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
		if (node == null || !(node.getUserObject() instanceof DataWrapper)) {
			return;
		}
		DataWrapper<?> data = (DataWrapper<?>) node.getUserObject();

		switch (data.getType()) {
		case LAYER_ENTRY -> this.renderLayerEntry((ILayerEntry) data.getData());
		case HYPOTHESES_SET -> this.renderHypothesesSet((IHypothesesSet) data.getData());
		case SELECTION -> this.renderSelection((DataWrapper<IHypothesesSelection>) data);
		default -> this.renderDefault();
		}

	}

	private void renderLayerEntry(ILayerEntry step) {
		this.titleForData.setText("Layer Entry " + step.getId());
		Object[][] data = new Object[][] { //
				new Object[] { "Agent", step.getAgent() }, //
				new Object[] { "State of DS (before)", step.getStateRepresentationBeforeExecution() } //
		};
		this.createTable(new String[] { "Property", "Value" }, data);
	}

	private void renderHypothesesSet(IHypothesesSet set) {
		this.titleForData.setText("Hypotheses for " + set.getShortInfo());

		var hypos = set.getHypotheses();

		Object[][] data = new Object[hypos.size()][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new Object[] { hypos.get(i).getValue(), hypos.get(i).getConfidence() };
		}

		this.createTable(new String[] { "Hypothesis", "Confidence" }, data);
	}

	private void renderSelection(DataWrapper<IHypothesesSelection> selection) {
		this.titleForData.setText(selection.toString());

		var selections = selection.getData().getSelectedHypotheses();
		Object[][] data = new Object[selections.size()][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new Object[] { selections.get(i).getValue(), selections.get(i).getConfidence() };
		}

		this.createTable(new String[] { "Hypothesis", "Confidence" }, data);
	}

	private void renderDefault() {
		this.dataPanel.removeAll();
		this.dataPanel.setPreferredSize(new Dimension());
		this.titleForData.setText("Unknown Data");
	}

	private void createTable(String[] header, Object[][] data) {
		JTable table = new JTable(new DefaultTableModel(data, header));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		this.dataPanel.add(scrollPane, gbc);
		this.dataPanel.doLayout();
		this.dataPanel.repaint();
		this.dataPanel.setPreferredSize(new Dimension(table.getPreferredSize().width + 5, table.getPreferredSize().height + 5));
		((DefaultTableModel) table.getModel()).fireTableDataChanged();

	}
}
