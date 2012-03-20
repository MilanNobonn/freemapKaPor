package sk.freemap.kapor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.opengis.referencing.operation.TransformException;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;

import com.autodesk.mgjava.MGCollection;
import com.autodesk.mgjava.MGMapApplet;
import com.autodesk.mgjava.MGMapLayer;
import com.autodesk.mgjava.MGMapObject;
import com.autodesk.mgjava.MGPoint;
import com.vividsolutions.jts.geom.Coordinate;

public class ExportAll {
	public static DataSet exportAll(MGMapApplet map) throws TransformException {
		DataSet dataset = new DataSet();

		NodeCollection allNodes = new NodeCollection(dataset);
		
		final boolean exportNames = Main.pref.getBoolean(ConfigKeys.FREEMAPKAPOR_EXPORT_NAME, false);
		

		Vector<MGMapLayer> layers = map.getMapLayers();
		for (Enumeration<MGMapLayer> layers_enum = layers.elements(); layers_enum
				.hasMoreElements();) {

			MGMapLayer layer = layers_enum.nextElement();
			if (layer.isVisible()) {

				MGCollection vertices = (MGCollection) (map
						.createObject("MGCollection"));
				MGCollection numberOfVertices = (MGCollection) (map
						.createObject("MGCollection"));

				Vector<MGMapObject> objs = layer.getMapObjects();

				Coordinate src = new Coordinate();

				for (Enumeration<MGMapObject> objs_enum = objs.elements(); objs_enum
						.hasMoreElements();) {
					MGMapObject obj = objs_enum.nextElement();

					obj.getVertices(vertices, numberOfVertices);

					String obj_name = null;
					if (exportNames)
						obj_name = obj.getName();
					
					int vertices_size = vertices.size();
					ArrayList<Node> nodes = new ArrayList<Node>(vertices_size);

					for (int i = 0; i < vertices_size; i++) {
						MGPoint p = (MGPoint) vertices.item(i);
						nodes.add(allNodes.getNode(p.getX(), p.getY()));
					}

					int numberOfVertices_size = numberOfVertices.size();

					int start = 0;
					for (int i = 0; i < numberOfVertices_size; i++) {
						Integer num = (Integer) numberOfVertices.item(i);

						List<Node> sub = nodes.subList(start, start + num);
						Node prev = null;

						LinkedList<Node> segment = new LinkedList<Node>();

						for (Iterator<Node> iter = sub.iterator(); iter
								.hasNext();) {
							Node node = iter.next();
							if (prev == null)
								segment.add(node);
							else if (prev != node)
								segment.add(node);
							prev = node;
						}

						if (segment.size() > 1) {
							Way way = new Way();
							way.setNodes(segment);
							way.put("source", "kapor2");

							if (obj_name != null)
								way.put("name", obj_name);

							dataset.addPrimitive(way);
						} else 
							if (segment.size() == 1 && obj_name != null)
							{
								final Node n = segment.getFirst(); 
								n.put("source", "kapor2");
								n.put("name", obj_name);
							}

						start = start + num;
					}
				}
			}
		}
		return dataset;
	}
}
