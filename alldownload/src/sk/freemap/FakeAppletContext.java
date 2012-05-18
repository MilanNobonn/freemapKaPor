package sk.freemap;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class FakeAppletContext implements AppletContext {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getApplet(java.lang.String)
	 */
	@Override
	public Applet getApplet(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getApplets()
	 */
	@Override
	public Enumeration<Applet> getApplets() {
		return new Vector<Applet>(0).elements();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getAudioClip(java.net.URL)
	 */
	@Override
	public AudioClip getAudioClip(URL url) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getImage(java.net.URL)
	 */
	@Override
	public Image getImage(URL url) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getStream(java.lang.String)
	 */
	@Override
	public InputStream getStream(String key) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getStreamKeys()
	 */
	@Override
	public Iterator<String> getStreamKeys() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#setStream(java.lang.String,
	 * java.io.InputStream)
	 */
	@Override
	public void setStream(String key, InputStream stream) throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showDocument(java.net.URL)
	 */
	@Override
	public void showDocument(URL url) {
		// System.out.println(url.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showDocument(java.net.URL,
	 * java.lang.String)
	 */
	@Override
	public void showDocument(URL url, String target) {
		// System.out.println(url.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showStatus(java.lang.String)
	 */
	@Override
	public void showStatus(String status) {
		// System.out.println(status);
	}

}
