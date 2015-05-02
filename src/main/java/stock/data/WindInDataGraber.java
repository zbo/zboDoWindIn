package stock.data;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import stock.meta.Stock;
import stock.name.AbstractMarket;

import java.io.IOException;
import java.util.List;

/**
 * Created by twer on 15-5-2.
 */
public class WindInDataGraber {
    AbstractMarket market;
    private org.openqa.selenium.WebDriver WebDriver;
    @Inject
    Gson gson;

    public void grab(String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        market = (AbstractMarket) (Class.forName(classname).newInstance());
        List<String> lines = FileUtils.readLines(market.getFile());
        for(String line : lines){
            Stock stock = gson.fromJson(line, Stock.class);
            System.out.println(stock.getName());
        }
    }
}
