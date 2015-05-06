package stock.meta;

import java.io.File;

import static java.lang.String.format;

/**
 * Created by twer on 15-5-2.
 */
public class WindIn {
    private static File logfile=new File("/Users/twer/project/zboDoWindIn/src/app-resources/windin-finished.log");
    private static File sqfile=new File("/Users/twer/project/zboDoWindIn/src/app-resources/windin-sq-finished.html");
    private static String source_base="http://www.windin.com/home/stock/stock-mh/%s.%s.shtml";
    String image_base="http://chart.windin.com/HQchart/kline/10000000/d/%s.%s.jpg";
    private static String basic_base="http://www.windin.com/home/stock/html/%s.%s.shtml";
    private static String sina_day_img_base="http://image2.sinajs.cn/newchart/daily/n/%s.gif";
    private static String sina_week_img_base="http://image2.sinajs.cn/newchart/weekly/n/%s.gif";
    public static File getLogfile() {
        return logfile;
    }

    public static String getSource_base() {
        return source_base;
    }

    public static String getBasic_base() {
        return basic_base;
    }

    public static String getSina_day_img(String code) {
        return format(sina_day_img_base,code);
    }

    public static String getSina_week_img(String code) {
        return format(sina_week_img_base,code);
    }

    public static File getSqfile() {
        return sqfile;
    }
}
