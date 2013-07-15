package sk.freemap;

import javax.swing.JFrame;

import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws IOException,
			FactoryException, SQLException {
		int minx = -591312 - 20;
		int maxx = -165436 + 20;
		int miny = -1334763 - 20;
		int maxy = -1132697 + 20;

		JFrame mainFrame = new JFrame("Kataster Downloader");
		mainFrame.setSize(800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);

		String mwfUrl;
		if (args.length < 1)
			mwfUrl = "http://195.28.70.134/kapor2/maps/mapa.mwf";
		else
			mwfUrl = args[0];

		KatApplet katapplet = new KatApplet(mwfUrl);
		Exporter exporter = new Exporter();

		ViewChangedObserver observer = new ViewChangedObserver(exporter,
				Kraje.load(), katapplet, minx, maxx, miny, maxy);

		ExecutionPanel savePanel = new ExecutionPanel(exporter, observer);
		mainFrame.add("East", savePanel);

		mainFrame.add(katapplet);

		katapplet.init();
		katapplet.start();
		katapplet.setViewChangedObserver(observer);
	}
}
