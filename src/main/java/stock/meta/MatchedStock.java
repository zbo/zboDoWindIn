package stock.meta;

import com.google.common.base.Strings;

public class MatchedStock {
    private String qfiiThisQ;
    private String sbThisQ;
    private String qfiiLastQ;
    private String sbLastQ;
    private String imageMpnth;
    private String imageWeek;
    private String imageDay;
    private Stock sotck;
    private MatchType matchType;

    public String getSbThisQ() {
        return sbThisQ;
    }

    public void setSbThisQ(String sbThisQ) {
        this.sbThisQ = sbThisQ;
    }

    public Stock getSotck() {
        return sotck;
    }

    public void setSotck(Stock sotck) {
        this.sotck = sotck;
    }

    public String getQfiiThisQ() {
        return qfiiThisQ;
    }

    public void setQfiiThisQ(String qfiiThisQ) {
        this.qfiiThisQ = qfiiThisQ;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public String getSbLastQ() {
        return sbLastQ;
    }

    public void setSbLastQ(String sbLastQ) {
        this.sbLastQ = sbLastQ;
    }

    public String getQfiiLastQ() {
        return qfiiLastQ;
    }

    public void setQfiiLastQ(String qfiiLastQ) {
        this.qfiiLastQ = qfiiLastQ;
    }

    public String getImageMpnth() {
        return imageMpnth;
    }

    public void setImageMpnth(String imageMpnth) {
        this.imageMpnth = imageMpnth;
    }

    public String getImageWeek() {
        return imageWeek;
    }

    public void setImageWeek(String imageWeek) {
        this.imageWeek = imageWeek;
    }

    public String getImageDay() {
        return imageDay;
    }

    public void setImageDay(String imageDay) {
        this.imageDay = imageDay;
    }

    public boolean thisQmatched() {
        if(Strings.isNullOrEmpty(this.getQfiiThisQ()) && Strings.isNullOrEmpty(this.getSbThisQ())){
            return false;
        }
        return true;
    }

    enum MatchType {SB, QFII, BOTH}
}
