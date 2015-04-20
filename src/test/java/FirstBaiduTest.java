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

public class FirstBaiduTest {
    WebDriver webDriver;
    File file, inputfile,filesb,fileqfii, fileboth;

    @Before
    public void setup() {
        webDriver = new FirefoxDriver();
        file = new File("/Users/twer/project/zboDoWindIn/src/test/java/logout.txt");
        inputfile = new File("/Users/twer/project/zboDoWindIn/src/test/java/input.txt");
        filesb=new File("/Users/twer/project/zboDoWindIn/src/test/java/sb.html");
        fileqfii=new File("/Users/twer/project/zboDoWindIn/src/test/java/qfii.html");
        fileboth=new File("/Users/twer/project/zboDoWindIn/src/test/java/botd.html");
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
    public void should_get_last_quarter_data(){
        String url="http://www.windin.com/home/stock/stock-mh/300168.SZ.shtml";
        webDriver.get(url);
        WebElement link2 = webDriver.findElement(By.id("report2"));
        link2.click();
        WebElement div = webDriver.findElement(By.id("MainHolder_Sumary2"));
        System.out.println(div.getAttribute("outerHTML"));
    }
    @Test
    //@Ignore
    public void should_get_windin_for_stock() {
        try {
            List<String> lines = FileUtils.readLines(inputfile);
            for (String line : lines) {
                String code=line.split("\t")[0];
                String name=line.split("\t")[1];
                String market="SZ";
                if(code.charAt(0)=='6'){
                    market="SH";
                }
                String source = String.format("http://www.windin.com/home/stock/stock-mh/%s.%s.shtml",code,market);
                String img = String.format("http://chart.windin.com/HQchart/kline/10000000/d/%s.%s.jpg", code, market);
                getOne(source,img,name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOne(String source,String img, String name) throws IOException {
        webDriver.get(source);
        logname(name,file,source);
        WebElement div = webDriver.findElement(By.id("MainHolder_Sumary1"));
        List<WebElement> trs = div.findElements(By.tagName("tr"));
        for (WebElement tr : trs) {
            StringBuilder line = RecoredLine(tr);
            Log_SBQFII(line,name,source,img);
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

    private void logname(String name, File file,String url) throws IOException {
        FileUtils.writeStringToFile(file, String.format("<a href=%s>========%s========</a><br>\n", url,name), Charset.defaultCharset(), true);
    }

    private void Log_SBQFII(StringBuilder line,String name,String url,String img) throws IOException {
        String type="";
        if(IsSB(line)){
            logname(name, filesb, url);
            FileUtils.writeStringToFile(filesb, line.append("<br>\n").toString(), Charset.defaultCharset(), true);
            SaveImage(img,filesb);
            type="sb";
        }
        if(IsQfii(line)){
            logname(name, fileqfii, url);
            FileUtils.writeStringToFile(fileqfii, line.append("<br>\n").toString(), Charset.defaultCharset(), true);
            SaveImage(img,fileqfii);
            type="qfii";
        }
        if(!type.isEmpty())
            SaveLastQ(type);
    }

    private boolean IsSB(StringBuilder line) {
        return line.toString().contains("社保");
    }

    private boolean IsQfii(StringBuilder line) {
        return line.toString().toUpperCase().contains("QFII");
    }

    private void SaveLastQ(String type) throws IOException {
        WebElement link2 = webDriver.findElement(By.id("report2"));
        link2.click();
        WebElement div2 = webDriver.findElement(By.id("MainHolder_Sumary2"));
        List<WebElement> trs = div2.findElements(By.tagName("tr"));

        if(type.contentEquals("sb")){
            for (WebElement tr : trs) {
                StringBuilder line = getStringLine(tr);
                if(IsSB(line)) {
                    FileUtils.writeStringToFile(filesb, line.append("<br>\n").toString(), Charset.defaultCharset(), true);
                }
            }
        }
        else if(type.contentEquals("qfii")){
            for (WebElement tr : trs) {
                StringBuilder line = getStringLine(tr);
                if(IsSB(line)) {
                    FileUtils.writeStringToFile(filesb, line.append("<br>\n").toString(), Charset.defaultCharset(), true);
                }
            }
        }
    }

    private void SaveImage(String img,File file) throws IOException {
        FileUtils.writeStringToFile(file, String.format("<img src=%s><br>\n",img), Charset.defaultCharset(), true);
    }

}
