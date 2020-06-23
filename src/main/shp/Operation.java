package shp;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    private GeometryFactory geometryFactory = new GeometryFactory();
    /**
     * create a Point
     *
     * @param x
     * @param y
     * @return
     */
    public Coordinate point(double x, double y) {
        return new Coordinate(x, y);
    }
    /**
     * create a line
     *
     * @return
     */
    public LineString createLine(List<Coordinate> points) {
        Coordinate[] coords = (Coordinate[]) points.toArray(new Coordinate[points.size()]);
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }
    /**
     * 返回a指定距离内的多边形和多多边形
     *
     * @param a
     * @param distance
     * @return
     */
    public Geometry bufferGeo(Geometry a, double distance) {
        return a.buffer(distance);
    }
    /**
     * 返回(A)与(B)中距离最近的两个点的距离
     *
     * @param a
     * @param b
     * @return
     */
    public double distanceGeo(Geometry a, Geometry b) {
        return a.distance(b);
    }
    /**
     * 两个几何对象的交集
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry intersectionGeo(Geometry a, Geometry b) {
        return a.intersection(b);
    }
    /**
     * 几何对象合并
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry unionGeo(Geometry a, Geometry b) {
        return a.union(b);
    }
    /**
     * 在A几何对象中有的，但是B几何对象中没有
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry differenceGeo(Geometry a, Geometry b) {
        return a.difference(b);
    }
    public static void main(String[] args) {
        Operation op = new Operation();
        //创建一条线
        List<Coordinate> points1 = new ArrayList<>();
        points1.add(op.point(0, 0));
        points1.add(op.point(1, 3));
        points1.add(op.point(2, 3));
        LineString line1 = op.createLine(points1);
        //创建第二条线
        List<Coordinate> points2 = new ArrayList<Coordinate>();
        points2.add(op.point(3, 0));
        points2.add(op.point(3, 3));
        points2.add(op.point(5, 6));
        LineString line2 = op.createLine(points2);
        System.out.println(op.distanceGeo(line1, line2));//out 1.0
        System.out.println(op.intersectionGeo(line1, line2));//out GEOMETRYCOLLECTION EMPTY
        System.out.println(op.unionGeo(line1, line2)); //out MULTILINESTRING ((0 0, 1 3, 2 3), (3 0, 3 3, 5 6))
        System.out.println(op.differenceGeo(line1, line2));//out LINESTRING (0 0, 1 3, 2 3)
    }
}