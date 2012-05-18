package sk.freemap;

import java.awt.Button;
import java.awt.Event;

import javax.swing.JPanel;

public class ExecutionPanel extends JPanel {
	String path;
	ViewChangedObserver o;

	public ExecutionPanel(ViewChangedObserver observer) {
		o = observer;
		add("East", new Button("Run"));
		add("East", new Button("Setup"));
	}

	public boolean action(Event evt, Object arg) {

		if (arg.equals("Run")) {
			new Thread(o).start();
			return true;
		} else if (arg.equals("Setup")) {
			DatabaseConnectionSetup s = new DatabaseConnectionSetup();
			s.setVisible(true);
			return true;
		} else
			return false;
	}
}
