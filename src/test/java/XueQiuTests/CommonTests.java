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
    }
}
