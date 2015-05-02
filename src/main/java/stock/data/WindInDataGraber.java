package stock.data;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import stock.meta.AbstractMarket;
import stock.meta.Stock;
import stock.meta.WindIn;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by twer on 15-5-2.
 */
public class WindInDataGraber {
    AbstractMarket market;
    private org.openqa.selenium.WebDriver WebDriver;
    @Inject
    Gson gson;
    @Inject
    WindInDataRecorder recorder;

    private Hashtable<String,Stock> history=new Hashtable<>();
    WebDriver webDriver;

    public void grab(String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        market = (AbstractMarket) (Class.forName(classname).newInstance());
        webDriver = new FirefoxDriver();
        List<String> lines = FileUtils.readLines(market.getFile());
        buildHistoryHash();
        for(String line : lines){
            Stock stock = gson.fromJson(line, Stock.class);
            if(alreadyUpdated(stock)){
                System.out.println(stock.getName()+" already processed. so skip.");
            }
            else{
                if(matchFromWeb(stock)){
                    List<String> recordLines = generateRecord(stock);
                    this.recorder.record(recordLines);
                }
            }
        }
        webDriver.close();
    }

    private List<String> generateRecord(Stock stock) {
        return null;
    }

    private boolean matchFromWeb(Stock stock) {
        return false;
    }

    private void buildHistoryHash() throws IOException {
        List<String> lines = FileUtils.readLines(WindIn.getLogfile());
        for(String line : lines){
            Stock stock = gson.fromJson(line, Stock.class);
            history.put(stock.getCode(),stock);
        }
    }

    private boolean alreadyUpdated(Stock stock) {
        if(history.get(stock.getCode())!=null)
            return true;
        return false;
    }
}
