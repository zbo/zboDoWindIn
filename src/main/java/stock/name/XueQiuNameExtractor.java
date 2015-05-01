package stock.name;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import stock.meta.Stock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XueQiuNameExtractor {
    private static String URL_SHA = "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=sha&order=asc&orderby=symbol&page=%s";
    private static String URL_SZA = "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=sza&order=asc&orderby=symbol&page=%s";
    private static String URL_ZXB = "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=zxb&order=asc&orderby=symbol&page=%s";
    private static String URL_CYB = "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=cyb&order=asc&orderby=symbol&page=%s";
    private static String output = "/Users/twer/project/zboDoWindIn/src/app-resources/all-stock.json";
    private File file = new File(output);
    private static Integer TOTAL_SHA_PAGES = 32;
    private static Integer TOTAL_SZA_PAGES = 48;
    private static Integer TOTAL_ZXB_PAGES = 22;
    private static Integer TOTAL_CYB_PAGES = 13;

    private WebDriver WebDriver;

    public List<Stock> extract() {
        List<Stock> result = new ArrayList<>();
        extractSingleType(result,TOTAL_SHA_PAGES,URL_SHA);
        extractSingleType(result,TOTAL_SZA_PAGES,URL_SZA);
        extractSingleType(result,TOTAL_ZXB_PAGES,URL_ZXB);
        extractSingleType(result,TOTAL_CYB_PAGES,URL_CYB);
        return result;
    }

    private void extractSingleType(List<Stock> result,int total,String url) {
        try {
            FileUtils.forceDelete(file);
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
        Gson gson = new Gson();
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
                String json = gson.toJson(stock);
                System.out.println(json);
                FileUtils.write(file, json + "\n", "UTF-8", true);
                result.add(stock);
        }
        return result;
    }
}
