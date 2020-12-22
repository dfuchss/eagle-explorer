package edu.kit.ipd.eagle.explorer;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A simple FileChooser for files.
 *
 * @author Dominik Fuchss
 *
 */
public final class FileChooser {

	private final JFileChooser jfc;
	private final String saveExtension;
	private final List<String> extensions;

	private final Component parent;

	/**
	 * Create chooser by parent.
	 *
	 * @param parent         the parent component
	 * @param type           the name of type to load
	 * @param saveExtension  the extension of the type (for saving)
	 * @param moreExtensions more extension of the type (for opening)
	 */
	public FileChooser(Component parent, String type, String saveExtension, String... moreExtensions) {
		this.parent = parent;
		this.saveExtension = saveExtension;

		this.extensions = new ArrayList<>(Arrays.asList(moreExtensions));
		this.extensions.add(saveExtension);
		FileNameExtensionFilter filter = //
				new FileNameExtensionFilter(type, this.extensions.toArray(String[]::new));

		this.jfc = new JFileChooser();
		this.jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.jfc.setAcceptAllFileFilterUsed(false);
		this.jfc.setFileFilter(filter);

	}

	/**
	 * Open file dialog.
	 *
	 * @return the file or {@code null}
	 */
	public File openFile() {
		int result = this.jfc.showOpenDialog(this.parent);
		File selected = this.jfc.getSelectedFile();
		if (result == JFileChooser.APPROVE_OPTION && selected != null && selected.exists()) {
			return selected;
		}
		return null;
	}

	/**
	 * Save file dialog.
	 *
	 * @return the file or {@code null}
	 */
	public File saveFile() {
		int result = this.jfc.showSaveDialog(this.parent);
		File selected = this.jfc.getSelectedFile();
		if (result == JFileChooser.APPROVE_OPTION && selected != null) {
			if (selected.getName().endsWith(this.saveExtension)) {
				return selected;
			} else {
				return new File(selected.getAbsolutePath() + "." + this.saveExtension);
			}
		}
		return null;

	}
}
