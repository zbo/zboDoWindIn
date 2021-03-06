package market.xueqiu;

import java.io.File;

/**
 * Created by bob.zhu on 15-5-1.
 */
public class SZAMarket extends AbstractMarket {

    public Integer TOTAL = 48;
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

    @Override
    public File getHTMLSB() {
        return new File(HTMLOUTSB);
    }

    @Override
    public File getHTMLQFII() {
        return new File(HTMLOUTQFII);
    }

    @Override
    public String getName() {
        return "sza";
    }
}
