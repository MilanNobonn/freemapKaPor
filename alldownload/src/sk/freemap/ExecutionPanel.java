package sk.freemap;

import java.awt.Button;
import java.awt.Event;

import javax.swing.JPanel;

public class ExecutionPanel extends JPanel {
	String path;
	ViewChangedObserver o;
	Exporter exporter;

	public ExecutionPanel(Exporter _exporter, ViewChangedObserver observer) {
		o = observer;
		exporter = _exporter;
		add("East", new Button("Run"));
		add("East", new Button("Setup"));
	}

	public boolean action(Event evt, Object arg) {
		if (exporter.conn == null && arg.equals("Run"))
			arg = "Setup";

		if (arg.equals("Run")) {
			new Thread(o).start();
			return true;
		} else if (arg.equals("Setup")) {
			DatabaseConnectionSetup s = new DatabaseConnectionSetup(exporter);
			s.setVisible(true);
			return true;
		} else
			return false;
	}
}
