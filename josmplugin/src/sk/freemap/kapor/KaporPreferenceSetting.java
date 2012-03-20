/**
 * 
 */
package sk.freemap.kapor;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.tools.GBC;

/**
 * @author C5117830
 *
 */
public class KaporPreferenceSetting implements PreferenceSetting {
	JCheckBox prefExportName;

	/* (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.preferences.PreferenceSetting#addGui(org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane)
	 */
	@Override
	public void addGui(final PreferenceTabbedPane gui) {
		
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        prefExportName = new JCheckBox("Export names.", Main.pref.getBoolean(ConfigKeys.FREEMAPKAPOR_EXPORT_NAME, false));
        settingsPanel.add(prefExportName, GBC.eol());

        JPanel tab = gui.createPreferenceTab("kapor2", "Kapor2 Plugin", "Kapor2 exporting of cadastral data");
        tab.add(settingsPanel, GBC.eol().fill(GBC.VERTICAL ));
	}

	/* (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.preferences.PreferenceSetting#ok()
	 */
	@Override
	public boolean ok() {
		Main.pref.put(ConfigKeys.FREEMAPKAPOR_EXPORT_NAME, prefExportName.isSelected());
		return false;
	}

}
