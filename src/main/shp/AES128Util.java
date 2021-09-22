package shp;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Create by leinuo on 2021/9/16
 */
public class AES128Util {
    private static final  String SECRET_KEY = "ZjZlMDg5NmI0YmE=";


    /**
     * 加密
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc)  {
        try {
            byte[] raw = SECRET_KEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            String encode = new BASE64Encoder().encode(encrypted);
            String s = bytesToHexString(encode.getBytes("utf-8"));
            return s;
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 解密
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc) {
        try {


            byte[] bytes = hexStringToByte(sSrc);
            sSrc =  new String(bytes, "utf-8");

            // 判断Key是否正确
            byte[] raw = SECRET_KEY.getBytes("utf-8");

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {

                return null;
            }
        } catch (Exception ex) {

            return null;
        }
    }


    /**
     * 字节数组转16进制字符
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */

        // 需要加密的字串
        String cSrc = System.currentTimeMillis()+"";
        System.out.println("加密后的字串s是："+cSrc);
        // 加密
        String enString = Encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = Decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
    }

}
