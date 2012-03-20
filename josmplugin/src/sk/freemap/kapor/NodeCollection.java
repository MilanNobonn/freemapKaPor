package sk.freemap.kapor;

import java.util.TreeMap;

import org.opengis.referencing.operation.TransformException;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;

import com.vividsolutions.jts.geom.Coordinate;

class NodeCollection {
	public TreeMap<Coordinate, Node> nodes;
	public DataSet dataset;

	public NodeCollection(DataSet d) {
		dataset = d;
		nodes = new TreeMap<Coordinate, Node>();
	}

	private static Coordinate dst = new Coordinate();
	private static Coordinate src = new Coordinate();

	public Node getNode(double x, double y) throws TransformException {
		Coordinate c = new Coordinate(x, y);
		if (nodes.containsKey(c)) {
			return nodes.get(c);
		} else {
			src.x = -y;
			src.y = -x;
			Projection.convert_s_jtsk_to_etrs89(src, dst);

			Node n = new Node();
			n.setCoor(new LatLon(dst.y, dst.x));
			nodes.put(c, n);

			dataset.addPrimitive(n);

			return n;
		}
	}
}