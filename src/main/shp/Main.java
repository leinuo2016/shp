package shp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by leinuo on 2020/6/22 下午5:22
 * <p>
 * qq:1321404703 https://github.com/leinuo2016
 */
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {

        //字体
        List<String> stringList = new ArrayList<>();
        stringList.add("121");
        stringList.add("1测试21");
        stringList.add("1修奥名21");
        //stringList.forEach(s -> System.out.println("输出 = " + s));
        int i=1;int j=i++;
        if((j>++j)&&(i++==j)){j+=i;}
       // System.out.println(i);
       // String str="%A5%9F%5ElX%5ElX%5Er%A9%95%5Er%5E%5Eq%5D%5E%7Bg%5Eo%9C%D1%D2%CA%CE%D4%CA%86%5Bf%92%C4%C4%C4%87%5Bf%96%CE%CD";
        //String s2 = new String(str.getBytes("utf-8"),"GBK");
       // System.out.println(s2);

        Main test = new Main();
        String str = "This is a 中文的 String!";
        System.out.println("str: " + str);
        String gbk = test.toGBK(str);
        System.out.println("转换成GBK码: " + gbk);
        System.out.println();
        String ascii = test.toASCII(str);
        System.out.println("转换成US-ASCII码: " + ascii);
        gbk = test.changeCharset(ascii,Main.US_ASCII, Main.GBK);
        System.out.println("再把ASCII码的字符串转换成GBK码: " + gbk);
        System.out.println();
        String iso88591 = test.toISO_8859_1(str);
        System.out.println("转换成ISO-8859-1码: " + iso88591);
        gbk = test.changeCharset(iso88591,Main.ISO_8859_1, Main.GBK);
        System.out.println("再把ISO-8859-1码的字符串转换成GBK码: " + gbk);
        System.out.println();
        String utf8 = test.toUTF_8(str);
        System.out.println("转换成UTF-8码: " + utf8);
        gbk = test.changeCharset(utf8,Main.UTF_8, Main.GBK);
        System.out.println("再把UTF-8码的字符串转换成GBK码: " + gbk);
        System.out.println();
        String utf16be = test.toUTF_16BE(str);
        System.out.println("转换成UTF-16BE码:" + utf16be);
        gbk = test.changeCharset(utf16be,Main.UTF_16BE, Main.GBK);
        System.out.println("再把UTF-16BE码的字符串转换成GBK码: " + gbk);
        System.out.println();
        String utf16le = test.toUTF_16LE(str);
        System.out.println("转换成UTF-16LE码:" + utf16le);
        gbk = test.changeCharset(utf16le,Main.UTF_16LE, Main.GBK);
        System.out.println("再把UTF-16LE码的字符串转换成GBK码: " + gbk);
        System.out.println();
        String utf16 = test.toUTF_16(str);
        System.out.println("转换成UTF-16码:" + utf16);
        gbk = test.changeCharset(utf16,Main.UTF_16LE, Main.GBK);
        System.out.println("再把UTF-16码的字符串转换成GBK码: " + gbk);
        String s = new String("中文".getBytes("UTF-8"),"UTF-8");
        System.out.println(s);
    }
    /** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
    public static final String US_ASCII = "US-ASCII";

    /** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /** 8 位 UCS 转换格式 */
    public static final String UTF_8 = "UTF-8";

    /** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
    public static final String UTF_16BE = "UTF-16BE";

    /** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
    public static final String UTF_16LE = "UTF-16LE";

    /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
    public static final String UTF_16 = "UTF-16";

    /** 中文超大字符集 */
    public static final String GBK = "GBK";

    /**
     * 将字符编码转换成US-ASCII码
     */
    public String toASCII(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, US_ASCII);
    }
    /**
     * 将字符编码转换成ISO-8859-1码
     */
    public String toISO_8859_1(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, ISO_8859_1);
    }
    /**
     * 将字符编码转换成UTF-8码
     */
    public String toUTF_8(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_8);
    }
    /**
     * 将字符编码转换成UTF-16BE码
     */
    public String toUTF_16BE(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16BE);
    }
    /**
     * 将字符编码转换成UTF-16LE码
     */
    public String toUTF_16LE(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16LE);
    }
    /**
     * 将字符编码转换成UTF-16码
     */
    public String toUTF_16(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16);
    }
    /**
     * 将字符编码转换成GBK码
     */
    public String toGBK(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, GBK);
    }

    /**
     * 字符串编码转换的实现方法
     * @param str  待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    /**
     * 字符串编码转换的实现方法
     * @param str  待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
}
