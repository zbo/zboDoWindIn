package stock.name;

import com.google.gson.Gson;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import stock.meta.AbstractMarket;
import stock.meta.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XueQiuNameExtractor implements Module {

    @Inject
    Gson gson;

    AbstractMarket market;

    private WebDriver WebDriver;

    public List<Stock> extract(String classname) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        market=(AbstractMarket)(Class.forName(classname).newInstance());
        FileUtils.deleteQuietly(market.getFile());
        List<Stock> result = new ArrayList<>();
        extractSingleType(result,market.getTotal(),market.getURL());
        return result;
    }

    private void extractSingleType(List<Stock> result,int total,String url) {
        try {
            WebDriver = new FirefoxDriver();
            int currentPage = 1;
            while (currentPage <= total) {
                result.addAll(extractSinglePage(String.format(url, currentPage)));
                currentPage++;
            }
            WebDriver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Stock> extractSinglePage(String url) throws IOException {
        List<Stock> result = new ArrayList<>();
        WebDriver.get(url);
        List<WebElement> trs = new ArrayList<>();
        do {
            try {
                trs = WebDriver.findElements(By.xpath("/html/body/div[3]/div/div[2]/div/div[3]/div[3]/div[1]/table/tbody/tr"));
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (trs.size() == 0);
        for (WebElement tr : trs) {
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                String name = tds.get(1).findElement(By.tagName("a")).getAttribute("innerHTML");
                String code = tds.get(0).findElement(By.tagName("a")).getAttribute("innerHTML");
                String volumn = tds.get(7).findElement(By.tagName("span")).getAttribute("innerHTML");
                String pe = tds.get(8).getAttribute("innerHTML");
                String range52 = tds.get(6).getAttribute("innerHTML");
                Stock stock = new Stock();
                stock.setName(name);
                stock.setCode(code);
                stock.setPe(pe);
                stock.setVolumn(volumn);
                stock.setRange52(range52);
                String json = gson.toJson(stock);
                System.out.println(json);
                FileUtils.write(market.getFile(), json + "\n", "UTF-8", true);
                result.add(stock);
        }
        return result;
    }

    @Override
    public void configure(Binder binder) {

    }
}
