package stock.data;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import market.xueqiu.AbstractMarket;
import stock.meta.Stock;
import stock.meta.WindIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by twer on 15-5-2.
 */
public class WindInDataGraber {
    AbstractMarket market;
    private org.openqa.selenium.WebDriver WebDriver;

    @Inject
    private Finance finance;
    @Inject
    Gson gson;
    @Inject
    WindInDataRecorder recorder;

    private Hashtable<String, Stock> history = new Hashtable<>();
    WebDriver webDriver;

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
                List<String> thisQuarterLines = matchThisQuarterFromWeb(stock);
                if (thisQuarterLines.size() > 0) {
                    this.recorder.record(thisQuarterLines);
                    List<String> lastQuarterLines = matchLastQuarterFromWeb(stock);
                    this.recorder.record(lastQuarterLines);
                    List<String> sinaImages = generateSinaImages(stock);
                    this.recorder.record(sinaImages);
                }
            }
        }
        webDriver.close();
    }

    private List<String> generateSinaImages(Stock stock) {
        return null;
    }

    private List<String> matchLastQuarterFromWeb(Stock stock) {
        return null;
    }

    private List<String> matchThisQuarterFromWeb(Stock stock) throws IOException {
        List<String> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        webDriver.get(String.format(WindIn.getSource_base(), stock.getCode().substring(2), stock.getCode().substring(0, 2)));
        String date=getDate();
        try {
            WebElement div = webDriver.findElement(By.id("MainHolder_Sumary1"));
            List<WebElement> trs = div.findElements(By.tagName("tr"));
            for (WebElement tr : trs) {
                StringBuilder builder = getStringLine(tr);
                temp.add(builder.toString() + "<br>");
                if (finance.IsSB(builder.toString()) || finance.IsQfii(builder.toString())) {
                    result.add(stock.getCode() + " " + stock.getName() + "<br>");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("error one" + stock.getName());
        }
        FileUtils.writeStringToFile(WindIn.getLogfile(), gson.toJson(stock) + "\n", "UTF-8", true);
        if (result.size() > 0) {
            for (String line : temp) {
                if (finance.IsQfii(line) || finance.IsSB(line))
                    result.add(date+" "+line);
            }
            result.add(Finance.spliter);
        }
        return result;
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
