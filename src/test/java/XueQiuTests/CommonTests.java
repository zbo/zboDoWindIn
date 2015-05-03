package XueQiuTests;

import org.junit.Test;

/**
 * Created by twer on 15-5-1.
 */
public class CommonTests {
    @Test
    public void StringFormateTest(){
        String URL="http://xueqiu.com/hq#exchange=CN&page=%d";
        String format = String.format(URL, 0);
        System.out.println(format);
//        String URLBase="http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=%s&order=asc&orderby=symbol&page=%s";
//        String cyb = String.format(URLBase, "cyb");
//        System.out.println(cyb);
        //java.util.MissingFormatArgumentException: Format specifier 's'
    }
}
