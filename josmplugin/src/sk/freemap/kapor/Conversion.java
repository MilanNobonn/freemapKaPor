package sk.freemap.kapor;

import org.opengis.referencing.operation.TransformException;

import com.autodesk.mgjava.MGCollection;
import com.autodesk.mgjava.MGMapApplet;
import com.autodesk.mgjava.MGMapObject;
import com.autodesk.mgjava.MGPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class Conversion {
	private static GeometryFactory factory = new GeometryFactory();

	public static Coordinate[] convert_vertices(MGCollection vertices)
			throws TransformException {

		int vertices_size = vertices.size();
		Coordinate[] coords = new Coordinate[vertices_size];

		for (int l = 0; l < vertices_size; l++) {
			MGPoint p = (MGPoint) vertices.item(l);
			coords[l] = new Coordinate(p.getX(), p.getY());
		}
		return coords;
	}

	public static Geometry object_to_geometry(MGMapApplet map, MGMapObject obj)
			throws TransformException {
		MGCollection vertices = (MGCollection) (map
				.createObject("MGCollection"));
		MGCollection numberOfVertices = (MGCollection) (map
				.createObject("MGCollection"));
		obj.getVertices(vertices, numberOfVertices);

		if (vertices.size() == 0 || numberOfVertices.size() == 0)
			return null;
		
		Coordinate[] coords = convert_vertices(vertices);

		assert numberOfVertices.size() >= 1;

		String type = obj.getType();
		if (type == "Text" || type == "Point") {
			assert numberOfVertices.size() == 1;
			assert (Integer) numberOfVertices.item(0) == 1;
			return factory.createPoint(coords[0]);

		} else if (type == "Polyline") {
			int numberOfVertices_size = numberOfVertices.size();

			if (numberOfVertices_size == 1)
				return factory.createLineString(coords);
			else {
				LineString[] line_strings = new LineString[numberOfVertices_size];

				int start = 0;
				for (int i = 0; i < numberOfVertices_size; i++) {
					Integer num = (Integer) numberOfVertices.item(i);
					assert num > 0;
					Coordinate[] segment = new Coordinate[num];
					for (int j = 0; j < num; j++)
						segment[j] = coords[start + j];
					line_strings[i] = factory.createLineString(segment);
					start = start + num;
				}

				return factory.createMultiLineString(line_strings);
			}
		} else if (type == "Polygon") {
			Polygon p;
			int numberOfVertices_size = numberOfVertices.size();

			if (numberOfVertices_size == 1) {
				CoordinateList list = new CoordinateList(coords, false);
				list.closeRing();

				if (list.size() < 4)
					return null;

				p = factory.createPolygon(factory.createLinearRing(list
						.toCoordinateArray()), null);
			} else {
				int num = (Integer) numberOfVertices.item(0);
				Coordinate[] shellCoords = new Coordinate[num];
				for (int i = 0; i < num; i++)
					shellCoords[i] = coords[i];

				CoordinateList list = new CoordinateList(coords, false);
				list.closeRing();

				if (list.size() < 4)
					return null;

				LinearRing shell = factory.createLinearRing(list
						.toCoordinateArray());

				LinearRing[] holes = new LinearRing[numberOfVertices_size - 1];
				int start = num;
				for (int i = 1; i < numberOfVertices_size; i++) {
					num = (Integer) numberOfVertices.item(i);
					list = new CoordinateList();

					for (int j = 0; j < num; j++)
						list.add(coords[start + j], false);

					list.closeRing();
					if (list.size() >= 4)
						holes[i - 1] = factory.createLinearRing(list
								.toCoordinateArray());
					else
						holes[i - 1] = null;

					start = start + num;
				}

				p = factory.createPolygon(shell, holes);
			}

			return p.buffer(0);
		} else
			return null;
	}

}
