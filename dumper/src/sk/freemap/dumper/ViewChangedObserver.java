package sk.freemap.dumper;

import java.io.IOException;

import com.autodesk.mgjava.MGMap;
import com.autodesk.mgjava.MGViewChangedObserver;

public class ViewChangedObserver implements MGViewChangedObserver {
	protected SavePanel panel;

	public ViewChangedObserver(SavePanel p) {
		panel = p;
	}

	public void onViewChanged(MGMap map) {
		if (panel.stream != null) {
			try {
				Exporter.export(map, panel.stream);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
