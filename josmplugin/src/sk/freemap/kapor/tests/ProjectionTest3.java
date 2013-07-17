package sk.freemap.kapor.tests;

import java.io.IOException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

import sk.freemap.kapor.Projection;

public class ProjectionTest3 {
	/**
	 * @param args
	 * @throws FactoryException
	 * @throws IOException
	 * @throws TransformException 
	 */
	public static void main(String[] args) throws IOException, FactoryException, TransformException {
		Projection.initCRS();
		Projection.initGrid();
		
		Coordinate s_jtsk = new Coordinate(1218269, 181932);
		Coordinate s_jtsk_03 = new Coordinate();
		Projection.convert_s_jtsk_to_s_jtsk_03(s_jtsk, s_jtsk_03);
		System.out.println(s_jtsk_03.x);
		System.out.println(s_jtsk_03.y);
		
		assert Math.abs(s_jtsk_03.x - 1218268.472) < 0.1;
		assert Math.abs(s_jtsk_03.y - 181931.265) < 0.1;
	}
}
