package shp;

import com.vividsolutions.jts.algorithm.Angle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create by leinuo on 2021/3/18
 *
 * 根据相邻点坐标形成线段的斜率过滤点坐标
 */
public class PointSelect {



    public static void main(String[] args) {

        //给定一条线，按斜率变化抽稀

        List<Point> line = new ArrayList<>();
        line.add(new Point(1,1));
        // 该点过滤
        line.add(new Point(2,1));
        line.add(new Point(3,1));
        line.add(new Point(4,2));
        line.add(new Point(5,3));
        // 该点过滤
        line.add(new Point(6,3));
        line.add(new Point(7,3));
        line.add(new Point(8,4));
        // 该点过滤
        line.add(new Point(9,6));
        line.add(new Point(10,8));
        line.add(new Point(11,10));

        line.add(new Point(12,12));


        List<Point> points = thinningBySlop(line);

        System.out.println(line.size()+","+points.size());

        //A(0.5,1) B(1.5,2) C(2,2.5) D(3,4.7) F(5,6.2)
        int degree = getDegree(2, 1, 1, 1, 3, 1);
        System.out.println("角度："+degree);

        degree = getDegree(2, 1, 1, 1, 3, 1);
        System.out.println("角度："+degree);

        degree = getDegree(2, 1, 1, 1, 3, 1);
        System.out.println("角度："+degree);

        // C
        degree = getDegree(2, 2.5, 1.5, 2, 3, 4.7);
        System.out.println("C角度："+degree);

        // D
        degree = getDegree(3, 4.7, 2, 2.5, 5, 6.2);
        System.out.println("D角度："+degree);


        //BC
        double slope = getSlope(1.5, 2, 2, 2.5);
        System.out.println("BC斜率："+slope);

        //CD
        slope = getSlope(2, 2.5, 3, 4.7);
        System.out.println("CD斜率："+slope);

        //DF
        slope = getSlope(5, 6.2, 3, 4.7);
        System.out.println("DF斜率："+slope);

    }

    /**
     * 按斜率变化抽稀
     * @param line
     * @return
     */
    private static List<Point> thinningBySlop(List<Point> line) {
        if(Objects.isNull(line) || line.isEmpty() || line.size() == 1){
            return line;
        }
        List<Point> points = new ArrayList<>();
        Point point;
        double slop = 0;
        int size = line.size();
        for (int i = 0; i < size; i++) {
            point = line.get(i);

            if(i == 0){
                points.add(point);
                continue;
            }

            if(i+1<size){
                slop = getSlope(point.getX(), point.getY(), line.get(i+1).getX(),  line.get(i+1).getY());
            }
            if(i!=0){
                if(slop!=getSlope(point.getX(), point.getY(), line.get(i-1).getX(),  line.get(i-1).getY())){
                    points.add(point);
                };
            }
        }
        return points;
    }

    //　斜率
    public static double getSlope(double point0X, double point0Y, double point1X, double point1Y){
        double v = Math.abs(point1Y-point0Y)/Math.abs(point0X-point1X);
        return v;
    }

    public static int getDegree(double vertexPointX, double vertexPointY, double point0X, double point0Y, double point1X, double point1Y) {
        //向量的点乘
        double vector = (point0X - vertexPointX) * (point1X - vertexPointX) + (point0Y - vertexPointY) * (point1Y - vertexPointY);
        //向量的模乘
        double sqrt = Math.sqrt(
                (Math.abs((point0X - vertexPointX) * (point0X - vertexPointX)) + Math.abs((point0Y - vertexPointY) * (point0Y - vertexPointY)))
                        * (Math.abs((point1X - vertexPointX) * (point1X - vertexPointX)) + Math.abs((point1Y - vertexPointY) * (point1Y - vertexPointY)))
        );
        //反余弦计算弧度
        double radian = Math.acos(vector / sqrt);
        //弧度转角度制
        return (int) (180 * radian / Math.PI);
    }


     static class  Point{
        private double x;

        private double y;

         public Point(double x, double y) {
             this.x = x;
             this.y = y;
         }

         public double getX() {
             return x;
         }

         public void setX(double x) {
             this.x = x;
         }

         public double getY() {
             return y;
         }

         public void setY(double y) {
             this.y = y;
         }
     }

}
