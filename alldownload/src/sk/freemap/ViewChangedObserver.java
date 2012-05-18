package sk.freemap;

import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.autodesk.mgjava.MGMap;
import com.autodesk.mgjava.MGViewChangedObserver;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;

public class ViewChangedObserver implements MGViewChangedObserver, Runnable {

	Collection<PreparedPolygon> kraje;
	MGMap map;

	volatile boolean processing;
	volatile int reqx, reqy;

	int minx;
	int maxx;
	int miny;
	int maxy;

	Exporter exporter;

	Date lastcommit;

	public ViewChangedObserver(Collection<PreparedPolygon> _kraje, MGMap _map,
			int _minx, int _maxx, int _miny, int _maxy) throws IOException,
			SQLException {
		kraje = _kraje;
		map = _map;

		minx = _minx;
		maxx = _maxx;
		miny = _miny;
		maxy = _maxy;

		exporter = new Exporter();
	}

	public synchronized void run() {
		processing = false;

		assert minx < maxx;
		assert miny < maxy;

		int width = (int) map.getWidth("M");
		int height = (int) map.getHeight("M");
		int step_size;

		if (width < height)
			step_size = width;
		else
			step_size = height;
		step_size = (int) (step_size * 0.8);

		lastcommit = new Date();

		Coordinate[] edges = { new Coordinate(), new Coordinate(),
				new Coordinate(), new Coordinate() };

		try {

			for (int x = minx; x < maxx; x = x + step_size) {
				for (int y = miny; y < maxy; y = y + step_size) {

					double w2 = width / 2;
					double h2 = height / 2;

					edges[0].x = x - w2;
					edges[0].y = y - h2;
					edges[1].x = x + w2;
					edges[1].y = y - h2;
					edges[2].x = x + w2;
					edges[2].y = y + h2;
					edges[3].x = x - w2;
					edges[3].y = y + h2;

					MultiPoint view = factory.createMultiPoint(edges);

					for (Iterator<PreparedPolygon> i = kraje.iterator(); i
							.hasNext();)
						if (i.next().intersects(view)) {
							getData(x, y);
							break;
						}

					if (new Date().getTime() - lastcommit.getTime() > 30)
						// Regular commit
						synchronized (exporter.conn) {
							exporter.conn.commit();
							lastcommit = new Date();
						}
				}
			}

			synchronized (exporter.conn) {
				exporter.conn.commit();
			}

			System.out.println("Done");

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void getData(int x, int y) {
		double scale = map.getScale();

		try {
			synchronized (exporter.conn) {
				PreparedStatement ps = exporter.conn
						.prepareStatement("SELECT t FROM downloaded WHERE x=? AND y=?");
				ps.setInt(1, x);
				ps.setInt(2, y);
				ps.execute();
				ResultSet rs = ps.getResultSet();
				if (rs.next())
					return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		while (processing)
			try {
				wait();
			} catch (InterruptedException e) {
			}

		processing = true;
		reqx = x;
		reqy = y;

		System.out.println(x);
		System.out.println(y);

		map.zoomScale(y, x, scale);

	}

	public synchronized void onViewChanged(MGMap map) {

		if (processing) {
			try {
				exporter.export(map);

				synchronized (exporter.conn) {
					PreparedStatement ps = exporter.conn
							.prepareStatement("INSERT INTO downloaded (x,y,t) SELECT ?,?,now()");
					ps.setDouble(1, reqx);
					ps.setDouble(2, reqy);
					ps.execute();

					exporter.conn.commit();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		processing = false;
		notifyAll();
	}

	private static GeometryFactory factory = new GeometryFactory();
}
