package sk.freemap.dumper;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SavePanel extends JPanel {
	public FileOutputStream stream;

	public SavePanel ()
	{
		add("East", new Button("Save"));
	}
	
	public boolean action(Event evt, Object arg) {
		if (arg.equals("Save")) {

			Frame parent = new Frame();
			FileDialog fd = new FileDialog(parent, "Please choose a file:",
					FileDialog.LOAD);
			fd.setVisible(true);
			String selectedItem = fd.getFile();
			if (selectedItem != null) {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}

				File ffile = new File(fd.getDirectory() + File.separator
						+ fd.getFile());
				try {
					stream = new FileOutputStream(ffile);
				} catch (FileNotFoundException fnf) {
					JOptionPane.showMessageDialog(null, "File not found...",
							"Alert", JOptionPane.ERROR_MESSAGE);
				}
			}
			return true;
		} else
			return false;
	}
}
