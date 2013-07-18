package sk.freemap;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.geom.prep.PreparedPolygon;

public class Kraje {

	public static LinkedList<PreparedPolygon> load() throws IOException {

		URL krajeShp = Kraje.class
				.getResource("kraje/hranice_krajov_simpl.shp");
		FileDataStore store = FileDataStoreFinder.getDataStore(krajeShp);
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = store
				.getFeatureSource();
		FeatureCollection<SimpleFeatureType, SimpleFeature> fC = featureSource
				.getFeatures();

		FeatureIterator<SimpleFeature> iter = fC.features();

		LinkedList<PreparedPolygon> list = new LinkedList<PreparedPolygon>();

		try {
			while (iter.hasNext()) {
				Feature f = iter.next();
				GeometryAttribute geomAttr = f.getDefaultGeometryProperty();
				list.add(new PreparedPolygon((Polygonal) geomAttr.getValue()));
			}
		} finally {
			iter.close();
		}

		return list;
	}
}
