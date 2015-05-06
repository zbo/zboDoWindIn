package stock.meta;

import com.google.common.base.Strings;

public class Stock {
    private Market market;
    private String name;
    private String code;
    private String pe;
    private int pb;
    private String volumn;
    private String range52;
    private float price;
    private int lastupdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getRange52() {
        return range52;
    }

    public void setRange52(String range52) {
        this.range52 = range52;
    }

    public int getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(int lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getMarketCode() {
        return Strings.isNullOrEmpty(this.getCode()) ? "" : this.getCode().substring(2);
    }

    public String getMarketShortName() {
        return Strings.isNullOrEmpty(this.getCode()) ? "" : this.getCode().substring(0, 2);
    }
}
