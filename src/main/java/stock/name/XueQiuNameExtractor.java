package stock.name;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import stock.meta.Stock;

import java.util.ArrayList;
import java.util.List;

public class XueQiuNameExtractor {
    private static String URL = "http://xueqiu.com/hq#exchange=CN&page=%d";
    private static String output="/Users/twer/project/zboDoWindIn/src/app-resources/all-stock.json";
    private static Integer TOTAL_PAGES = 190;
    private WebDriver WebDriver;

    public List<Stock> extract() {
        WebDriver = new FirefoxDriver();
        int currentPage = 1;
        List<Stock> result = new ArrayList<>();
        while (currentPage <= TOTAL_PAGES) {
            result.addAll(extractSingle(String.format(URL, currentPage)));
            currentPage++;
        }
        WebDriver.close();
        return result;
    }

    private List<Stock> extractSingle(String url) {
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
            try {
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                String name = tds.get(0).findElement(By.tagName("a")).getAttribute("innerHTML");
                String code = tds.get(1).findElement(By.tagName("a")).getAttribute("innerHTML");
                String volumn = tds.get(7).findElement(By.tagName("span")).getAttribute("innerHTML");
                String pe = tds.get(8).getAttribute("innerHTML");
                String range52 = tds.get(6).getAttribute("innerHTML");
                Stock stock = new Stock();
                stock.setName(name);
                stock.setCode(code);
                stock.setPe(pe);
                stock.setVolumn(volumn);
                stock.setRange52(range52);
                result.add(stock);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }
}
