package sk.freemap.kapor;

import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;

public class PreferenceChangedListener implements
		org.openstreetmap.josm.data.Preferences.PreferenceChangedListener {

	@Override
	public void preferenceChanged(PreferenceChangeEvent e) {
		if (e.getKey() == ConfigKeys.FREEMAPKAPOR_MWFURL) {
			KaporPlugin.mwfUrl = e.getNewValue();
		}
	}

}
