package sk.freemap.kapor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Pattern;

import org.opengis.referencing.operation.TransformException;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;

import com.autodesk.mgjava.MGMapApplet;
import com.autodesk.mgjava.MGMapLayer;
import com.autodesk.mgjava.MGMapObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

public class ExportBudovy {
	private static Pattern budova_patt = Pattern.compile("budova.*");

	public static DataSet exportBudovy(MGMapApplet map)
			throws TransformException {
		DataSet dataset = new DataSet();

		MGMapLayer znacky_kl = map.getMapLayer("znacky_kl");
		MGMapLayer zappar = map.getMapLayer("zappar");
		MGMapLayer kladpar = map.getMapLayer("kladpar");

		if (znacky_kl == null || zappar == null || kladpar == null)
			return null;

		LinkedList<PreparedPolygon> kladpar_pgeom = new LinkedList<PreparedPolygon>();

		{
			Vector<MGMapObject> kladpar_objs = kladpar.getMapObjects();
			
			if (kladpar_objs == null)
				return null;
			
			for (Enumeration<MGMapObject> objs_enum = kladpar_objs.elements(); objs_enum
					.hasMoreElements();) {
				MGMapObject obj = objs_enum.nextElement();
				Geometry g = Conversion.object_to_geometry(map, obj);
				if (g != null)
					kladpar_pgeom.add(new PreparedPolygon((Polygonal) g));
			}
		}

		LinkedList<Point> budovy_znacky = new LinkedList<Point>();

		{
			Vector<MGMapObject> znacky_kl_objs = znacky_kl.getMapObjects();
			
			if (znacky_kl_objs == null)
				return null;

			for (Enumeration<MGMapObject> objs_enum = znacky_kl_objs.elements(); objs_enum
					.hasMoreElements();) {
				MGMapObject obj = objs_enum.nextElement();
				String name = obj.getName();

				if (budova_patt.matcher(name).matches()) {
					Point p = (Point) Conversion.object_to_geometry(map, obj);
					if (p != null)
						budovy_znacky.add(p);
				}
			}
		}

		LinkedList<PreparedPolygon> budovy_kladpar = new LinkedList<PreparedPolygon>();

		LinkedList<Geometry> lines = new LinkedList<Geometry>();

		for (Iterator<Point> b_znak_iter = budovy_znacky.iterator(); b_znak_iter
				.hasNext();) {
			Point p = b_znak_iter.next();

			// Find the smallest polygon covering the point p
			PreparedPolygon smallest = null;
			double smallest_area = 0;

			for (Iterator<PreparedPolygon> iter = kladpar_pgeom.iterator(); iter
					.hasNext();) {
				PreparedPolygon polygon = iter.next();
				if (polygon.contains(p)) {

					double area = polygon.getGeometry().getArea();
					if (smallest == null || area < smallest_area) {
						smallest = polygon;
						smallest_area = area;
					}
				}
			}

			if (smallest != null) {
				budovy_kladpar.add(smallest);
				lines.add(smallest.getGeometry().getBoundary());
			}
		}

		Vector<MGMapObject> zappar_objs = zappar.getMapObjects();
		
		if (zappar_objs == null)
			return null;

		for (Enumeration<MGMapObject> objs_enum = zappar_objs.elements(); objs_enum
				.hasMoreElements();) {
			MGMapObject obj = objs_enum.nextElement();

			String name = obj.getName();
			if (name.equals("hranica vlastnícka, užívacia")
					|| name.equals("viditeľná hranica so slučkou")
					|| name.equals("viditeľná hranica so zmenšenou slučkou")) {
				Geometry polyline = Conversion.object_to_geometry(map, obj);

				if (polyline != null) {
					for (Iterator<PreparedPolygon> iter = budovy_kladpar
							.iterator(); iter.hasNext();) {

						PreparedPolygon budova = iter.next();
						if (budova.intersects(polyline)) {
							// line intersects with building
							lines.add(polyline);
							break;
						}

					}
				}
			}
		}

		Polygonizer polygonizer = new Polygonizer();
		{
			Geometry[] lines_array = new Geometry[lines.size()];
			lines_array = lines.toArray(lines_array);
			GeometryCollection line_collection = new GeometryCollection(
					lines_array, factory);
			polygonizer.add(line_collection.union());
		}

		NodeCollection allNodes = new NodeCollection(dataset);

		Collection<Polygon> polygons = polygonizer.getPolygons();
		for (Iterator<Polygon> iter = polygons.iterator(); iter.hasNext();) {
			Polygon polygon = iter.next();
			PreparedPolygon prepared_polygon = new PreparedPolygon(polygon);

			for (Iterator<Point> budova_iter = budovy_znacky.iterator(); budova_iter
					.hasNext();) {
				Point b = budova_iter.next();

				if (prepared_polygon.contains(b)) {
					Coordinate[] exring = polygon.getExteriorRing()
							.getCoordinates();
					ArrayList<Node> nodes = new ArrayList<Node>(exring.length);
					for (int i = 0; i < exring.length; i++) {
						nodes.add(allNodes.getNode(exring[i].x, exring[i].y));
					}

					Way way = new Way();
					way.setNodes(nodes);
					way.put("building", "yes");
					way.put("source", "kapor2");
					dataset.addPrimitive(way);
					break;
				}
			}

		}

		return dataset;
	}

	private static GeometryFactory factory = new GeometryFactory();
}
