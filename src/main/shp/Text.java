package shp;

/**
 * Create by leinuo on 2020/9/16
 * qq:1321404703 https://github.com/leinuo2016
 */
public class Text {


    public enum Type{
        Type1,
        Type2;
    }

    public static void main(String[] args) {
        Text text = new Text();
        System.out.println( Type.valueOf("type1"));

        System.out.println( Type.valueOf("typ1"));

    }
}
