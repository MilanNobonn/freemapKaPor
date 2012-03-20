package sk.freemap.kapor;

import java.applet.AppletContext;
import java.net.MalformedURLException;

import com.autodesk.mgjava.MGMapApplet;
import com.autodesk.mgjava.MGMapContext;

public class KatApplet extends MGMapApplet implements MGMapContext {
	private static final long serialVersionUID = 2295804469040609429L;

	public AppletContext getAppletContext() {
		return new FakeAppletContext();
	}

	public void showDocument(java.net.URL url) {
		// System.out.println("showDocument " + url.toString());
	}

	public void showDocument(java.net.URL url, java.lang.String target) {
		// System.out.println("showDocument " + url.toString());

	}

	public void showStatus(java.lang.String status) {
		// System.out.println("showStatus " + status);
	}

	public java.lang.String getParameter(java.lang.String name) {
		if (name == "mwfUrl") {
			return KaporPlugin.mwfUrl;
		}
		return "";
	}

	public java.net.URL getDocumentBase() {
		try {
			return new java.net.URL("file:///test.html");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public java.net.URL getCodeBase() {
		try {
			return new java.net.URL("file:///test.jar");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
