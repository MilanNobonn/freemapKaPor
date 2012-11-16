package sk.freemap.kapor;

import java.io.IOException;

import javax.swing.JMenuItem;

import org.opengis.referencing.FactoryException;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

import sk.freemap.kapor.preferences.KaporPreferenceSetting;
import sk.freemap.kapor.preferences.PreferenceKeys;

public class KaporPlugin extends Plugin implements PreferenceKeys {
	public static String mwfUrl;

	public KaporPlugin(PluginInformation info) throws FactoryException,
			IOException {
		super(info);
		mwfUrl = Main.pref.get(FREEMAPKAPOR_MWFURL);
		if (mwfUrl == null || mwfUrl.length() == 0) {
			mwfUrl = "http://195.28.70.134/kapor2/maps/mapa.mwf";
			Main.pref.put(FREEMAPKAPOR_MWFURL, mwfUrl);
		}

		Projection.initCRS();
		Projection.initGrid();

		JMenuItem menuItem = new JMenuItem("Kapor");
		menuItem.addActionListener(new KaporMenuActionListener());
		Main.main.menu.add(menuItem);

		NavigatableComponent
				.addZoomChangeListener(new KaporZoomChangeListener());
	}
	
	@Override
    public PreferenceSetting getPreferenceSetting()
    {
    	return new KaporPreferenceSetting();
    }
}
