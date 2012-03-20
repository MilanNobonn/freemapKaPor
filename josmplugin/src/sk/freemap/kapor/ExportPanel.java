package sk.freemap.kapor;

import java.awt.Button;
import java.awt.Event;

import javax.swing.JPanel;

import org.opengis.referencing.operation.TransformException;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;

public class ExportPanel extends JPanel {

	private static final long serialVersionUID = 3914697032208183485L;

	public ExportPanel() {
		add("East", new Button("Export"));
		add("East", new Button("Budovy"));
	}

	public boolean action(Event evt, Object arg) {
		try {
			KatApplet map = KaporMenuActionListener.applet;

			DataSet dataset;

			if (arg.equals("Export"))
				dataset = ExportAll.exportAll(map);
			else if (arg.equals("Budovy"))
				dataset = ExportBudovy.exportBudovy(map);
			else
				return false;

			if (dataset.getNodes().size() > 0)
				Main.main.addLayer(new OsmDataLayer(dataset, OsmDataLayer
						.createNewName(), null));

			return true;

		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
