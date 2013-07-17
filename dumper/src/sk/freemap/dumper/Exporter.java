package sk.freemap.dumper;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

import com.autodesk.mgjava.MGCollection;
import com.autodesk.mgjava.MGMap;
import com.autodesk.mgjava.MGMapLayer;
import com.autodesk.mgjava.MGMapObject;
import com.autodesk.mgjava.MGPoint;

public class Exporter {
	public static void export(MGMap map, FileOutputStream filestream) throws IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(buffer);
		
		Vector<MGMapLayer> layers = map.getMapLayers();
		for (Enumeration<MGMapLayer> layers_enum = layers.elements(); layers_enum
				.hasMoreElements();) {
			MGMapLayer layer = layers_enum.nextElement();
			if (layer.isVisible()) {
				stream.println("===" + layer.getLayerType() + "|"
						+ layer.getName() + "|" + layer.getLegendLabel());

				MGCollection vertices = (MGCollection) (map
						.createObject("MGCollection"));
				MGCollection numberOfVertices = (MGCollection) (map
						.createObject("MGCollection"));

				Vector<MGMapObject> objs = layer.getMapObjects();

				for (Enumeration<MGMapObject> objs_enum = objs.elements(); objs_enum
						.hasMoreElements();) {
					MGMapObject obj = objs_enum.nextElement();

					stream.print(obj.getType() + "|" + obj.getKey() + "|"
							+ obj.getName() + "|");
					
					obj.getVertices(vertices, numberOfVertices);

					int numberOfVertices_size = numberOfVertices.size();
					for (int i = 0; i < numberOfVertices_size; i++) {
						if (i != 0)
							stream.print(";");
						stream.print(numberOfVertices.item(i));
					}

					stream.print("|");

					int vertices_size = vertices.size();
					for (int i = 0; i < vertices_size; i++) {
						if (i != 0)
							stream.print(";");
						MGPoint p = (MGPoint) vertices.item(i);
						stream.print(p.getX());
						stream.print(",");
						stream.print(p.getY());
					}
					stream.println();
				}
			}
		}

		filestream.write(buffer.toByteArray());
		filestream.flush();
	}
}
