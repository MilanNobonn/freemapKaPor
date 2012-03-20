package sk.freemap.kapor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

import flanagan.interpolation.BiCubicSpline;

public class Projection {
	protected static CoordinateReferenceSystem S_JTSK_03;
	protected static CoordinateReferenceSystem WGS84;

	public static MathTransform s_jtsk_03_to_etrs89;
	public static MathTransform etrs89_to_s_jtsk_03;

	protected static BiCubicSpline s_jtsk_diffX_grid;
	protected static BiCubicSpline s_jtsk_diffY_grid;

	protected static TreeSet<Double> xset;
	protected static TreeSet<Double> yset;

	public static void initCRS() throws IOException, FactoryException {
		WGS84 = loadCRS("/projdata/wgs84.prj");
		S_JTSK_03 = loadCRS("/projdata/s_jtsk_03.prj");
		s_jtsk_03_to_etrs89 = CRS.findMathTransform(S_JTSK_03, WGS84, true);
		etrs89_to_s_jtsk_03 = CRS.findMathTransform(WGS84, S_JTSK_03, true);
	}

	public static CoordinateReferenceSystem loadCRS(String resouceName)
			throws IOException, FactoryException {
		StringBuilder sb = new StringBuilder();
		String line;

		InputStream in = Projection.class.getResourceAsStream(resouceName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		while ((line = reader.readLine()) != null)
			sb.append(line);

		return CRS.parseWKT(sb.toString());
	}

	private static Coordinate temp = new Coordinate(0, 0);

	public static void convert_s_jtsk_to_s_jtsk_03(Coordinate s_jtsk,
			Coordinate s_jtsk_03) {
		boolean negative = s_jtsk.x < 0;

		double x = Math.abs(s_jtsk.x), y = Math.abs(s_jtsk.y);

		double ix = x, iy = y;

		// Limit ix do values in range xset
		if (ix < xset.first())
			ix = xset.first();

		if (iy < yset.first())
			iy = yset.first();

		// Limit ix do values in range yset
		if (ix > xset.last())
			ix = xset.last();

		if (iy > yset.last())
			iy = yset.last();

		double x03 = x + s_jtsk_diffX_grid.interpolate(ix, iy);
		double y03 = y + s_jtsk_diffY_grid.interpolate(ix, iy);

		if (negative) {
			x03 = -x03;
			y03 = -y03;
		}

		s_jtsk_03.x = x03;
		s_jtsk_03.y = y03;
	}

	public static void convert_s_jtsk_to_etrs89(Coordinate s_jtsk,
			Coordinate etrs89) throws TransformException {

		convert_s_jtsk_to_s_jtsk_03(s_jtsk, temp);

		JTS.transform(temp, etrs89, Projection.s_jtsk_03_to_etrs89);

	}

	public static void initGrid() throws IOException {
		class GridEntry {
			public double S_JTSK_X;
			public double S_JTSK_Y;

			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (!(obj instanceof GridEntry))
					return false;
				GridEntry b = (GridEntry) obj;
				return (this.S_JTSK_X == b.S_JTSK_X)
						&& (this.S_JTSK_Y == b.S_JTSK_Y);
			}

			public int hashCode() {
				return new Double(S_JTSK_X).hashCode()
						^ new Double(S_JTSK_Y).hashCode();
			}

			public GridEntry(double a_S_JTSK_X, double a_S_JTSK_Y) {
				S_JTSK_X = a_S_JTSK_X;
				S_JTSK_Y = a_S_JTSK_Y;
			}
		}

		InputStream in = Projection.class
				.getResourceAsStream("/projdata/10km.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));

		xset = new TreeSet<Double>();
		yset = new TreeSet<Double>();

		Hashtable<GridEntry, Double> diffX = new Hashtable<GridEntry, Double>();
		Hashtable<GridEntry, Double> diffY = new Hashtable<GridEntry, Double>();

		{
			Pattern whitespace = Pattern.compile("\\s+");
			String line;
			while ((line = reader.readLine()) != null) {
				int commentindex = line.indexOf("#");
				if (commentindex >= 0)
					line = line.substring(0, commentindex);
				String[] elements = whitespace.split(line);
				if (elements.length == 4) {
					double S_JTSK_X = Double.valueOf(elements[0]);
					double S_JTSK_Y = Double.valueOf(elements[1]);
					double S_JTSK_X_03 = Double.valueOf(elements[2]);
					double S_JTSK_Y_03 = Double.valueOf(elements[3]);

					xset.add(S_JTSK_X);
					yset.add(S_JTSK_Y);

					assert Math.abs(S_JTSK_X_03 - S_JTSK_X) < 2;
					assert Math.abs(S_JTSK_Y_03 - S_JTSK_Y) < 2;

					GridEntry ge = new GridEntry(S_JTSK_X, S_JTSK_Y);
					diffX.put(ge, S_JTSK_X_03 - S_JTSK_X);
					diffY.put(ge, S_JTSK_Y_03 - S_JTSK_Y);

					assert diffX.contains(ge);
					assert diffY.contains(ge);
				}
			}
		}

		int q;
		double[] xarray = new double[xset.size()];
		q = 0;
		for (Iterator<Double> iter = xset.iterator(); iter.hasNext(); q++)
			xarray[q] = iter.next();

		double[] yarray = new double[yset.size()];
		q = 0;
		for (Iterator<Double> iter = yset.iterator(); iter.hasNext(); q++)
			yarray[q] = iter.next();

		double[][] diffXarray = new double[xarray.length][yarray.length];
		double[][] diffYarray = new double[xarray.length][yarray.length];

		GridEntry ge = new GridEntry(0, 0);

		for (int i = 0; i < xarray.length; i++)
			for (int j = 0; j < yarray.length; j++) {
				ge.S_JTSK_X = xarray[i];
				ge.S_JTSK_Y = yarray[j];
				diffXarray[i][j] = diffX.get(ge);
				diffYarray[i][j] = diffY.get(ge);
			}

		s_jtsk_diffX_grid = new BiCubicSpline(xarray, yarray, diffXarray);
		s_jtsk_diffY_grid = new BiCubicSpline(xarray, yarray, diffYarray);
	}
}
