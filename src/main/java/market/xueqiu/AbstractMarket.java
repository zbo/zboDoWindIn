package market.xueqiu;

import java.io.File;

/**
 * Created by bob.zhu on 15-5-1.
 */
public abstract class AbstractMarket {


    public abstract Integer getTotal();
    public abstract String getURL();
    public abstract File getFile();
    public abstract File getHTMLSB();
    public abstract File getHTMLQFII();
    public abstract String getName();

    public String WorkingBase="/Users/bob.zhu/project/zboDoWindIn/src/app-resources/";
    public String URL = GetUrlBase(getName());
    public String OUTPUT = WorkingBase+ getName() +"-stock.json";
    public String HTMLOUTSB = WorkingBase+ getName() +"-sb.html";
    public String HTMLOUTQFII = WorkingBase+ getName() +"-qfii.html";

    private String GetUrlBase(String market){
        return "http://xueqiu.com/hq#exchange=CN&plate=1_1_1&firstName=1&secondName=1_1&type="+
                market+
                "&order=asc&orderby=symbol&page=%s";
    }



}
