package edu.kit.ipd.eagle.explorer;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import edu.kit.ipd.eagle.port.xplore.IExplorationResult;
import edu.kit.ipd.eagle.port.xplore.dto.ExplorationResultDTO;

/**
 * The main class of the agent analysis explorer.
 *
 * @author Dominik Fuchss
 *
 */
public final class MainFrame {

	private static final String NAME = "Agent Analysis Explorer";

	private JFrame frame;
	private FileChooser fc;

	private JLabel titleForData;
	private JPanel treePanel;
	private JPanel dataPanel;
	private JMenuItem load;
	private JMenuItem exit;
	private JTextArea explorationId;

	/**
	 * Launch the application.
	 *
	 * @param args the command line parameters (will be ignored)
	 */
	public static void main(String[] args) {
		MainFrame.setSystemLookAndFeel();
		EventQueue.invokeLater(() -> {
			try {
				MainFrame window = new MainFrame();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Cannot set system's look and feel ..");
		}
	}

	private MainFrame() {
		this.initialize();
		this.fc = new FileChooser(this.frame, "JSON", "json");
		this.loadControllers();
	}

	private void loadControllers() {
		this.load.addActionListener(e -> this.loadFile());
		this.exit.addActionListener(e -> System.exit(0));
	}

	private void loadFile() {
		File json = this.fc.openFile();
		if (json == null) {
			return;
		}
		this.loadFile(json);
	}

	private void loadFile(File file) {
		try {
			var result = ExplorationResultDTO.load(file);
			this.loadExploration(result);
			this.frame.setTitle(MainFrame.NAME + " - " + file.getName());
		} catch (IOException e) {
			this.setToEmptyData();
			JOptionPane.showMessageDialog(this.frame, "Error: " + e.getMessage(), "Error while loading", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadExploration(IExplorationResult result) {
		JTree tree = TreeBuilder.buildTree(this.treePanel, result);
		new ExplorationTreeListener(tree, this.dataPanel, this.titleForData);
		this.explorationId.setText(result.getId());
	}

	private void setToEmptyData() {
		this.treePanel.removeAll();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setTitle(MainFrame.NAME);
		this.frame.setBounds(100, 100, 800, 650);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 2.0, 1.0, 0.0, 1.0 };
		this.frame.getContentPane().setLayout(gridBagLayout);

		this.treePanel = new JPanel();
		GridBagConstraints gbcTreePanel = new GridBagConstraints();
		gbcTreePanel.insets = new Insets(0, 0, 5, 0);
		gbcTreePanel.fill = GridBagConstraints.BOTH;
		gbcTreePanel.gridx = 0;
		gbcTreePanel.gridy = 0;
		this.frame.getContentPane().add(new JScrollPane(this.treePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), gbcTreePanel);

		GridBagLayout gblTreePanel = new GridBagLayout();
		gblTreePanel.columnWeights = new double[] { 1.0 };
		gblTreePanel.rowWeights = new double[] { 1.0 };
		this.treePanel.setLayout(gblTreePanel);

		this.explorationId = new JTextArea();
		this.explorationId.setEnabled(false);
		this.explorationId.setLineWrap(true);

		GridBagConstraints gbcInputText = new GridBagConstraints();
		gbcInputText.insets = new Insets(0, 0, 5, 0);
		gbcInputText.fill = GridBagConstraints.BOTH;
		gbcInputText.gridx = 0;
		gbcInputText.gridy = 1;
		this.frame.getContentPane().add(new JScrollPane(this.explorationId, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), gbcInputText);
		this.explorationId.setColumns(10);

		this.titleForData = new JLabel("");
		this.titleForData.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(0, 0, 5, 0);
		gbcTitle.gridx = 0;
		gbcTitle.gridy = 2;
		this.frame.getContentPane().add(this.titleForData, gbcTitle);

		this.dataPanel = new JPanel();
		GridBagConstraints gbcDataPanel = new GridBagConstraints();
		gbcDataPanel.fill = GridBagConstraints.BOTH;
		gbcDataPanel.gridx = 0;
		gbcDataPanel.gridy = 3;
		this.frame.getContentPane().add(new JScrollPane(this.dataPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), gbcDataPanel);
		GridBagLayout gblDataPanel = new GridBagLayout();
		gblDataPanel.columnWeights = new double[] { 1.0 };
		gblDataPanel.rowWeights = new double[] { 1.0 };
		this.dataPanel.setLayout(gblDataPanel);

		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);

		this.load = new JMenuItem("Load JSON");
		file.add(this.load);

		this.exit = new JMenuItem("Exit");
		file.add(this.exit);
	}

}
