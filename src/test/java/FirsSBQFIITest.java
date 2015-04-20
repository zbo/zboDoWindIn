import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class FirsSBQFIITest {
    WebDriver webDriver;
    File file, inputfile, filesb, fileqfii, fileboth;
    public enum OwnerType{
        SB,QFII
    }
    @Before
    public void setup() {
        webDriver = new FirefoxDriver();
        file = new File("/Users/twer/project/zboDoWindIn/src/test/java/logout.txt");
        inputfile = new File("/Users/twer/project/zboDoWindIn/src/test/java/input.txt");
        filesb = new File("/Users/twer/project/zboDoWindIn/src/test/java/sb.html");
        fileqfii = new File("/Users/twer/project/zboDoWindIn/src/test/java/qfii.html");
        fileboth = new File("/Users/twer/project/zboDoWindIn/src/test/java/botd.html");
        FileUtils.deleteQuietly(file);
        FileUtils.deleteQuietly(filesb);
        FileUtils.deleteQuietly(fileqfii);
        FileUtils.deleteQuietly(fileboth);
    }

    @After
    public void teardown() {
        webDriver.close();
    }

    @Ignore
    @Test
    public void shoud_get_baidu_response() {
        String source = "http://www.baidu.com";
        webDriver.get(source);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        Assert.assertTrue(webElement.getAttribute("outerHTML").length() > 0);
    }

    @Test
    @Ignore
    public void should_get_last_quarter_data() {
        String url = "http://www.windin.com/home/stock/stock-mh/300168.SZ.shtml";
        webDriver.get(url);
        WebElement link2 = webDriver.findElement(By.id("report2"));
        link2.click();
        WebElement div = webDriver.findElement(By.id("MainHolder_Sumary2"));
        System.out.println(div.getAttribute("outerHTML"));
    }

    @Test
    @Ignore
    public void should_replace_day_to_week() {
        String days = "http://chart.windin.com/HQchart/kline/10000000/d/%s.%s.jpg";
        String weeks = days.replace("/d/", "/w/");
        Assert.assertTrue(weeks.contentEquals("http://chart.windin.com/HQchart/kline/10000000/w/%s.%s.jpg"));
    }

    @Test
    //@Ignore
    public void should_get_windin_for_stock() {
        try {
            List<String> lines = FileUtils.readLines(inputfile);
            for (String line : lines) {
                String code = line.split("\t")[0];
                String name = line.split("\t")[1];
                String market = "SZ";
                if (code.charAt(0) == '6') {
                    market = "SH";
                }
                Windin windin = new Windin(code, market, name);
                getOne(windin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOne(Windin windin) throws IOException {
        String source = windin.getSource();
        webDriver.get(source);
        LogName(windin, file);
        WebElement div = webDriver.findElement(By.id("MainHolder_Sumary1"));
        List<WebElement> trs = div.findElements(By.tagName("tr"));
        for (WebElement tr : trs) {
            StringBuilder line = RecoredLine(tr);
            Log_SBQFII(line, windin);
        }

    }

    private StringBuilder RecoredLine(WebElement tr) throws IOException {
        StringBuilder line = getStringLine(tr);
        FileUtils.writeStringToFile(file, line.append("\n").toString(), Charset.defaultCharset(), true);
        return line;
    }

    private StringBuilder getStringLine(WebElement tr) {
        StringBuilder line = new StringBuilder();
        for (WebElement td : tr.findElements(By.tagName("td"))) {
            String innerHtml = td.getAttribute("innerHTML");
            line.append(" " + innerHtml);
        }
        return line;
    }

    private void Log_SBQFII(StringBuilder line, Windin windin) throws IOException {
        File file;
        OwnerType type;
        if (IsSB(line)) {
            file = this.filesb;
            type = OwnerType.SB;
        } else if (IsQfii(line)) {
            file = this.fileqfii;
            type = OwnerType.QFII;
        } else {
            return;
        }
        LogName(windin, file);
        LogLine(line, file);
        SaveLastQ(type);
        SaveImage(windin.getImage(), file);
    }

    private void SaveLastQ(OwnerType type) throws IOException {
        WebElement link2 = webDriver.findElement(By.id("report2"));
        link2.click();
        WebElement div2 = webDriver.findElement(By.id("MainHolder_Sumary2"));
        List<WebElement> trs = div2.findElements(By.tagName("tr"));

        if (type==OwnerType.SB) {
            for (WebElement tr : trs) {
                StringBuilder line = getStringLine(tr);
                if (IsSB(line)) {
                    LogLine(line, filesb);
                }
            }
        } else if (type==OwnerType.QFII) {
            for (WebElement tr : trs) {
                StringBuilder line = getStringLine(tr);
                if (IsQfii(line)) {
                    LogLine(line, fileqfii);
                }
            }
        }
    }

    private void LogLine(StringBuilder line, File file) throws IOException {
        FileUtils.writeStringToFile(file, line.append("<br>\n").toString(), Charset.defaultCharset(), true);
    }

    private void LogName(Windin windin, File file) throws IOException {
        FileUtils.writeStringToFile(file, String.format("<a href=%s>========%s========</a><br>\n", windin.getSource(), windin.getName()), Charset.defaultCharset(), true);
        FileUtils.writeStringToFile(file, String.format("<a href=%s>基本面</a><br>\n", windin.getBaseInfo()), Charset.defaultCharset(), true);
    }

    private void SaveImage(String img, File file) throws IOException {
        FileUtils.writeStringToFile(file, String.format("<img src=%s>\n", img), Charset.defaultCharset(), true);
        FileUtils.writeStringToFile(file, String.format("<img src=%s><br>\n", img.replace("/d/", "/w/")), Charset.defaultCharset(), true);
    }

    private boolean IsSB(StringBuilder line) {
        return line.toString().contains("社保");
    }

    private boolean IsQfii(StringBuilder line) {
        return line.toString().toUpperCase().contains("QFII");
    }

}
