package sk.freemap.kapor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.openstreetmap.josm.Main;

public class KaporMenuActionListener implements ActionListener {

	public static KatApplet applet = null;
	public static JFrame frame = null;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (frame == null) {
			frame = new JFrame("KAPOR");
			frame.setSize(800, 600);
			frame.setVisible(true);

			frame.add("North", new ExportPanel());

			applet = new KatApplet();
			frame.add(applet);

			applet.init();
			applet.start();
		}

		frame.setVisible(true);

		if (Main.map != null) {
			KaporZoomChangeListener.setAppletZoom();
		}
	}
}
