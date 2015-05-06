package stock.data;

import org.junit.Before;
import org.junit.Test;
import stock.meta.MatchedStock;
import stock.meta.Stock;

import java.io.IOException;

public class WindInDataRecorderTest{
    @Before
    public void setup(){
        this.recorder=new WindInDataRecorder();
    }
    private WindInDataRecorder recorder;
    @Test
    public void should_record_stock_test() throws IOException {
        MatchedStock matchedStock = new MatchedStock();
        Stock stock = new Stock();
        stock.setName("浔兴股份");
        stock.setVolumn("1012312312");
        stock.setCode("SZ002098");
        stock.setPe("99");
        matchedStock.setSotck(stock);
        matchedStock.setQfiiThisQ("(2105-03-31) 社保基金 2 1000 2.8");
        matchedStock.setSbThisQ("(2105-03-31) QFII 2 1000 2.8");
        matchedStock.setSbLastQ("(2105-03-31) 社保基金 1 5000 1.5");
        matchedStock.setSbLastQ("(2105-03-31) QFII 1 800 1.8");
        matchedStock.setImageWeek("http://sina/image/wwwwwxxxxx");
        matchedStock.setImageDay("http://sina/image/dddddxxxxx");
        recorder.record(matchedStock);
    }

}