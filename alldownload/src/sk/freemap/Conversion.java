package sk.freemap;

import java.util.ArrayList;

import com.autodesk.mgjava.MGCollection;
import com.autodesk.mgjava.MGMap;
import com.autodesk.mgjava.MGMapObject;
import com.autodesk.mgjava.MGPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

public class Conversion {
	private static final double epsilon = 1e-6;
	private static final GeometryFactory factory = new GeometryFactory();

	public static Coordinate[] convert_linear_vertices(
			final MGCollection vertices, final int begin, final int end) {

		final Coordinate[] coords = new Coordinate[end - begin];

		int counter = 0;
		double prev_x = 1e10, prev_y = 1e10;

		for (int l = begin; l < end; l++) {
			final MGPoint p = (MGPoint) vertices.item(l);
			final double x = p.getX(), y = p.getY();

			if (Math.abs(x - prev_x) > epsilon
					|| Math.abs(y - prev_y) > epsilon)
				coords[counter++] = new Coordinate(x, y);
		}

		if (coords.length == counter)
			return coords;

		final Coordinate[] coords2 = new Coordinate[coords.length];
		for (int i = 0; i < coords.length; i++)
			coords2[i] = coords[i];

		return coords2;
	}

	public static Geometry object_to_geometry(MGMap map, MGMapObject obj) {
		final MGCollection vertices = (MGCollection) (map
				.createObject("MGCollection"));
		final MGCollection numberOfVertices = (MGCollection) (map
				.createObject("MGCollection"));
		obj.getVertices(vertices, numberOfVertices);

		if (vertices.size() == 0 || numberOfVertices.size() == 0)
			return null;

		assert numberOfVertices.size() >= 1;

		final int numberOfVertices_size = numberOfVertices.size();

		String type = obj.getType();
		if (type == "Text" || type == "Point") {
			assert numberOfVertices_size == 1;
			assert (Integer) numberOfVertices.item(0) == 1;

			final MGPoint p = (MGPoint) vertices.item(0);

			return factory.createPoint(new Coordinate(p.getX(), p.getY()));

		} else if (type == "Polyline") {

			ArrayList<LineString> segments = new ArrayList<LineString>(
					numberOfVertices_size);

			int start = 0;
			for (int i = 0; i < numberOfVertices_size; i++) {
				final Integer num = (Integer) numberOfVertices.item(i);
				Coordinate[] t = convert_linear_vertices(vertices, start, start
						+ num);
				if (t.length >= 2)
					segments.add(factory.createLineString(t));
				start += num;
			}

			if (segments.size() == 0)
				return null;
			else if (segments.size() == 1)
				return segments.get(0);
			else {
				LineString[] line_strings = segments
						.toArray(new LineString[segments.size()]);
				return factory.createMultiLineString(line_strings);
			}

		} else if (type == "Polygon") {

			ArrayList<LinearRing> rings = new ArrayList<LinearRing>(
					numberOfVertices_size);

			int start = 0;
			for (int i = 0; i < numberOfVertices_size; i++) {
				final Integer num = (Integer) numberOfVertices.item(i);
				Coordinate[] t = convert_linear_vertices(vertices, start, start
						+ num);
				if (t.length >= 3)
					rings.add(factory.createLinearRing(t));
				else {
					// Can't create a polygon with invalid outer ring
					if (i == 0)
						return null;
				}
				start += num;
			}

			Geometry newPolygon;
			if (rings.size() == 0)
				return null;
			else if (rings.size() == 1)
				newPolygon = factory.createPolygon(rings.get(0), null);
			else {
				LinearRing shell = rings.get(0);
				rings.remove(0);
				LinearRing[] holes = rings
						.toArray(new LinearRing[rings.size()]);
				newPolygon = factory.createPolygon(shell, holes);
			}

			return newPolygon.buffer(0);

		} else
			return null;
	}

}
