package shp;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;

import java.text.NumberFormat;

/**
 * Create by leinuo on 2021/9/9
 */
public class RandNum {

    public static void main(String[] args) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(3);
        for (int i = 0; i <287 ; i++) {
            double v = RandomUtil.randomDouble(-0.02, 0.02);
            System.out.println(numberFormat.format(v));
        }

    }
}
