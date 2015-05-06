package stock.data;

import com.google.gson.Gson;
import com.google.inject.Inject;
import market.xueqiu.AbstractMarket;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import stock.meta.MatchedStock;
import stock.meta.Stock;
import stock.meta.WindIn;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by twer on 15-5-2.
 */
public class WindInDataGraber {
    AbstractMarket market;
    @Inject
    Gson gson;
    @Inject
    WindInDataRecorder recorder;
    WebDriver webDriver;
    private org.openqa.selenium.WebDriver WebDriver;
    @Inject
    private Finance finance;
    private Hashtable<String, Stock> history = new Hashtable<>();
    private String contentFormat="%s - %s";

    public void grab(String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        market = (AbstractMarket) (Class.forName(classname).newInstance());
        webDriver = new FirefoxDriver();
        List<String> lines = FileUtils.readLines(market.getFile());
        buildHistoryHash();
        for (String line : lines) {
            Stock stock = gson.fromJson(line, Stock.class);
            if (alreadyUpdated(stock)) {
                System.out.println(stock.getName() + " already processed. so skip.");
            } else {
                MatchedStock matchedStock = new MatchedStock();
                matchThisQuarterFromWeb(stock, matchedStock);
                if (matchedStock.thisQmatched()) {
                    matchLastQuarterFromWeb(stock, matchedStock);
                    generateSinaImages(stock, matchedStock);
                    this.recorder.record(matchedStock);
                }
            }
        }
        webDriver.close();
    }

    private void generateSinaImages(Stock stock, MatchedStock matchedStock) {

    }

    private void matchLastQuarterFromWeb(Stock stock, MatchedStock matchedStock) {
        try {
            WebElement link2 = webDriver.findElement(By.id("report2"));
            link2.click();
            WebElement div2 = webDriver.findElement(By.id("MainHolder_Sumary2"));
            String date = getDate();
            List<WebElement> trs = div2.findElements(By.tagName("tr"));
            for (WebElement tr : trs) {
                String content = getStringLine(tr).toString();
                matchedStock.setSotck(stock);
                if (finance.IsSB(content)) {
                    matchedStock.setSbLastQ(format(contentFormat, date, content));
                } else if (finance.IsQfii(content)) {
                    matchedStock.setQfiiLastQ(format(contentFormat, date, content));
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("error one" + stock.getName());
        }
    }

    private void matchThisQuarterFromWeb(Stock stock, MatchedStock matchedStock) throws IOException {
        webDriver.get(format(WindIn.getSource_base(), stock.getCode().substring(2), stock.getCode().substring(0, 2)));
        String date = getDate();
        try {
            WebElement div = webDriver.findElement(By.id("MainHolder_Sumary1"));
            List<WebElement> trs = div.findElements(By.tagName("tr"));
            for (WebElement tr : trs) {
                String content = getStringLine(tr).toString();
                matchedStock.setSotck(stock);
                if (finance.IsSB(content)) {
                    matchedStock.setSbThisQ(format(contentFormat, date, content));
                } else if (finance.IsQfii(content)) {
                    matchedStock.setQfiiThisQ(format(contentFormat, date, content));
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("error one" + stock.getName());
        }
        FileUtils.writeStringToFile(WindIn.getLogfile(), gson.toJson(stock) + "\n", "UTF-8", true);
    }

    private String getDate() {
        WebElement span = webDriver.findElement(By.xpath("//*[@id=\"MainHolder1\"]/table[1]/tbody/tr[1]/td/span"));
        return span.getAttribute("innerHTML");
    }

    private StringBuilder getStringLine(WebElement tr) {
        StringBuilder line = new StringBuilder();
        for (WebElement td : tr.findElements(By.tagName("td"))) {
            String innerHtml = td.getAttribute("innerHTML");
            line.append(" " + innerHtml);
        }
        return line;
    }

    private void buildHistoryHash() throws IOException {
        List<String> lines = FileUtils.readLines(WindIn.getLogfile());
        for (String line : lines) {
            Stock stock = gson.fromJson(line, Stock.class);
            history.put(stock.getCode(), stock);
        }
    }

    private boolean alreadyUpdated(Stock stock) {
        if (history.get(stock.getCode()) != null)
            return true;
        return false;
    }
}
