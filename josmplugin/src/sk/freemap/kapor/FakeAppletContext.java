package sk.freemap.kapor;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

public class FakeAppletContext implements AppletContext {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getApplet(java.lang.String)
	 */
	@Override
	public Applet getApplet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getApplets()
	 */
	@Override
	public Enumeration<Applet> getApplets() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getAudioClip(java.net.URL)
	 */
	@Override
	public AudioClip getAudioClip(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getImage(java.net.URL)
	 */
	@Override
	public Image getImage(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getStream(java.lang.String)
	 */
	@Override
	public InputStream getStream(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#getStreamKeys()
	 */
	@Override
	public Iterator<String> getStreamKeys() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showDocument(java.net.URL)
	 */
	@Override
	public void showDocument(URL url) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showDocument(java.net.URL,
	 * java.lang.String)
	 */
	@Override
	public void showDocument(URL url, String target) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.AppletContext#showStatus(java.lang.String)
	 */
	@Override
	public void showStatus(String status) {
		// TODO Auto-generated method stub

	}

}
