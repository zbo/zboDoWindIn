package stock.name;

import java.io.File;

/**
 * Created by twer on 15-5-1.
 */
public class ZXBMarket extends AbstractMarket {
    public String URL = "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type=zxb&order=asc&orderby=symbol&page=%s";
    public String OUTPUT = "/Users/twer/project/zboDoWindIn/src/app-resources/zxb-stock.json";
    public Integer TOTAL = 22;
    @Override
    public Integer getTotal() {
        return TOTAL;
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public File getFile() {
        return new File(OUTPUT);
    }
}
