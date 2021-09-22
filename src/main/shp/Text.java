package shp;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Create by leinuo on 2020/9/16
 * qq:1321404703 https://github.com/leinuo2016
 */
public class Text {


    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public enum Type{
        Type1,
        Type2;
    }

    public static void main(String[] args) {

        String s = "003020007100302000710030200071";

        String s1 = "0030200071";

        System.out.println(s.length()+","+s1.length());

        System.out.println(s.substring(0,10));

        System.out.println( Type.valueOf("Type1"));

        //System.out.println( Type.valueOf("Typ1"));
        //dateTest();
        numFloatTest();
        //dateTest2();
    }


    public static void numFloatTest() {
        float x = 7.12f;
        double fd = 0.1;

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);

        for(int i=10;i>0;i--){
            float v = numFloat(x, fd);
            System.out.println(v);
            System.out.println(nf.format(v));
        }
    }

    /**
     * 数字浮动
     * @param x 浮动基数
     * @param fd　浮动比例
     * @return
     */
    public static float numFloat(float x,double fd) {
        Double y =  (Math.random() * (x + (x >= 0 ? 1 : -1)) * fd) * (Math.random() > 0.5 ? 1 : -1);
        return y.floatValue()+x;
    }

    public static void dateTest(){
        LocalDate today = LocalDate.now(); // -> 2014-12-24// 根据年月日取日期，12月就是12：
        System.out.println(today.getDayOfMonth());
        LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        System.out.println(lastDayOfThisMonth.getDayOfMonth());


        LocalDate crischristmas = LocalDate.of(2014, 12, 25); // -> 2014-12-25// 根据字符串取：
        LocalDate endOfFeb = LocalDate.parse("2014-02-28"); // 严格按照ISO yyyy-MM-dd验证，02写成2都不行，当然也有一个重载方法允许自己定义格式
        //LocalDate.parse("2014-02-29"); // 无效日期无法通过：DateTimeParseException: Invalid date


        // 取本月第1天：
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth()); // 2014-12-01// 取本月第2天：
        LocalDate secondDayOfThisMonth = today.withDayOfMonth(2); // 2014-12-02// 取本月最后一天，再也不用计算是28，29，30还是31：
        //LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth()); // 2014-12-31// 取下一天：
        LocalDate firstDayOf2015 = lastDayOfThisMonth.plusDays(1); // 变成了2015-01-01// 取2015年1月第一个周一，这个计算用Calendar要死掉很多脑细胞：
        LocalDate firstMondayOf2015 = LocalDate.parse("2015-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)); // 2015-01-05
    }

    public static void dateTest2(){
        String date="2021-05-14";
        LocalDate parse = LocalDate.parse(date, dateTimeFormatter);
        //获取本月第一天
        LocalDate firstDayOfThisMonth = parse.with(TemporalAdjusters.firstDayOfMonth());

        //获取本月最后一天
        LocalDate lastDayOfThisMonth = parse.with(TemporalAdjusters.lastDayOfMonth());

        int dayOfMonth = lastDayOfThisMonth.getDayOfMonth();

        Instant startInstant = firstDayOfThisMonth.atTime(0,0,0).atZone(ZoneId.systemDefault()).toInstant();

        Instant endInstant = firstDayOfThisMonth.atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant();

        // 每个rtu按天统计到现在
        for(int d=0;d<dayOfMonth;d++){
            LocalDate localDate = firstDayOfThisMonth.plusDays(d);
            if(localDate.isAfter(LocalDate.now())){
                System.out.println("日期超过当前");
                break;
            }
            String format = dateTimeFormatter.format(localDate);
            System.out.println(format);
        }
    }

}
